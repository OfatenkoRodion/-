package patterns

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

object DinoFireAndForget extends App {

  val systemWithTRexGuard = ActorSystem(`T-Rex`.giveBirth, "fire-and-forget-sample")

  systemWithTRexGuard ! EatThis("Apple")
  systemWithTRexGuard ! EatThis("Akka")
  systemWithTRexGuard ! EatThis("Fat human")

  case class EatThis(food: String)

  object `T-Rex` {
    def giveBirth: Behavior[EatThis] =
      Behaviors.receive {
        case (context, EatThis(food)) =>
          context.log.info("T-Rex ate this: {}", food)
          Behaviors.same
      }
  }

}
