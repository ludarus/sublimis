import com.google.inject.AbstractModule

import database.PostgresExecutionContext
import database.RedisExecutionContext

//this binds custom classes in the dependency injection
class Module extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[PostgresExecutionContext]).asEagerSingleton()
    bind(classOf[RedisExecutionContext]).asEagerSingleton()
  }
}
