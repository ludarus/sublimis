package controllers

import javax.inject.Inject
import play.api.Configuration
import org.apache.pekko.stream.scaladsl._
import websocket.MyWebSocketActor
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.Materializer
import play.api.libs.streams.ActorFlow
import play.api.mvc._

class CampaignController @Inject() (
    cc: ControllerComponents,
    config: Configuration
)(implicit
    system: ActorSystem,
    mat: Materializer
) extends AbstractController(cc) {

  // ======================== WEBSOCKET CONTROLLER METHODS =======================================

  // def socket = WebSocket.accept[String, String] { request =>
  //   println("connection activated")
  //   // log the message to stdout and send response back to client
  //   Flow[String].map { msg =>
  //     println(msg)
  //     "I received your message: " + msg
  //   }
  // }

  def socket = WebSocket.accept[String, String] { request =>
    println("connection activated")
    // log the message to stdout and send response back to client
    ActorFlow.actorRef { out => MyWebSocketActor.props(out) }
  }

}
