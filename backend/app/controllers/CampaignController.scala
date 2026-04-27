package controllers

import javax.inject.Inject
import play.api.Configuration
import org.apache.pekko.stream.scaladsl._
import websocket.MyWebSocketActor
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
    config: Configuration
)(implicit
    system: ActorSystem,
    mat: Materializer
) extends AbstractController(cc) {

  // ======================== ACTION CONTROLLER METHODS ========================

  def createLiveCampaign = Action { request =>
    println("creatlivecampgaian")
    request.cookies.get("session_id") match {
      case Some(sid) =>
        println("sessionid recieved: " + sid.toString())
        lc.ping()
        Created("recieved cookie")
      case None =>
        println("no cookie sent")
        BadRequest("no cookie sent")
    }
  }

  // ======================== WEBSOCKET CONTROLLER METHODS ========================

  def echo = WebSocket.accept[String, String] { request =>
    println("connection activated")
    // log the message to stdout and send response back to client
    ActorFlow.actorRef { out => MyWebSocketActor.props(out) }
  }

}
