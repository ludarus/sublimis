package websocket

import org.apache.pekko.actor._
import scala.collection.immutable.Set
import custom.Message

//companion singleton object for helper methods & case classes
object LiveCampaignActor {
  // like a factory method to create an instance of the actor with properties
  def props() = Props(new LiveCampaignActor())

  // case classes are public and immutable. used for sending signals between the actorRefs
  case class Join(user: ActorRef)
  case class Leave(user: ActorRef)
  case class Broadcast(message: String)
}

class LiveCampaignActor() extends Actor {
  import LiveCampaignActor._

  var clients = Set.empty[ActorRef]

  def receive = {
    case Join(user) =>
      println("adding user to actor")
      clients += user

    case Leave(user) =>
      println("removing user from actor")
      clients -= user

    case Broadcast(message) =>
      println("actor broadcasting " + message)
      // sending to each client
      clients.foreach { client =>
        client ! new Message(message, self)
      }

  }
}
