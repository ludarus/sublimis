package modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import javax.inject.Singleton
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection
import play.api.Configuration
import play.api.inject.ApplicationLifecycle
import scala.concurrent.Future

// make a module for the redis client that i can use in dependency injection
// (to preserve the same redis connection for the whole lifecycle of the application as seen in the Database module for jdbc)
class RedisModule extends AbstractModule {
  @Provides
  @Singleton
  def provideRedisClient(
      config: Configuration,
      lifecycle: ApplicationLifecycle
  ): RedisClient = {
    val uri = RedisURI.Builder
      .redis(config.get[String]("redis.default.ip"), 6379)
      .withAuthentication(
        "default",
        config.get[String]("redis.default.password").toCharArray()
      )
      .build()
    val client = RedisClient.create(uri)

    // kinda like a destructor
    lifecycle.addStopHook(() => Future.successful(client.shutdown()))

    // returning the client
    client
  }

  @Provides
  @Singleton
  def provideStandardRedisConnection(
      client: RedisClient,
      lifecycle: ApplicationLifecycle
  ): StatefulRedisConnection[String, String] = {
    val connection = client.connect()

    lifecycle.addStopHook(() => Future.successful(connection.close()))

    connection
  }

  @Provides
  @Singleton
  def providePubSubRedisConnection(
      client: RedisClient,
      lifecycle: ApplicationLifecycle
  ): StatefulRedisPubSubConnection[String, String] = {
    val connection = client.connectPubSub()

    lifecycle.addStopHook(() => Future.successful(connection.close()))
    connection
  }
}
