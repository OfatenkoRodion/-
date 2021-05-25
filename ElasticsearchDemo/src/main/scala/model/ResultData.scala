package model

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class ResultData(result: String)

object ResultData {
  implicit val resultDataDecoder: Decoder[ResultData] = deriveDecoder
  implicit val resultDataEncoder: Encoder[ResultData] = deriveEncoder
}