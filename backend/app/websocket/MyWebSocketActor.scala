package websocket

import org.apache.pekko.actor._

object MyWebSocketActor {
  def props(out: ActorRef) = Props(new MyWebSocketActor(out))
}

class MyWebSocketActor(out: ActorRef) extends Actor {
  def receive = { case msg: String =>
    println(msg)
    out ! ("I received your message: " + msg)
  }

  override def postStop() = {
    println("thing is closing")
  }

}
