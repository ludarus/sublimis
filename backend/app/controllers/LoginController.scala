package controllers

import play.api.mvc._
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

class LoginController @Inject() (
    cc: ControllerComponents,
    pg: ScalaJdbcConnection,
    config: Configuration
) extends AbstractController(cc) {

  implicit val ec: scala.concurrent.ExecutionContext =
    scala.concurrent.ExecutionContext.global

  private val JsonFactory = GsonFactory.getDefaultInstance();
  private val clientId = config.get[String]("google.clientId")
  // private val dbPass = config.get[String]("db.default.password")

  println("clientid = " + clientId)
  // println("pass = " + dbPass)
  val verifier = new GoogleIdTokenVerifier.Builder(
    new NetHttpTransport(),
    JsonFactory
  )
    .setAudience(Collections.singletonList(clientId))
    .build();

  // ======================== HELPER METHODS ========================
  def verifyRequest(request: Request[AnyContent]) = { 
    //TODO REFACTOR THE COOKIE AND USER VERIFICATION INTO A HELPER FUNCTION

  }
  // ======================== ACTION CONTROLLER METHODS ========================

  def giveUserInfo() = Action.async { request =>
    println("here is the requersdt " + request.toString())
    request.cookies.get("session_id") match {
      case Some(sid) =>
        println("sessionid recieved: " + sid.toString())
        val c = pg.verifySid(sid.value)

        c.map { option =>
          option match {
            case Some(usr) =>
              println("user found " + usr.name)
              Created(
                usr.getJson()
              )
            case None =>
              println("user not found")
              BadRequest("failed to verify")
          }
        }
      case None =>
        println("no cookie sent")
        Future.successful(BadRequest("no cookie sent"))
    }
  }

  def logout() = Action.async { request =>
    request.cookies.get("session_id") match {
      case Some(sid) =>
        println("sessionid recieved: " + sid.toString())
        val c = pg.invalidateSessionId(sid.value)

        c.map { i =>
          println("logged out " + i + " amount")
          Created("succesfluly logged out")
        }
      case None =>
        println("no cookie sent")
        Future.successful(BadRequest("no cookie sent"))
    }
  }

  def signinVerification() = Action.async { request =>
    println("connection recieved")
    println(request.cookies.toString())
    // getting main token thing
    val credential = (request.body.asJson.get \ "credential").asOpt[String];
    credential match {

      // TODO: POSTGRESQL NOTE: WHEN MAKING OTHER TABLES, DO AS FOLLOWS:
      // userid bigint references googleusers (id)
      case Some(credReal) =>
        println(credReal)
        try {
          println("start of try")
          println("credential = " + credReal)
          // verifying token
          val idToken = verifier.verify(credReal)
          println("verified")
          // extracting payload
          val payload = idToken.getPayload()
          println("updaing usr")

          val f = pg.handleUser(payload)

          f.map { usr =>
            println("usr = " + usr.toString)
            Created(
              usr.getJson()
            )
              .withCookies(
                Cookie(
                  name = "session_id",
                  value = usr.sessionid,
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
