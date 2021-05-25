package model

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class ConfigIdDTO(configId: Int) extends AnyVal

object ConfigIdDTO {
  implicit val configIdDTODecoder: Decoder[ConfigIdDTO] = deriveDecoder
  implicit val configIdDTOEncoder: Encoder[ConfigIdDTO] = deriveEncoder
}