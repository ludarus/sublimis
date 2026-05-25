package websocket

import custom.Message
import org.apache.pekko.actor._
import websocket.LiveCampaignActor

// making a user actor for the parent actor (campaign actor) to delegate specific websocket connections to each actor, but contained in a lobby

object CampaignUserActor {
  def props(out: ActorRef, parent: ActorRef) = Props(
    new CampaignUserActor(out, parent)
  )
}

class CampaignUserActor(out: ActorRef, parent: ActorRef) extends Actor {
  import LiveCampaignActor._

  override def preStart(): Unit = {
    parent ! Join(self)
  }

  override def postStop(): Unit = {
    parent ! Leave(self)
  }

  def receive = {

    // on message from individual websocket connection
    case msg: String =>
      parent ! Broadcast(msg)

    // on message from parent
    case msg: Message =>
      // sending message back to client
      out ! msg.payload
  }
}
