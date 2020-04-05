package json.tethys

import enumeratum.{Enum, EnumEntry}
import tethys.enumeratum.{TethysEnum, TethysKeyEnum}

// enumeratum module for tethys
sealed trait SexType extends EnumEntry

case object SexType extends Enum[SexType]
  with TethysEnum[SexType] // provides JsonReader and JsonWriter instances
  with TethysKeyEnum[SexType] {

  case object Female extends SexType
  case object Male extends SexType

  val values: IndexedSeq[SexType] = findValues
}

