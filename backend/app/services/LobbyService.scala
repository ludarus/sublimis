// service that manages lobbies and their websocket actors

package services

import javax.inject.Singleton
import javax.inject.Inject
import database.LettuceConnection

@Singleton
class LobbyService @Inject() (
    lc: LettuceConnection
    // lobbyRegistry: LobbyRegistry TODO MAKE THIS
) {
  // maps campiagn ids to actors
  val lobbyRegistry: Map[String, Int] = Map()
}
