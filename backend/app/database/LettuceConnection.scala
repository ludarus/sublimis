//abstraction layer to interface with the redis cache/db

package database

import javax.inject.Inject
import scala.concurrent.Future
import play.api.db.Database
import java.sql.Connection
import java.util.UUID
import java.sql.Timestamp
import database.RedisExecutionContext
import play.api.Configuration
import javax.inject.Singleton

import io.lettuce.core._;
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection
import io.lettuce.core.pubsub.RedisPubSubListener
import io.lettuce.core.api.sync.RedisCommands;

@Singleton
class LettuceConnection @Inject() (
    redisExecutionContext: RedisExecutionContext,
    client: RedisClient,
    stdConnection: StatefulRedisConnection[String, String],
    psConnection: StatefulRedisPubSubConnection[String, String],
    config: Configuration
) {

  // val commands = stdConnection.async()
  val commands = stdConnection.sync()
  val psCommands = psConnection.sync()

  // adding a listener for the pub sub events
  psConnection.addListener(new RedisPubSubListener[String, String]() {
    override def message(channel: String, message: String): Unit = {
      println("recieved messgaeA")
    }

    override def message(pattern: String, channel: String, message: String) {
      //correct override for event pub/sub
      println("recieved msg " + pattern, channel, message)
    }

    override def subscribed(channel: String, count: Long) {}

    override def psubscribed(channel: String, count: Long) {}

    override def unsubscribed(channel: String, count: Long) {}

    override def punsubscribed(channel: String, count: Long) {}

  })

  println("added listener")
  psCommands.psubscribe("lobbies:*:events")
  println("subscribed")

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

      // publishing change
      commands.publish(s"lobbies:${cid}:events", "owner-updated")
      println("the owner is " + commands.get(s"lobbies:${cid}:owner"))

      cid
    }(redisExecutionContext)
  }

}
