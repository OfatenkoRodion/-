package example2

import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.scaladsl.Behaviors.ReceiveImpl
import akka.actor.typed.{ActorSystem, Behavior, scaladsl}


// Классическое создание по доке
object SimpleCreation extends App {

  // Не более чем фабрика
  object Dinosaur {
    def apply(): Behavior[Msg] = Behaviors.receive { (context, newMsg) =>

      context.log.info("Dinosaur say: {}", newMsg.text)
      Behaviors.same
    }
  }

  class Msg(val text: String)

  // Буквально принимает Behavior, пофиг от куда брать
  val system: ActorSystem[Msg] = ActorSystem(Dinosaur(), "msg")
  system ! new Msg("Roar")
  system.terminate()
}


object CreationWithoutObject extends App {

  val dinosaur = Behaviors.receive[Msg] { (context, newMsg) =>
    context.log.info("Dinosaur say: {}", newMsg.text)
    Behaviors.same
  }

  class Msg(val text: String)

  // Прекрасно стартует с переменной
  val system: ActorSystem[Msg] = ActorSystem(dinosaur, "msg")
  system ! new Msg("Roar")
  system.terminate()
}


object FactoryCreation extends App {

  // Не более чем фабрика
  object Dinosaur {
    // Кстати, попытка создать new ReceiveImpl(onMessage) на прямую - это дно, он приватный :C
    def createBehavior: Behavior[Msg] = Behaviors.receive { (context, newMsg) =>
      context.log.info("Dinosaur say: {}", newMsg.text)
      Behaviors.same
    }
  }

  class Msg(val text: String)

  // Буквально принимает Behavior, пофиг от куда брать
  val system: ActorSystem[Msg] = ActorSystem(Dinosaur.createBehavior, "msg")
  system ! new Msg("Roar")
  system.terminate()
}

object BehaviorsSameFun extends App {

  // Не более чем фабрика
  object Dinosaur {
    def createBehavior: Behavior[Msg] = Behaviors.receive { (context, newMsg) =>
      context.log.info("Dinosaur say: {}", newMsg.text)
      Behaviors.same[MsgType1] // Ну да, вот так нельзя, ОБИДНО
      Behaviors.same[Msg]
    }
  }

  abstract class Msg(val text: String)
  class MsgType1(text: String) extends Msg(text)
  class MsgType2(text: String) extends Msg(text)

  val system: ActorSystem[Msg] = ActorSystem(Dinosaur.createBehavior, "msg")
  system ! new MsgType1("Roar")
  system ! new MsgType2("Roar2")
  system.terminate()
}


object BehaviorsMethods extends App {

  // receivePartial офигенный метод
  val dinosaur = Behaviors.receivePartial[Command] {
    case (context, SayMyName(name)) =>
      context.log.info(s"Hello to $name")
      Behaviors.same
    case (context, UseUnhandled) =>
      context.log.info("UseUnhandled")
      Behaviors.unhandled
    case (context, UseEmpty) =>
      context.log.info("UseEmpty")
      Behaviors.empty
    case (context, UseIgnore) =>
      context.log.info("UseIgnore")
      Behaviors.ignore
  }

  sealed trait Command
  case class SayMyName(name: String) extends Command
  case object UseUnhandled extends Command
  case object UseEmpty extends Command
  case object UseIgnore extends Command


  val system: ActorSystem[Command] = ActorSystem(dinosaur, "msg")
  system ! SayMyName("Kirito") // увидем печать
  system ! SayMyName("Asuna") // увидем печать
  system ! SayMyName("Leafa") // увидем печать
  system ! SayMyName("Sinon") // увидем печать
  system ! UseUnhandled // увидем печать, но сообщение попадет в dead letters. На приемку последующих сообщений не влияет
  system ! SayMyName("Yui") // увидем печать
  system ! SayMyName("Klein") // увидем печать
  system ! UseEmpty // увидем печать, но сообщение попадет в dead letters. Все след. сообщения улетают в dead letters
  system ! SayMyName("Agil") // сообщение улетит в dead letters
  system ! SayMyName("Silica") // сообщение улетит в dead letters
  system.terminate()

  val system2: ActorSystem[Command] = ActorSystem(dinosaur, "msg")
  system2 ! SayMyName("Kirito") // увидем печать
  system2 ! SayMyName("Asuna") // увидем печать
  system2 ! SayMyName("Asuna") // увидем печать
  system2 ! UseIgnore // увидем печать, но все последующие сообщения просто пропадут.
  system ! SayMyName("Yui") // тупо выкинем, не попадут в dead letters
  system ! SayMyName("Klein") // тупо выкинем, не попадут в dead letters
  system2.terminate()

}




