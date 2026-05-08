import com.google.inject.AbstractModule

import database.PostgresExecutionContext
import database.RedisExecutionContext
import services.LobbyService

//this binds custom classes in the dependency injection
class Module extends AbstractModule {
  override def configure(): Unit = {
    // eager singleton means the clas will get created on startup, and there can only be one of them.
    bind(classOf[PostgresExecutionContext]).asEagerSingleton()
    bind(classOf[RedisExecutionContext]).asEagerSingleton()
    bind(classOf[LobbyService]).asEagerSingleton()
  }
}
