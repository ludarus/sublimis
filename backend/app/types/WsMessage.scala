package custom

import org.apache.pekko.actor._
import custom.SublimisUser
import java.sql.Timestamp
import play.api.libs.json._


//enums for msgType
// 0 -> error msg
// 1 -> chat msg
// 2 -> game event
// 3 -> ready signal

class WsMessage(
    val payload: String,
    val msgType: Short
) {
  val time: Timestamp = new Timestamp(System.currentTimeMillis())

  def getJson(): JsObject = {

    Json.obj(
      "type" -> msgType,
      "time" -> time.getTime(),
      "payload" -> payload
    )
  }
}

