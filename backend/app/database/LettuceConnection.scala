//abstraction layer to interface with the redis cache/db
// maybe remove this layer and implement lettuce directly into the live campaign service? We don't actually call lc in the controller

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

    override def message(pattern: String, channel: String, message: String) = {
      // correct override for event pub/sub
      println("recieved msg " + pattern, channel, message)
    }

    override def subscribed(channel: String, count: Long): Unit = {}

    override def psubscribed(channel: String, count: Long): Unit = {}

    override def unsubscribed(channel: String, count: Long): Unit = {}

    override def punsubscribed(channel: String, count: Long): Unit = {}

  })

  println("added listener")
  psCommands.psubscribe("lobbies:*:events")
  println("subscribed")

  def ping() = {
    Future {
      println(commands.ping())
    }(redisExecutionContext)
  }

  // gets the owner of the specified campaign
  def getOwner(cid: String): Future[String] = {
    Future {
      commands.get(s"lobbies:${cid}:owner")
    }(redisExecutionContext)
  }

  def removePlayer(cid: String, uid: String) = {
    Future {
      commands.lrem(s"lobbies:${cid}:players", 0, uid)
      commands.publish(s"lobbies:${cid}:events", "player-removed")
    }(redisExecutionContext)
  }

  def removeCampaign(cid: String) = {
    Future {
      commands.del(s"lobbies:${cid}:players")
      commands.publish(s"lobbies:${cid}:events", "campaign-removed")
    }(redisExecutionContext)
  }

  def addPlayer(cid: String, uid: String) = {
    Future {
      commands.rpush(s"lobbies:${cid}:players", uid)
      commands.publish(s"lobbies:${cid}:events", "player-added")
    }(redisExecutionContext)
  }

  // sets the owner of the specified campaign
  def setOwner(cid: UUID, uid: String) = {
    Future {

      // setting the creator to be the user's id
      commands.set(s"lobbies:${cid}:owner", uid)

      // publishing change
      commands.publish(s"lobbies:${cid}:events", "owner-updated")
      println("the owner is " + commands.get(s"lobbies:${cid}:owner"))

    }(redisExecutionContext)
  }

}
