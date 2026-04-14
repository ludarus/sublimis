package controllers

import org.apache.pekko.stream.scaladsl._
import websocket.MyWebSocketActor
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.Materializer
import play.api.libs.streams.ActorFlow
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

class BasicController @Inject() (
    cc: ControllerComponents,
    db: ScalaJdbcConnection,
    config: Configuration
)(implicit
    system: ActorSystem,
    mat: Materializer
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

  // ======================== ACTION CONTROLLER METHODS =======================================
  def index() = Action {
    // result is a struct like class that packages the header and body into the correct return type for action
    // remember that in scala, the last expression of a function is it's return value

    Ok(<h1>lalalalal</h1>)
      .as(HTML)
      .withCookies(Cookie("lalalaCookie", "p"))
      .bakeCookies()

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

  def giveUserInfo() = Action.async { request =>
    println("here is the requersdt " + request.toString())
    request.cookies.get("session_id") match {
      case Some(sid) =>
        println("sessionid recieved: " + sid.toString())
        val c = db.verifySid(sid.value)

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

  def testRecieve() = Action.async { request =>
    println("post recieved")

    request.cookies.get("session_id") match {
      case Some(sid) =>
        println("sessionid recieved: " + sid.toString())
        val c = db.verifySid(sid.value)

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
          // verifying token
          val idToken = verifier.verify(credReal)
          // extracting payload
          val payload = idToken.getPayload()

          println("updaing usr")
          val f = db.handleUser(payload)

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
  // ======================== WEBSOCKET CONTROLLER METHODS =======================================

  // def socket = WebSocket.accept[String, String] { request =>
  //   println("connection activated")
  //   // log the message to stdout and send response back to client
  //   Flow[String].map { msg =>
  //     println(msg)
  //     "I received your message: " + msg
  //   }
  // }

  def socket = WebSocket.accept[String, String] { request =>
    println("connection activated")
    // log the message to stdout and send response back to client
    ActorFlow.actorRef { out => MyWebSocketActor.props(out) }
  }

}
