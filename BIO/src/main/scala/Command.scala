sealed trait Command

case object MakeMove extends Command
case object Redraw extends Command
case object Kill  extends Command
case object FriendSupport  extends Command
case object CreateResource  extends Command


case object GetReadyForFriendSupport  extends Command
case class LookAround(animal: Animal)  extends Command
case class LookAroundAnswer(resource: Option[Animal])  extends Command
case class TryToEatResource(resource: Animal)  extends Command
case class EatThis(resource: Option[Animal])  extends Command
case class TryToGetSupport(friend: Animal)  extends Command
case class KillRequest(animal: Animal)  extends Command
case class CreateAnimalRequest(parent: Animal)  extends Command
