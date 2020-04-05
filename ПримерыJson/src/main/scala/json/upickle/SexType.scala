package json.upickle

// new enum for Upickle
sealed abstract class SexType(val value: String)

object SexType {

  case object Male extends SexType("Predator")

  case object Female extends SexType("Herbivorous")

}
