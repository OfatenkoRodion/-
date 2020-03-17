package patterns

import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

object DinoRequestResponse extends App{

  val dinoKitchen: ActorSystem[Human] = ActorSystem(DinoKitchen.open, "DinoKitchen")

  dinoKitchen ! Human(207)
  dinoKitchen.terminate()

  case class Human(bonesLeft: Int)
  case class DinoLoveDinner(human: Human, pair: ActorRef[DinoLoveDinner])

  object DinoKitchen {
    def open: Behavior[Human] = Behaviors.receive { (context, human) =>
      context.log.info("Заказ на поедание человечка")

      val dinoBoy = context.spawn(Dino.giveBirth("John"), "dino1")
      val dino2Girl = context.spawn(Dino.giveBirth("Angel"), "dino2")

      dinoBoy ! DinoLoveDinner(human, dino2Girl)

      Behaviors.same
    }
  }

  object Dino {
    def giveBirth(name: String): Behavior[DinoLoveDinner] =
      Behaviors.receive { (context, dinoDinner) =>

        context.log.info("{}: хрум косточку", name)
        val human = Human(dinoDinner.human.bonesLeft - 1)
        context.log.info("Осталось {} косточек", human.bonesLeft)

        if (human.bonesLeft != 0) {
          dinoDinner.pair ! DinoLoveDinner(human, context.self)
          Behaviors.same
        } else Behaviors.same // но вообще надо бы грохнуть оба актора
      }
  }


}
