package actors

import akka.actor.{Actor, ActorSystem, Props}

object ActorIntro extends App {

  val actorSystem = ActorSystem("acSystem")
  println(actorSystem.name)


  // using companion object is best practise for actor with params
  object WordCounterActor {
    def props(init: Int) = Props(new WordCounterActor(init))
  }

  class WordCounterActor(initState: Int) extends Actor {
    var totalWords = initState

    def receive: PartialFunction[Any, Unit] = {
      case message: String =>
        println(s"[word counter] I have received: $message")
        totalWords += message.split(" ").length
      case msg => println(s"[word counter] I cannot understand ${msg.toString}")
    }
  }

  //val wordCounter = actorSystem.actorOf(Props[WordCounterActor]) is was no params
  val wordCounter = actorSystem.actorOf(WordCounterActor.props(0))

  wordCounter ! "Hello world!"

}
