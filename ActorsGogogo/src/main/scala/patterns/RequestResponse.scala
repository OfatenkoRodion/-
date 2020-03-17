package patterns

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.actor.typed.scaladsl.Behaviors

object RequestResponse extends App {

  sealed trait Command
  case object Start extends Command
  case class Request(query: String, replyTo: ActorRef[Response]) extends Command
  case class Response(result: String) extends Command

  val cookieAsker = CookieAsker()
  val system: ActorSystem[Command] = ActorSystem(cookieAsker, "CookieAsker")

  system ! Start

  object CookieAsker {
    def apply() = Behaviors.receivePartial[Command] {
      case (context, Response(result)) =>
        context.log.info(result)
        Behaviors.same
      case (context, Start) =>
        val cookieFabric = context.spawn(CookieFabric(), "CookieFabric")
        cookieFabric ! Request("give me cookies", context.self)
        Behaviors.same
    }

  }

  object CookieFabric {
    def apply(): Behaviors.Receive[Request] =
      Behaviors.receiveMessage[Request] {
        case Request(query, replyTo) =>
          replyTo ! Response(s"Here are the cookies for [$query]!")
          Behaviors.same
      }
  }
}
