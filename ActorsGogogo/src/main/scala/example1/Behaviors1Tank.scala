package example1

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior, scaladsl}

object Behaviors1Tank extends App {

  val system: ActorSystem[BeginWar] = ActorSystem(MyGeneral(), "war")
  system ! new BeginWar

}

class BeginWar

object MyGeneral {
  def apply(): Behavior[BeginWar] = Behaviors.setup { context =>
    context.log.info("Fuck, its war! I need some technique")

    val friend = context.spawn(TankFactoryOwner(), "owner")

    friend ! BuildTankOrder(5)
    Behaviors.same
  }
}


case class BuildTankOrder(tanksCount: Int)

object TankFactoryOwner {

  def apply(): Behavior[BuildTankOrder] = Behaviors.receive { (context, order) =>
    context.log.info("Got order to build {} tanks!", order.tanksCount)
    val tankFactory = context.spawn(TankFactory(), "owner")
    tankFactory ! order
    Behaviors.stopped
  }
}


object TankFactory {

  def apply(): Behavior[BuildTankOrder] = Behaviors.receive { (context, order) =>
    track(order.tanksCount, context)
  }

  private def track(tanksCount: Int, context: scaladsl.ActorContext[BuildTankOrder]): Behavior[BuildTankOrder] =
    if (tanksCount != 0) {
      context.log.info("Tank is ready")
      track(tanksCount - 1, context)
      Behaviors.same
    } else {
      Behaviors.stopped
    }

}