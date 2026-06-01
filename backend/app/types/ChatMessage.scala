package custom

import org.apache.pekko.actor._
import custom.SublimisUser
import java.sql.Timestamp
import play.api.libs.json._

//wrapper class around message
class ChatMessage(
    val payload: String,
    val origin: ActorRef,
    val author: SublimisUser
) {
  val time: Timestamp = new Timestamp(System.currentTimeMillis())

  def getJson(): JsObject = {
    Json.obj(
      "name" -> author.name,
      "img" -> author.img,
      "userid" -> author.userid,
      "time" -> time.getTime(),
      "payload" -> payload
    )
  }
}
