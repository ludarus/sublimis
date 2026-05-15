// service that manages lobbies and their websocket actors

package services

import org.apache.pekko.actor._
import javax.inject.Singleton
import javax.inject.Inject
import database.LettuceConnection
import java.util.UUID
import websocket.LiveCampaignActor
import collection.mutable

@Singleton
class LiveCampaignService @Inject() (
    lc: LettuceConnection
)(implicit
    system: ActorSystem
) {
  // maps campiagn ids to actors
  val lobbyRegistry: mutable.Map[String, ActorRef] = mutable.Map.empty

  def newCampaign(uid: String): UUID = {
    // gen new uuid. the chances of one matching an already existing cid are so low im not even going to check for it (even tho i did for the sessionids)
    val cid = UUID.randomUUID()

    lc.setOwner(uid, cid)

    val lobbyActor =
      system.actorOf(LiveCampaignActor.props(), s"lobbies:${cid.toString()}")

    lobbyRegistry(cid.toString()) = lobbyActor

    cid
  }
}
