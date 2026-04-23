= websockets and redis flow
- for live campains, there should be a websocket service that can handle multiple lobbies filled with players
- the websocket service does some processing, then publishes the event to redis, which websocket connections for the players in that lobby are subscribed to
  - this event is then sent back to the clients of the other players in the lobby
