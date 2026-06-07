package custom

import custom.WsMessage
import org.apache.pekko.actor._
import custom.SublimisUser
import java.sql.Timestamp
import play.api.libs.json._

class ChatMessage(
    payload: String,
    val origin: ActorRef,
    val author: SublimisUser
) extends WsMessage(payload, 1) {

  override def getJson(): JsObject = {
    Json.obj(
      "type" -> msgType,
      "name" -> author.name,
      "img" -> author.img,
      "userid" -> author.userid,
      "time" -> time.getTime(),
      "payload" -> payload
    )
  }

}
