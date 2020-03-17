package patterns

import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import akka.util.Timeout
import java.util.concurrent.TimeUnit.SECONDS

import example1.HelloWorld.Greet
import patterns.Dave.{Command, Start}

import scala.concurrent.duration._
import scala.util.{Failure, Success}

object DinoRequestResponseAsk extends App{

 val dinoKitchen: ActorSystem[Command] = ActorSystem(Dave(), "DinoKitchen")

  dinoKitchen ! Start
}

object Dave {

  sealed trait Command
  case object Start extends Command
  case class BatiaTebeNormalno(replyTo: ActorRef[Command]) extends Command
  case class Yes(message: String) extends Command
  case class Response(message: String) extends Command

  def apply(): Behavior[Command] =
    Behaviors.setup[Command] { context =>

      implicit val timeout: Timeout = 3.seconds

      Behaviors.receiveMessage {
        case Start =>
          context.ask(context.self, BatiaTebeNormalno) {
            case Success(Response(message)) => Yes(message)
            case Failure(_)                 => Yes("Request failed")
          }
          Behaviors.same
        case Yes(message) =>
          context.log.info("Yes", message)
          Behaviors.same
        case BatiaTebeNormalno(replyTo) =>
          replyTo ! Response("BatiaTebeNormalno")
          Behaviors.same
      }
    }
}

