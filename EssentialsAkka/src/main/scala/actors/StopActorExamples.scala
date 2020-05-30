package actors

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Kill, PoisonPill, Props, Terminated}

object StopActorExamples {

  val system = ActorSystem("system")

  val childName = "child1"
  val someActor = system.actorOf(Props[SomeActor])

  someActor ! SomeActor.CreateChild(childName)
  someActor ! SomeActor.ContextStopChild(childName)
  someActor ! SomeActor.ContextStop(childName)

  /**
    * PoisonPill немедленно убьет актор
  **/
  someActor ! PoisonPill
  /**
    * Kill заставит актор выстрелить ошибку, что обьет его и пошлет событие на супервизор
  **/
  someActor ! Kill

}

object SomeActor {

  case class CreateChild(name: String)
  case object ContextStop
  case class ContextStopChild(name: String)

}

class SomeActor extends Actor with ActorLogging {

  import SomeActor._

  override def receive: Receive = withChildren(Map.empty)

  def withChildren(children: Map[String, ActorRef]): Receive = {
    case CreateChild(name) =>

      val child = context.actorOf(Props[SomeActorChild], name)
      context.watch(child)
      context.become(withChildren(children + (name -> child)))

    /**
      *  context.stop(self) убиваем самого себя, но не моментально. Еще парочка сообщений долетит. Так же убивает всех потомков
    **/
    case ContextStop => context.stop(self)

    /**
      *  context.stop(ref) убиваем потомка, но не моментально. Еще парочка сообщений долетит. Так же убивает всех потомков потомка
    **/
    case ContextStopChild(name) =>
      children.get(name).foreach(ref => context.stop(ref))
      context.become(withChildren(children - name))

    case Terminated(ref) =>
    /**
      *  заставляем родителя смотреть за потомком при помощи context.watch(child) и когда тот умрет, то получим это сообщение
    **/
  }

}

class SomeActorChild extends Actor with ActorLogging {
  override def receive: Receive = {
    case msg => log.info(s"[SomeActorChild] - $msg")
  }
}