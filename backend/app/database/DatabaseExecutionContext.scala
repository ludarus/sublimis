package database

import javax.inject.Inject
import org.apache.pekko.actor.ActorSystem
import play.api.libs.concurrent.CustomExecutionContext

// creating custom execution context to make sure db related processes don't block main thread
class DatabaseExecutionContext @Inject() (system: ActorSystem)
    extends CustomExecutionContext(system, "database.dispatcher") {
//do something in here (or maybe not??)
}
