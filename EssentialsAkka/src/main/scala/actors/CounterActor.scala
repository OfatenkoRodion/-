package actors


import actors.CounterActor.{Decrement, Increment, Print}
import akka.actor.{Actor, ActorSystem, Props}

object CounterActor {
  object Increment
  object Decrement
  object Print
}

object Main extends App {

  class CounterActor extends Actor {
    import CounterActor._
    var sum = 0
    override def receive: Receive = {
      case Increment => sum = sum + 1
      case Decrement => sum = sum - 1
      case Print => println(s"[CounterActor] sum: $sum")
    }
  }

  val actorSystem = ActorSystem("system")

  val counter = actorSystem.actorOf(Props[CounterActor])

  counter ! Increment
  counter ! Decrement
  counter ! Increment

  counter ! Print
}

object Main2 extends App {

  // actor without mutable fields
  class CounterActor extends Actor {
    import CounterActor._
    override def receive: Receive = sumReceive(0)

    def sumReceive(sum: Int): Receive = {
      case Increment => context.become(sumReceive(sum + 1))
      case Decrement => context.become(sumReceive(sum - 1))
      case Print => println(s"[CounterActor] ${self.path} sum: $sum")
      case any_message => println(s"[CounterActor] default: $any_message")
    }
  }

  val actorSystem = ActorSystem("system")

  val counter = actorSystem.actorOf(Props[CounterActor], "counter_name")

  counter ! Increment
  counter ! Decrement
  counter ! Increment

  counter ! Print

  val maybeActot= actorSystem.actorSelection("/user/counter_name")

  maybeActot ! "Found you with selection"
}