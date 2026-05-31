package websocket

import org.apache.pekko.actor._
import scala.collection.immutable.Set
import custom.Message
import services.LiveCampaignService

//companion singleton object for helper methods & case classes
object LiveCampaignActor {
  // like a factory method to create an instance of the actor with properties
  def props(campaignId: String, campService: LiveCampaignService) = Props(
    new LiveCampaignActor(campaignId, campService)
  )

  // case classes are public and immutable. used for sending signals between the actorRefs
  case class Join(user: ActorRef, uid: String)
  case class Leave(user: ActorRef, uid: String)
  case class Broadcast(message: String)
}

//should store cid? MAYBE. This would defeat the purpose of a map? No it wouldn't because the map is optimized for searching.
// im going to store cid

//maybe put a reference to the service so it can call methods both ways?

class LiveCampaignActor(cid: String, service: LiveCampaignService)
    extends Actor {
  import LiveCampaignActor._

  var clients = Set.empty[ActorRef]

  def receive = {
    case Join(user, uid) =>
      println("adding user to actor")
      clients += user
      clients.foreach { client =>
        client ! new Message(uid + " has joined", self)
      }
    // should tell service that user has left so it can interface with redis and such
    case Leave(user, uid) =>
      clients -= user
      // broadcasting message
      clients.foreach { client =>
        client ! new Message(user.toString() + " has left", self)
      }
      println("removing user from actor")

      // updating redis information
      service.removePlayer(cid, uid)

      // checking if there's no users left, then closing the game automatically
      if (clients.size == 0) {
        // updating redis information
        service.removeCampaign(cid)
        // kills actor
        self ! PoisonPill
      }

    case Broadcast(message) =>
      println("actor broadcasting " + message)
      // sending to each client
      clients.foreach { client =>
        client ! new Message(message, self)
      }

  }
}
