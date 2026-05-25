package custom

import org.apache.pekko.actor._

//wrapper class around message 
class Message(
    val payload: String,
    val origin: ActorRef
) {}
