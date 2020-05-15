import akka.actor.{ActorSystem, Props}

object Main extends App {
  val system = ActorSystem("mySystem")
  val evolutionMaster = system.actorOf(Props[GameMapActor])
  evolutionMaster ! Redraw
}





