package websocket

import custom.ChatMessage
import org.apache.pekko.actor._
import websocket.LiveCampaignActor
import custom.SublimisUser

// making a user actor for the parent actor (campaign actor) to delegate specific websocket connections to each actor, but contained in a lobby
// should store own uid for quick leaving and joining?

object CampaignUserActor {
  def props(out: ActorRef, parent: ActorRef, userObj: SublimisUser) = Props(
    new CampaignUserActor(out, parent, userObj)
  )
}

class CampaignUserActor(out: ActorRef, parent: ActorRef, user: SublimisUser)
    extends Actor {
  import LiveCampaignActor._

  // ideally make a service call from within the ws actor to directly update the redis, but this seems like a bad idea in practice
  override def preStart(): Unit = {
    parent ! Join(self, user)
  }

  override def postStop(): Unit = {
    parent ! Leave(self, user)
  }

  def receive = {

    // on message from individual websocket connection
    case msg: String =>
      parent ! Broadcast(new ChatMessage(msg, self, user))

    // on message from parent
    case msg: ChatMessage =>
      // sending message back to client
      println(s"sending messgae ${msg.payload} back to user")
      out ! msg.getJson().toString()
  }
}
