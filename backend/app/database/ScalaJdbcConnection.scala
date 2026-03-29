package database

import javax.inject.Inject
import scala.concurrent.Future
import play.api.db.Database
import database.DatabaseExecutionContext

// @inject just makes play create instances of the constructor parameters
class ScalaJdbcConnection @Inject() (
    db: Database,
    databaseExecutionContext: DatabaseExecutionContext
) {
  def getAmount(): Future[Option[String]] = {
    Future {
      val connection = db.getConnection()
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM gurt")
      var output: String = ""
      if (resultSet.next()) {
        output += resultSet.getString("amount")
        while (resultSet.next()) {
          output += ", " + resultSet.getString("amount")
        }
        Some(output)
      } else {
        None
      }
    }(databaseExecutionContext)
  }
}
