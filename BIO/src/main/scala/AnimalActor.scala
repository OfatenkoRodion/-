import akka.actor.Actor

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class AnimalActor(animal: Animal) extends Actor {

  println("Animal "+ context.self.path.name+ " alive")

  override def receive: Receive = {

    case MakeMove =>
      if (animal.hp < 0) {
        context.parent ! KillRequest(animal)
      } else context.parent ! LookAround(animal)

    case Kill =>
      println("Dead " + animal.uuid)
      context.stop(self)

    case eatTHis: EatThis =>
      eatTHis.resource match {
        case None => None
        case Some(r) => animal.hp = animal.hp + r.hp
          if (animal.hp > 600) {
            animal.hp = animal.hp - GreatRandom.hpDefault * 2
            context.parent ! CreateAnimalRequest(animal)
          }
      }
      oneLifeCircle()

    case lookAroundAnswer: LookAroundAnswer => {
      lookAroundAnswer.resource match {
        case None => moveToRandomPoint
        case Some(r) => moveToResource(r)
      }
    }
    case FriendSupport =>
      animal match {
        case a:AnimalB =>
          a.hp = a.hp * 2
          a.isReadyForSupport = false
          reloadSupportFuture()
          oneLifeCircle()
        case a:AnimalC =>
          a.hp = a.hp * 2
          a.isReadyForSupport = false
          reloadSupportFuture()
          oneLifeCircle()
        case a: Any => {
          println("Hana")
        }
      }
  }
    def moveToRandomPoint = {

      val rnd = new scala.util.Random

      animal.x = animal.x + rnd.nextInt(6) - 3
      animal.y = animal.y + rnd.nextInt(6) - 3
      animal.hp = animal.hp - 1

      if (animal.x < 25) animal.x = 1000 - animal.x
      if (animal.y < 25) animal.y = 1000 - animal.y

      oneLifeCircle()
    }

    def moveToResource(r: Animal)   = {

      val rnd = new scala.util.Random

      animal.hp = animal.hp - 1
      if (r.x > animal.x) {
        animal.x = animal.x + rnd.nextInt(3)
      } else
      if (r.x!= animal.x) animal.x = animal.x - rnd.nextInt(3)

      if (r.y > animal.y) {
        animal.y = animal.y + rnd.nextInt(3)
      } else   if (r.y!= animal.y) animal.y = animal.y - rnd.nextInt(3)

      if (animal.x < 25) animal.x = 1000 - animal.x
      if (animal.y < 25) animal.y = 1000 - animal.y

      if (scala.math.abs(r.x - animal.x) + scala.math.abs(r.y - animal.y)< 20 ){
        animal match {
          case a:AnimalA => context.parent ! TryToEatResource(r)
          case a:AnimalB =>

            r match {
              case r: AnimalC =>
                if (a.isReadyForSupport) {
                  context.parent ! TryToGetSupport(r)
                }
                else oneLifeCircle()
              case r: Resource => context.parent ! TryToEatResource(r)
            }
          case a:AnimalC => r match {
            case r: AnimalB =>
              if (a.isReadyForSupport) {
                context.parent ! TryToGetSupport(r)
              }
              else oneLifeCircle()
            case r: Resource => context.parent ! TryToEatResource(r)
          }
        }
      }  else oneLifeCircle()
    }

  def oneLifeCircle() = {
    context.system.scheduler.scheduleOnce(5000 microseconds) {
      self ! MakeMove
    }
    context.parent ! animal
  }

  def reloadSupportFuture() = {
    context.system.scheduler.scheduleOnce(50000 microseconds) {
      self ! GetReadyForFriendSupport
    }
  }

}
