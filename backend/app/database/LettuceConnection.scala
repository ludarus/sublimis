package database

import javax.inject.Inject
import scala.concurrent.Future
import play.api.db.Database
import java.sql.Connection
import java.sql.Timestamp
import database.RedisExecutionContext

import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

class LettuceConnection @Inject() (
    db: Database,
    databaseExecutionContext: RedisExecutionContext
) {
  val uri: RedisURI = RedisURI.Builder
    .redis("localhost", 6379)
    .build();

  val client: RedisClient = RedisClient.create(uri);
  val connection = client.connect();
  val commands = connection.sync();
}
