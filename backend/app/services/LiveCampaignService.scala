// service that manages lobbies and their websocket actors

package services

import javax.inject.Singleton
import javax.inject.Inject
import database.LettuceConnection

@Singleton
class LiveCampaignService @Inject() (
    lc: LettuceConnection
) {
  // maps campiagn ids to actors
  val lobbyRegistry: Map[String, /*not actually an int*/ Int] = Map()
}
