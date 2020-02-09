package json

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder, Json}

import io.circe.generic.auto._, io.circe.syntax._ /// лютая дичь, автоматически сделаент декодер и енкодер

object Circe {

  implicit val encodeStudent: Encoder[Student] = new Encoder[Student] {
    final def apply(a: Student): Json = Json.obj(
      ("name", Json.fromString(a.name)),
      ("lastName", Json.fromString(a.lastName)),
      ("middleName", Json.fromString(a.middleName.orNull)),
      ("age", Json.fromInt(a.age)),
      ("sex", Json.fromString(a.sex.toString)),
    ).dropNullValues
  }

  implicit val decoderStudent: Decoder[Student] = deriveDecoder[Student]
  implicit val encodeStudent2: Encoder[Student] = deriveEncoder

}
