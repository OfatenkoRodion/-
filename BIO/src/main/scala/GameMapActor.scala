import akka.actor.{Actor, Props, Terminated}
import javax.swing.{JFrame, WindowConstants}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class GameMapActor extends Actor {

  val frame = new JFrame()
  frame.setSize(1000,1000)
  frame.setResizable(false)

  private var animals: Map[String, Animal] =
    GreatRandom.rndScenario1.map{v =>
      context.actorOf(Props(new AnimalActor(v)),v.uuid) ! MakeMove
      v.uuid -> v
    }.toMap

  private var deadAnimals = Map.empty[String,Animal]

  private var resources: Map[String, Resource] =
    GreatRandom.rndScenario2.map(v => v.uuid -> v).toMap

  override def receive: Receive = {

    case a: Animal =>
      animals -= a.uuid
      animals += a.uuid -> a

    case Redraw =>
      val newMap = new GameMap(resources = resources.values.toList, animals = animals.values.toList, deadAnimals = deadAnimals.values.toList)
      frame.getContentPane().add(newMap)
      frame.setLocationRelativeTo(null)
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
      frame.setVisible(true)

      context.system.scheduler.scheduleOnce(10000 microseconds) {
        self ! Redraw
      }

    case animal:LookAround =>
      animal.animal match {
        case a:AnimalA => {
          val minRes =(resources.values ++ animals.values.filter(v => v match {
            case c:AnimalC => true
            case _ => false
          })).toList.map(r => (r,scala.math.sqrt(scala.math.pow(r.x - animal.animal.x,2) + scala.math.pow(r.y - animal.animal.y,2)))).minBy(_._2)
          if (minRes._2 < 150) {
            sender() ! LookAroundAnswer(Option(minRes._1))
          } else
            sender() ! LookAroundAnswer(None)
        }
        case a:AnimalB => {
          val minRes =(resources.values ++ animals.values.filter(v => v match {
            case c:AnimalC => c.isReadyForSupport
            case _ => false
          })).toList.map(r => (r,scala.math.sqrt(scala.math.pow(r.x - animal.animal.x,2) + scala.math.pow(r.y - animal.animal.y,2)))).minBy(_._2)
          if (minRes._2 < 150) {
            sender() ! LookAroundAnswer(Option(minRes._1))
          } else
            sender() ! LookAroundAnswer(None)
        }

        case a:AnimalC => {
          val minRes = (resources.values ++ animals.values.filter(v => v match {
            case c:AnimalB => c.isReadyForSupport
            case _ => false
          })).toList.map(r => (r,scala.math.sqrt(scala.math.pow(r.x - animal.animal.x,2) + scala.math.pow(r.y - animal.animal.y,2)))).minBy(_._2)
          if (minRes._2 < 150) {
            sender() ! LookAroundAnswer(Option(minRes._1))
          } else
            sender() ! LookAroundAnswer(None)
        }
      }
    case resource: TryToEatResource =>
      resource.resource match {
        case r:Resource =>

          sender ! EatThis(resources.get(r.uuid))
          resources = resources - r.uuid

          context.system.scheduler.scheduleOnce(3 seconds) {
            self ! CreateResource
          }
          context.system.scheduler.scheduleOnce(13 seconds) {
            self ! CreateResource
          }

        case r:AnimalC =>

          sender ! EatThis(resources.get(r.uuid))
          context.actorSelection(context.self.path + "/" + r.uuid) ! Kill
          animals = animals -  r.uuid
      }

    case tryToGetSupport: TryToGetSupport => {
      animals.get(tryToGetSupport.friend.uuid) match {
        case None => sender() ! MakeMove
        case Some(friend) =>
          context.actorSelection(context.self.path + "/" + friend.uuid) ! FriendSupport
          sender() ! FriendSupport
      }
    }

    case killRequest:KillRequest =>
      animals = animals -  killRequest.animal.uuid
      sender() ! Kill
      deadAnimals+= killRequest.animal.uuid -> killRequest.animal

      context.system.scheduler.scheduleOnce(5 seconds) {
      }

    case createRequest: CreateAnimalRequest => {

      createRequest.parent match {
        case a:AnimalA =>

          val newChildAnimal1 = GreatRandom.rndAnimalAWithFixStartPosition(a.x,a.y)
          context.actorOf(Props(new AnimalActor(newChildAnimal1)), newChildAnimal1.uuid) ! MakeMove
          animals+= newChildAnimal1.uuid -> newChildAnimal1

          val newChildAnimal2 = GreatRandom.rndAnimalAWithFixStartPosition(a.x,a.y)
          context.actorOf(Props(new AnimalActor(newChildAnimal2)), newChildAnimal2.uuid) ! MakeMove
          animals+= newChildAnimal2.uuid -> newChildAnimal2

        case a:AnimalB =>

          val newChildAnimal1 = GreatRandom.rndAnimalBWithFixStartPosition(a.x,a.y)
          context.actorOf(Props(new AnimalActor(newChildAnimal1)), newChildAnimal1.uuid) ! MakeMove
          animals+= newChildAnimal1.uuid -> newChildAnimal1

          val newChildAnimal2 = GreatRandom.rndAnimalBWithFixStartPosition(a.x,a.y)
          context.actorOf(Props(new AnimalActor(newChildAnimal2)), newChildAnimal2.uuid) ! MakeMove
          animals+= newChildAnimal2.uuid -> newChildAnimal2

        case a:AnimalC =>

          val newChildAnimal1 = GreatRandom.rndAnimalCWithFixStartPosition(a.x,a.y)
          context.actorOf(Props(new AnimalActor(newChildAnimal1)), newChildAnimal1.uuid) ! MakeMove
          animals+= newChildAnimal1.uuid -> newChildAnimal1

          val newChildAnimal2 = GreatRandom.rndAnimalCWithFixStartPosition(a.x,a.y)
          context.actorOf(Props(new AnimalActor(newChildAnimal2)), newChildAnimal2.uuid) ! MakeMove
          animals+= newChildAnimal2.uuid -> newChildAnimal2
      }
    }
    case CreateResource =>
      if (resources.size < 20 ){
        val newResource = GreatRandom.rndResource
        resources+= newResource.uuid -> newResource
      }
  }

}