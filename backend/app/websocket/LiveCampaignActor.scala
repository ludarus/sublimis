package websocket

import play.api.libs.json._
import org.apache.pekko.actor._
import scala.collection.immutable.Set
import custom.ChatMessage
import services.LiveCampaignService
import custom.SublimisUser
import custom.WsMessage

//companion singleton object for helper methods & case classes
object LiveCampaignActor {
  // like a factory method to create an instance of the actor with properties
  def props(campaignId: String, campService: LiveCampaignService) = Props(
    new LiveCampaignActor(campaignId, campService)
  )

  // case classes are public and immutable. used for sending signals between the actorRefs
  case class Join(actor: CampaignUserActor)
  case class Leave(actor: CampaignUserActor)
  case class Broadcast(message: ChatMessage)
}

//should store cid? MAYBE. This would defeat the purpose of a map? No it wouldn't because the map is optimized for searching.
// im going to store cid

//maybe put a reference to the service so it can call methods both ways?
//  -insert something about circular references-

class LiveCampaignActor(cid: String, service: LiveCampaignService)
    extends Actor {
  import LiveCampaignActor._

  // trying out tuples
  var clients = Set.empty[CampaignUserActor]

  def receive = {
    case Join(actor) =>
      println("adding user to actor")
      clients += (actor)
      clients.foreach { client =>
        // updating the client side playerlist
        client.getActor ! new WsMessage(
          // an array of the names of the users
          Json.toJson(clients.toSeq.map(_.user.name).toArray).toString,
          4
        )
        // TODO: make this into a server message not a user message
        client.getActor ! new ChatMessage(
          actor.user.name + " has joined",
          self,
          actor.user
        )
      }
    // should tell service that user has left so it can interface with redis and such
    case Leave(actor) =>
      clients -= (actor)
      // broadcasting message
      clients.foreach { client =>
        client.getActor ! new ChatMessage(
          actor.user.name + " has left",
          self,
          actor.user
        )
      }
      println("removing user from actor")

      // updating redis information
      service.removePlayer(cid, actor.user.userid)

      // checking if there's no users left, then closing the game automatically
      if (clients.size == 0) {
        // updating redis information
        service.removeCampaign(cid)
        // kills actor
        self ! PoisonPill
      }

    case Broadcast(message) =>
      println("actor broadcasting " + message.payload)
      println(message.payload.length())
      // sending to each client
      if (message.payload == "list") {
        println("special messgae")
        println(clients.toString)
        clients.foreach { client =>
          client.getActor ! new ChatMessage(
            "Sending playerlist Now!",
            self,
            message.author
          )

          client.getActor ! new WsMessage(
            // an array of the names of the users
            Json.toJson(clients.toSeq.map(_.user.name).toArray).toString,
            4
          )
        }
      } else {
        clients.foreach { client =>
          client.getActor ! message
        }
      }

  }
}
