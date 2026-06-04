// service that manages lobbies and their websocket actors

package services

import org.apache.pekko.actor._
import javax.inject.Singleton
import javax.inject.Inject
import database.LettuceConnection
import java.util.UUID
import websocket.LiveCampaignActor
import websocket.CampaignUserActor
import collection.mutable

@Singleton
class LiveCampaignService @Inject() (
    lc: LettuceConnection
)(implicit
    system: ActorSystem
) {
  implicit val ec: scala.concurrent.ExecutionContext =
    scala.concurrent.ExecutionContext.global
  // maps campiagn ids to actors
  val lobbyRegistry: mutable.Map[String, ActorRef] = mutable.Map.empty

  // called on new campaign creation
  def newCampaign(uid: String): UUID = {
    // gen new uuid. the chances of one matching an already existing cid are so low im not even going to check for it (even tho i did for the sessionids)
    val cid = UUID.randomUUID()

    lc.setOwner(cid, uid)

    val lobbyActor =
      system.actorOf(
        LiveCampaignActor.props(cid.toString(), this),
        s"lobbies:${cid.toString()}"
      )

    // mapping the cid to the actor object
    lobbyRegistry(cid.toString()) = lobbyActor

    cid
  }

  // called on websocket connection, returns parent lobby actor so that a child actor can be added to it
  def joinCampaign(cid: String, uid: String): ActorRef = {
    // checking if person is the owner of the campaign
    // TODO check perms as well, eg if user isnt allowed to join campaign
    // reminder: map is for futures, match is for options
    lc.getOwner(cid).map { owner =>
      if (owner == uid) {
        // TODO pass soem special value or cookie back to client to signify role
        println("user is the owenr")
      }
    }

    // adding user as a member of the campaign. storing as uid
    lc.addPlayer(cid, uid)

    println("added user to thing")

    // returning the parent actor
    lobbyRegistry(cid)
  }

  // TOO MUCH ABSTRACTION. COMBINE LETTUCECONNECTION AND THIS CLASS??
  // called on websocket disconnection
  def removePlayer(cid: String, uid: String) = {
    lc.removePlayer(cid, uid)
  }

  // called on final player websocket disconnection
  def removeCampaign(cid: String) = {
    //TODO only remove the campaign afrer 5 minutes of no players?
    lc.removeCampaign(cid)
    lobbyRegistry.remove(cid)
    println("removing campaign " + cid)

  }

}
