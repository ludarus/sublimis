package controllers

import java.util.UUID
import java.util.Collections
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import scala.concurrent.Future
import javax.inject.Inject
import play.api.mvc.Action
import play.api.mvc._
import play.api.libs.json.Json
import java.sql.Array
import play.api.http.HttpEntity
import org.apache.pekko.util.ByteString
import database.ScalaJdbcConnection
import org.apache.pekko.util.Helpers.Requiring
import play.api.Configuration
import views.html.defaultpages.notFound
import scala.util.Failure
import scala.util.Success

class BasicController @Inject() (
    cc: ControllerComponents,
    db: ScalaJdbcConnection,
    config: Configuration
) extends AbstractController(cc) {

  implicit val ec: scala.concurrent.ExecutionContext =
    scala.concurrent.ExecutionContext.global

  val JsonFactory = GsonFactory.getDefaultInstance();
  val clientId = config.get[String]("google.clientId")
  val dbPass = config.get[String]("db.default.password")

  println("clientid = " + clientId)
  println("pass = " + dbPass)
  val verifier = new GoogleIdTokenVerifier.Builder(
    new NetHttpTransport(),
    JsonFactory
  )
    .setAudience(Collections.singletonList(clientId))
    .build();

  def index() = Action {
    // result is a struct like class that packages the header and body into the correct return type for action
    // remember that in scala, the last expression of a function is it's return value

    Ok(<h1>lalalalal</h1>)
      .as(HTML)
      .withCookies(Cookie("lalalaCookie", "p"))
      .bakeCookies()

  }

  def usefulResponse() = Action {
    printf("Fuchk you")
    Ok(
      Json.obj(
        "skibidi" -> "rizz",
        "ohio" -> 3,
        "num" -> 69
      )
    )
  }

  def returnJson() = Action {
    Ok.sendFile(new java.io.File("./public/json/test.json"))
  }

  // todo site
  def notDone() = TODO

  def thing() = Action.async { request =>
    println("recieved info")
    Future.successful(Created("greate job"))
  }

  def fullRedirect(name: String) = Action {
    Redirect("/")
  }

  def signinVerification() = Action.async { request =>
    println("connection recieved")
    // getting main token thing
    val credential = (request.body.asJson.get \ "credential").asOpt[String];
    credential match {

      // TODO: POSTGRESQL NOTE: WHEN MAKING OTHER TABLES, DO AS FOLLOWS:
      // userid bigint references googleusers (id)
      case Some(credReal) =>
        println(credReal)
        try {
          println("start of try")
          // verifying token
          val idToken = verifier.verify(credReal)
          // extracting payload
          val payload = idToken.getPayload()

          println("updaing usr")
          val f = db.handleUser(payload)

          f.map { value =>
            val sessionid = value
            println("sessionid = " + sessionid.toString)
            Created("verified and created sessionid")
              .withCookies(
                Cookie(
                  name = "session_id",
                  value = sessionid.toString,
                  httpOnly = true,
                  secure = true
                )
              )
          }.recover { case exception =>
            println("failed to retrieve sid " + exception)
            InternalServerError("failed to retrieve sid")
          }

        } catch {
          case _: Throwable =>
            println("error foo")
            Future.successful(BadRequest("invalid cred"))
        }

      case None =>
        println("something is seriously wrong lool")

        Future.successful(BadRequest("missing cred"))
    }
  }
}
