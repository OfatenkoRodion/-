package actors
import actors.ChangeBehaviorsActor.Mom.MomStart
import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object ChangeBehaviorsActor extends App {

  object FussyKid {
    case object KidAccept
    case object KidReject
    val HAPPY = "happy"
    val SAD = "sad"
  }
  class FussyKid extends Actor {
    import FussyKid._
    import Mom._

    var state = HAPPY
    override def receive: Receive = {
      case Food(VEGETABLE) => state = SAD
      case Food(CHOCOLATE) => state = HAPPY
      case Ask =>
        if (state == HAPPY) sender ! KidAccept
        else sender ! KidReject
    }
  }

  class StatelessFussyKid extends Actor {
    import FussyKid._
    import Mom._

    override def receive: Receive = happyReceive

    def happyReceive: Receive = {
      case Food(VEGETABLE) => context.become(sadReceive)
      case Food(CHOCOLATE) =>
      case Ask => sender ! KidAccept
    }

    def sadReceive: Receive = {
      case Food(VEGETABLE) =>
      case Food(CHOCOLATE) =>context.become(happyReceive)
      case Ask => sender ! KidReject
    }
  }


  object Mom {
    case class MomStart(kidRef: ActorRef)
    case class Food(food: String)
    case object Ask
    val VEGETABLE = "vegetable"
    val CHOCOLATE = "chocolate"
  }
  class Mom extends Actor {
    import Mom._
    import FussyKid._

    override def receive: Receive = {
      case MomStart(kidRef) =>
        kidRef ! Food(VEGETABLE)
        kidRef ! Ask
      case KidAccept => println("kidAccept")
      case KidReject => println("KidReject")
    }
  }

  val system =  ActorSystem("system")
  val fussyKid = system.actorOf(Props[FussyKid])
  val statelessFussyKid = system.actorOf(Props[StatelessFussyKid])
  val mom = system.actorOf(Props[Mom])

  //mom ! MomStart(fussyKid)
  mom ! MomStart(statelessFussyKid)

}
