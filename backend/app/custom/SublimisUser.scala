package custom

import play.api.libs.json._

class SublimisUser(
    val sessionid: String,
    val userid: String,
    val email: String,
    val name: String,
    val img: String,
    val familyname: String = "",
    val givenname: String = "",
    val email_verified: Boolean = false,
    val locale: String = ""
) {

  def getJson(): JsObject = {
    Json.obj(
      "email" -> email,
      "name" -> name,
      "img" -> img,
      "familyname" -> familyname,
      "givenname" -> givenname,
      "email_verified" -> email_verified,
      "locale" -> locale
    )
  }
}
