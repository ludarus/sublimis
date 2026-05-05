package database

import javax.inject.Inject
import scala.concurrent.Future
import play.api.db.Database
import java.sql.Connection
import java.util.UUID
import java.sql.Timestamp
import database.RedisExecutionContext
import play.api.Configuration

import io.lettuce.core._;
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection
import io.lettuce.core.api.sync.RedisCommands;

class LettuceConnection @Inject() (
    redisExecutionContext: RedisExecutionContext,
    client: RedisClient,
    stdConnection: StatefulRedisConnection[String, String],
    psConnection: StatefulRedisPubSubConnection[String, String],
    config: Configuration
) {

  // val commands = stdConnection.async()
  val commands = stdConnection.sync()

  def ping() = {
    Future {
      println(commands.ping())
    }(redisExecutionContext)
  }

  def newCampaign(uid: String): Future[UUID] = {
    Future {
      // gen new uuid. the chances of one matching an already existing cid are so low im not even going to check for it (even tho i did for the sessionids)
      val cid = UUID.randomUUID()

      // setting the creator to be the user's id
      commands.set(s"lobbies:${cid}:owner", uid)

      println("the owner is " + commands.get(s"lobbies:${cid}:owner"))

      cid
    }(redisExecutionContext)
  }

  // TODO: FIGURE OUT WHICH STUF I WANT TO KEEP OPEN AND CLOSE, BUT CLIENT SHOULD BE CLOSED AT SOME POINT

  // probably have a client open when a campaign(or maybe just a single websocket) opens, and close the client when the campaign/websocket is done
  // client.close()

}
