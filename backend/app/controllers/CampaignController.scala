package controllers

import play.api.libs.json._
import javax.inject.Inject
import play.api.Configuration
import database.ScalaJdbcConnection
import org.apache.pekko.stream.scaladsl._
import websocket.MyWebSocketActor
import websocket.LiveCampaignActor
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.Materializer
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import database.LettuceConnection
import play.api.mvc.Action
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success
import services.LiveCampaignService
import websocket.CampaignUserActor
import org.apache.pekko.actor.PoisonPill
import play.http.websocket.Message.Close

class CampaignController @Inject() (
    cc: ControllerComponents,
    pg: ScalaJdbcConnection,
    lobbyService: LiveCampaignService,
    config: Configuration
)(implicit
    system: ActorSystem,
    mat: Materializer
) extends AbstractController(cc) {
  implicit val ec: scala.concurrent.ExecutionContext =
    scala.concurrent.ExecutionContext.global
  // ======================== ACTION CONTROLLER METHODS ========================

  def createLiveCampaign = Action.async { request =>
    request.cookies.get("session_id") match {
      case Some(sid) =>
        println("sessionid recieved: " + sid.toString())
        // verifying request
        val c = pg.verifySid(sid.value)

        c.flatMap { option =>
          option match {
            case Some(usr) =>
              println("user found " + usr.name)
              Future.successful(
                Created(
                  Json.obj(
                    "cid" -> lobbyService.newCampaign(usr.userid).toString()
                  )
                )
              )

            case None =>
              println("user not found")
              Future.successful(Unauthorized("failed to verify"))
          }
        }
      case None =>
        println("no cookie sent")
        Future.successful(Unauthorized("no cookie sent"))
    }
  }

  def giveLiveCampaigns = Action {
    // user verification not needed on this, because browsing public campaigns should be fine
    println("giving live info")
    val cids = lobbyService.lobbyRegistry.keys.toArray
    println(cids.length)
    Created(
      Json.obj(
        "cids" -> cids
      )
    )
  }

  // ======================== WEBSOCKET CONTROLLER METHODS ========================

  // test echo func
  def echo = WebSocket.accept[String, String] { request =>
    println("connection activated")
    ActorFlow.actorRef { out => MyWebSocketActor.props(out) }
  }

  // verify before accepting ws request
  // TODO: WHEN IMPLEMENTING CRSF, FOLLOW INSTRUCTIONS HERE: https://www.playframework.com/documentation/3.0.x/ScalaWebSockets#Rejecting-a-WebSocket
  def live(cid: String) = WebSocket.acceptOrResult[String, String] { request =>
    request.cookies.get("session_id") match {
      case Some(sid) =>
        println("worked")
        println("sessionid recieved: " + sid.toString())
        val c = pg.verifySid(sid.value)

        c.map { option =>
          option match {
            case Some(usr) =>
              // on verified user
              println("user found " + usr.name)
              // if campaign exists:
              if (lobbyService.lobbyRegistry.contains(cid)) {
                Right(
                  // creating a child actorRef
                  // TODO check if cid exists in the first place
                  ActorFlow.actorRef { out =>
                    CampaignUserActor.props(
                      out,
                      // handling user and providing parent actor ref at the same time (insane tech)
                      lobbyService.joinCampaign(cid, usr.userid),
                      usr
                    )
                  }
                )
              } else {
                println("campaign dne")
                // Left(NotFound("campaign DNE"))
                // sending a single close frame with error code info because the browser doesn't expose error codes on websocket rejection
                Right(
                  Flow.fromSinkAndSource(
                    Sink.ignore,
                    Source.single(
                      Json
                        .obj(
                          "type" -> 0,
                          "payload" -> "campaign does not exist",
                          "error" -> 404
                        )
                        .toString
                    )
                  )
                )
              }
            case None =>
              println("user not found")
              // Left(Forbidden)
              Right(
                Flow.fromSinkAndSource(
                  Sink.ignore,
                  Source.single(
                    Json
                      .obj(
                        "type" -> 0,
                        "payload" -> "user not found",
                        "error" -> 401
                      )
                      .toString
                  )
                )
              )
          }
        }
      case None =>
        println("didn't worked")
        Future.successful(Left(Forbidden))
    }
  }

}
