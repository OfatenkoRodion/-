package json

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder, Json}
import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor, Json}
import json.Sex.Sex
import json.{Sex, Student}

import io.circe.generic.auto._, io.circe.syntax._ /// лютая дичь, автоматически сделаент декодер и енкодер

object Circe {

  implicit val encodeStudent: Encoder[Student] = new Encoder[Student] {
    override final def apply(a: Student): Json = Json.obj(
      ("name", Json.fromString(a.name)),
      ("lastName", Json.fromString(a.lastName)),
      ("middleName", Json.fromString(a.middleName.getOrElse("null"))),
      ("age", Json.fromInt(a.age)),
      ("sex", Json.fromString(a.sex.toString)),
    )
  }

  implicit val decodeStudent: Decoder[Student] = new Decoder[Student] {
    override def apply(c: HCursor): Result[Student] = for {
      name <- c.downField("name").as[String]
      lastName <- c.downField("lastName").as[String]
      middleName <- c.downField("middleName").as[Option[String]]
      age <- c.downField("age").as[Int]
      sex <- c.downField("sex").as[Sex]
    } yield
      Student(name, lastName, middleName, age, sex)
  }

  implicit val genderDecoder: Decoder[Sex] = Decoder.decodeEnumeration(Sex)

  //implicit val decoderStudent: Decoder[Student] = deriveDecoder[Student]
  // implicit val encodeStudent2: Encoder[Student] = deriveEncoder

}

//  import Circe._
// Student("Bob", "Hot", None, 32, Sex.male).asJson
//  val json =
//    """
//      |{
//      |  "name" : "Bob",
//      |  "lastName" : "Hot",
//      |  "middleName" : "MID",
//      |  "age" : 32,
//      |  "sex" : "male"
//      |}
//    """.stripMargin
//decode[Student](json)
