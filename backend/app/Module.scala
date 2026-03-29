import com.google.inject.AbstractModule

import database.DatabaseExecutionContext

//this binds custom classes in the dependency injection
class Module extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[DatabaseExecutionContext]).asEagerSingleton()
  }
}
