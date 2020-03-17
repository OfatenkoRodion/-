package patterns

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior, SupervisorStrategy}

object SupervisionTyped extends App {
  val system = ActorSystem(Counter(), "system")

  system ! Counter.Increment("")
  system ! Counter.Increment("fbdfnb")

  object Counter {

    case class Increment(str: String)

    def apply(): Behavior[Increment] =
      Behaviors.supervise[Increment] {
        Behaviors.supervise[Increment] {
          Behaviors.receive { (context, message) =>
            message match {
              case Increment("") => context.log.warn("NULL")
                throw new NullPointerException

              case Increment(str) if str.length > 20 =>
                context.log.warn("LONG")
                throw new IllegalArgumentException

              case Increment(str) =>
                val length = str.split("").length
                context.log.info("length == {}", length)
                Behaviors.same

            }

          }
        }.onFailure[NullPointerException](SupervisorStrategy.restart)
      }.onFailure[IllegalArgumentException](SupervisorStrategy.restart)
  }

}