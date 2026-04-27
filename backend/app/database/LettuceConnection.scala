package database

import javax.inject.Inject
import scala.concurrent.Future
import play.api.db.Database
import java.sql.Connection
import java.sql.Timestamp
import database.RedisExecutionContext
import play.api.Configuration

import io.lettuce.core._;
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection
import io.lettuce.core.api.sync.RedisCommands;

class LettuceConnection @Inject() (
    redisExecutionContext: RedisExecutionContext,
    rclient: RedisClient,
    pg: ScalaJdbcConnection,
    rstdConnection: StatefulRedisConnection[String, String],
    rpsConnection: StatefulRedisPubSubConnection[String, String],
    config: Configuration
) {

  val commands = rstdConnection.sync()

  def ping() = {
    Future {
      println(commands.ping())
    }(redisExecutionContext)
  }

  def newCampaign(campaignId: String) = {}

  // TODO: FIGURE OUT WHICH STUF I WANT TO KEEP OPEN AND CLOSE, BUT CLIENT SHOULD BE CLOSED AT SOME POINT

  // probably have a client open when a campaign(or maybe just a single websocket) opens, and close the client when the campaign/websocket is done
  // client.close()

}
