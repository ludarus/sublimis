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

class CampaignController @Inject() (
    cc: ControllerComponents,
    lc: LettuceConnection,
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
              // UNIT GOES HERE
              lc.ping()

              // UNIT ENDS
              Future.successful(
                Created(
                  Json.obj(
                    "cid" -> lobbyService.newCampaign(usr.userid).toString()
                  )
                )
              )

            case None =>
              println("user not found")
              Future.successful(BadRequest("failed to verify"))
          }
        }
      case None =>
        println("no cookie sent")
        Future.successful(BadRequest("no cookie sent"))
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
    // log the message to stdout and send response back to client
    ActorFlow.actorRef { out => MyWebSocketActor.props(out) }
  }

  // verify before accepting ws request
  // TODO i really need to make a nice wrapper function around this verification flow becauase it's killing me
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
              Right(
                // creating a child actorRef
                ActorFlow.actorRef { out =>
                  CampaignUserActor.props(
                    out,
                    // handling user and providing parent actor ref at the same time (insane tech)
                    lobbyService.joinCampaign(cid, usr.userid)
                  )
                }
              )
            case None =>
              println("user not found")
              Left(Forbidden)
          }
        }
      case None =>
        println("didn't worked")
        Future.successful(Left(Forbidden))
    }
  }

}
