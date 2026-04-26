package database

import javax.inject.Inject
import scala.concurrent.Future
import play.api.db.Database
import java.sql.Connection
import java.sql.Timestamp
import database.RedisExecutionContext
import play.api.Configuration

import io.lettuce.core._;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

class LettuceConnection @Inject() (
    db: Database,
    databaseExecutionContext: RedisExecutionContext,
    config: Configuration
) {
  private val redisPass = config.get[String]("redis.default.password")

  private val uri: RedisURI = RedisURI.Builder
    // .redis("99.240.208.73", 6379)
    .redis("10.0.0.108", 6379)
    .withPassword(redisPass)
    .build();

  private val client: RedisClient = RedisClient.create(uri);

  def ping() = {
    Future {
      val connection = client.connect();
      val commands = connection.sync();
      println(commands.ping())
      connection.close()
    }(databaseExecutionContext)
  }

  def newCampaign(campaignId: String) = {

  }

  // TODO: FIGURE OUT WHICH STUF I WANT TO KEEP OPEN AND CLOSE, BUT CLIENT SHOULD BE CLOSED AT SOME POINT

  // probably have a client open when a campaign(or maybe just a single websocket) opens, and close the client when the campaign/websocket is done
  // client.close()

}
