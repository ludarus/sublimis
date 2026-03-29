package controllers

import javax.inject.Inject
import play.api.mvc.Action
import play.api.mvc._
import play.api.libs.json.Json
import java.sql.Array
import play.api.http.HttpEntity
import org.apache.pekko.util.ByteString
import database.ScalaJdbcConnection
import org.apache.pekko.util.Helpers.Requiring

class BasicController @Inject() (
    cc: ControllerComponents,
    db: ScalaJdbcConnection
) extends AbstractController(cc) {

  implicit val ec: scala.concurrent.ExecutionContext =
    scala.concurrent.ExecutionContext.global
  // common server results:
  // val ok            = Ok("Hello world!")
  // val notFound     = NotFound
  // val pageNotFound = NotFound(<h1>Page not found</h1>)
  // val badRequest   = BadRequest(views.html.form(formWithErrors))
  // val oops         = InternalServerError("Oops")
  // val anyStatus    = Status(488)("Strange response type")
  // redirect         = Redirect("route")

  def index() = Action {
    // result is a struct like class that packages the header and body into the correct return type for action
    // remember that in scala, the last expression of a function is it's return value

    // Result(
    //   header = ResponseHeader(200, Map.empty),
    //   body = HttpEntity.Strict(ByteString("server is enabled."), Some("text/plain"))
    //
    // )

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

  def fullRedirect(name: String) = Action {
    Redirect("/")
  }

  def callDb() = Action.async {
    db.getAmount().map {
      case Some(poo) => {
        Ok(poo)
      }
      case None => NotFound("not found")
    }
  }

}
