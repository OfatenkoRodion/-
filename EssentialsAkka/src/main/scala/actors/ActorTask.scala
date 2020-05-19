package actors

import actors.ActorTask.WordCounterMaster.{Initialize, WordCounterReply, WordCounterTask}
import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object ActorTask extends App {

  object WordCounterMaster {
    case class Initialize(nChildren: Int)
    case class WordCounterTask(id: Int, text: String)
    case class WordCounterReply(id: Int, count: Int)
  }
  class WordCounterMaster extends Actor {
    import WordCounterMaster._

    override def receive: Receive = {
      case Initialize(nChildren) =>
        val childrenRefs = for (i <- 1 to nChildren) yield context.actorOf(Props[WordCounterWorker], s"wcw_$i")
        context.become(withChildren(childrenRefs, 0, 0, Map()))
    }

    def withChildren(childrenRefs: Seq[ActorRef], currentChildIndex: Int, currentTaskId: Int, requestMap: Map[Int, ActorRef]): Receive = {
      case text: String =>
        val originalSender = sender()
        val task = WordCounterTask(currentTaskId, text)
        val childRef = childrenRefs(currentChildIndex)
        childRef ! task
        val nextChildIndex = (currentChildIndex + 1) % childrenRefs.length
        val newTaskId = currentChildIndex + 1
        val newRequestMap = requestMap + (currentTaskId -> originalSender)
        context.become(withChildren(childrenRefs, nextChildIndex, currentTaskId, newRequestMap))
      case WordCounterReply(id, count) =>
        val originalSender = requestMap(id)
        //originalSender ! count
        println(s"Count: $count")
        context.become(withChildren(childrenRefs, currentChildIndex, currentTaskId, requestMap - id))
    }
  }

  class WordCounterWorker extends Actor {
    override def receive: Receive = {
      case WordCounterTask(id, text) => sender() ! WordCounterReply(id, text.split(" ").length)
    }
  }

  val system = ActorSystem("system")
  val master = system.actorOf(Props[WordCounterMaster])
  master ! Initialize(5)

  master ! "Hello actor system"



}
