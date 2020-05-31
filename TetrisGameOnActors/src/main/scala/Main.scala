import actors.GreatRandomActor
import akka.actor.{ActorSystem, Props}

object Main extends App {

  val system = ActorSystem("system")

  val actor = system.actorOf(Props[GreatRandomActor])

  actor ! GreatRandomActor.NextShape
}
