package websocket

import org.apache.pekko.actor._
import scala.collection.immutable.Set
import custom.ChatMessage
import services.LiveCampaignService
import custom.SublimisUser

//companion singleton object for helper methods & case classes
object LiveCampaignActor {
  // like a factory method to create an instance of the actor with properties
  def props(campaignId: String, campService: LiveCampaignService) = Props(
    new LiveCampaignActor(campaignId, campService)
  )

  // case classes are public and immutable. used for sending signals between the actorRefs
  case class Join(actor: ActorRef, user: SublimisUser)
  case class Leave(actor: ActorRef, user: SublimisUser)
  case class Broadcast(message: ChatMessage)
}

//should store cid? MAYBE. This would defeat the purpose of a map? No it wouldn't because the map is optimized for searching.
// im going to store cid

//maybe put a reference to the service so it can call methods both ways?

class LiveCampaignActor(cid: String, service: LiveCampaignService)
    extends Actor {
  import LiveCampaignActor._

  var clients = Set.empty[ActorRef]

  def receive = {
    case Join(actor, user) =>
      println("adding user to actor")
      clients += actor
      clients.foreach { client =>
        client ! new ChatMessage(user.name + " has joined", self, user)
      }
    // should tell service that user has left so it can interface with redis and such
    case Leave(actor, user) =>
      clients -= actor
      // broadcasting message
      clients.foreach { client =>
        client ! new ChatMessage(user.name + " has left", self, user)
      }
      println("removing user from actor")

      // updating redis information
      service.removePlayer(cid, user.userid)

      // checking if there's no users left, then closing the game automatically
      if (clients.size == 0) {
        // updating redis information
        service.removeCampaign(cid)
        // kills actor
        self ! PoisonPill
      }

    case Broadcast(message) =>
      println("actor broadcasting " + message.payload)
      // sending to each client
      clients.foreach { client =>
        client ! message
      }

  }
}
