package websocket

import custom.Message
import org.apache.pekko.actor._
import websocket.LiveCampaignActor

// making a user actor for the parent actor (campaign actor) to delegate specific websocket connections to each actor, but contained in a lobby
// should store own uid for quick leaving and joining?

object CampaignUserActor {
  def props(out: ActorRef, parent: ActorRef, userId: String) = Props(
    new CampaignUserActor(out, parent, userId)
  )
}

class CampaignUserActor(out: ActorRef, parent: ActorRef, uid: String)
    extends Actor {
  import LiveCampaignActor._

  // ideally make a service call from within the ws actor to directly update the redis, but this seems like a bad idea in practice
  override def preStart(): Unit = {
    parent ! Join(self, uid)
  }

  override def postStop(): Unit = {
    parent ! Leave(self, uid)
  }

  override def toString(): String = {
    uid
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
