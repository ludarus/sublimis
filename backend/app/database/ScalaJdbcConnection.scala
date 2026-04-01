package database

import java.util.UUID
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload
import javax.inject.Inject
import scala.concurrent.Future
import play.api.db.Database
import java.sql.Connection
import java.sql.Timestamp
import database.DatabaseExecutionContext

// @inject just makes play create instances of the constructor parameters
class ScalaJdbcConnection @Inject() (
    db: Database,
    databaseExecutionContext: DatabaseExecutionContext
) {

  // updates the user in DB, verifies token, generates sessionID
  def handleUser(payload: Payload): Future[UUID] = {
    Future {
      val connection = db.getConnection()
      // see if there's already an entry for user
      val findQuerey = """
      SELECT * FROM googleusers WHERE id = ?
      """

      val findStmnt = connection.prepareStatement(findQuerey)
      findStmnt.setString(1, payload.getSubject())
      val usrExistsRes = findStmnt.executeQuery()
      val sessionid = generateSessionId(connection)
      if (usrExistsRes.next()) {
        // update existing info
        println("existing user found, updating info")

        val sql = """
        UPDATE googleusers
        SET email = ?,
            name = ?,
            familyname = ?,
            givenname = ?,
            email_verified = ?,
            locale = ?
        WHERE id = ?
        """
        println(payload.getSubject())

        val prpStmnt = connection.prepareStatement(sql)
        prpStmnt.setString(1, payload.getEmail())
        prpStmnt.setString(2, payload.get("name").toString())
        prpStmnt.setString(3, payload.get("family_name").toString())
        prpStmnt.setString(4, payload.get("given_name").toString())
        prpStmnt.setBoolean(
          5,
          payload.get("email_verified").toString().toBoolean
        )
        prpStmnt.setString(
          6,
          if (payload.get("locale") == null) { "" }
          else { payload.get("locale").toString }
        )
        prpStmnt.setString(7, payload.getSubject())

        prpStmnt.executeUpdate()
        println("sucessfully updated user")
        prpStmnt.close()

        // creating session id, updating previous info

        val ssn = """
        UPDATE sessions 
          SET sessionid = ?,
          created = ?,
          expires = ?
        WHERE userid = ?
        """
        val ssnStmnt = connection.prepareStatement(ssn)

        ssnStmnt.setString(4, payload.getSubject())
        ssnStmnt.setObject(1, sessionid)
        ssnStmnt.setTimestamp(2, new Timestamp(System.currentTimeMillis))
        // 1 hr expiry
        ssnStmnt.setTimestamp(
          3,
          new Timestamp(System.currentTimeMillis + 3600000)
        )

        val resulting = ssnStmnt.executeUpdate()
        println(resulting)
        // }

        println("sucessfully updated usr in sessions")
        ssnStmnt.close()
      } else {
        // add new user
        println("no user found, adding new user")

        val sql = """
        INSERT INTO googleusers
        (id, email, name, familyname, givenname, email_verified, locale)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        """

        val prpStmnt = connection.prepareStatement(sql)
        prpStmnt.setString(1, payload.getSubject())
        prpStmnt.setString(2, payload.getEmail())
        prpStmnt.setString(3, payload.get("name").toString())
        prpStmnt.setString(4, payload.get("family_name").toString())
        prpStmnt.setString(5, payload.get("given_name").toString())
        prpStmnt.setBoolean(
          6,
          payload.get("email_verified").toString().toBoolean
        )
        prpStmnt.setString(
          7,
          if (payload.get("locale") == null) { "" }
          else { payload.get("locale").toString }
        )

        prpStmnt.executeUpdate()
        println("sucessfully added new user")
        prpStmnt.close()

        // creating session id and stuff
        val ssn = """
        INSERT INTO sessions 
        (userid, sessionid, created, expires) 
        VALUES (?, ?, ?, ?)
        """
        val ssnStmnt = connection.prepareStatement(ssn)

        ssnStmnt.setString(1, payload.getSubject())
        ssnStmnt.setObject(2, sessionid)
        ssnStmnt.setTimestamp(3, new Timestamp(System.currentTimeMillis))
        // 1 hr expiry
        ssnStmnt.setTimestamp(
          4,
          new Timestamp(System.currentTimeMillis + 3600000)
        )

        val resulting = ssnStmnt.executeUpdate()
        println(resulting)
        // }

        println("sucessfully added new usr to sessions")
        ssnStmnt.close()
      }

      sessionid
    }(databaseExecutionContext)
  }

  def generateSessionId(connection: Connection): UUID = {
    var testRes = true
    val testStmnt = connection.prepareStatement("""
        SELECT * FROM sessions WHERE sessionid = ?
        """)
    var sessionid = UUID.randomUUID()
    while (testRes) {
      sessionid = UUID.randomUUID()
      testStmnt.setObject(1, sessionid)
      testRes = testStmnt.executeQuery().next()
    }
    testStmnt.close()

    sessionid
  }

}
