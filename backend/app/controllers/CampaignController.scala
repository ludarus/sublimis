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

class CampaignController @Inject() (
    cc: ControllerComponents,
    lc: LettuceConnection,
    pg: ScalaJdbcConnection,
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
              val futureRes = lc.newCampaign(usr.userid)

              futureRes.map { cid =>
                // UNIT ENDS
                Created(
                  Json.obj(
                    "cid" -> cid.toString()
                  )
                )
              }

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

  // ======================== WEBSOCKET CONTROLLER METHODS ========================

  // test echo func
  def echo = WebSocket.accept[String, String] { request =>
    println("connection activated")
    // log the message to stdout and send response back to client
    ActorFlow.actorRef { out => MyWebSocketActor.props(out) }
  }

  // verify before accepting ws request
  // TODO i really need to make a nice wrapper function around this verification flow becauase it's killing me
  def live = WebSocket.acceptOrResult[String, String] { request =>
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
              // storing thing in the other thing
              Right(
                ActorFlow.actorRef { out =>
                  MyWebSocketActor.props(out)
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
