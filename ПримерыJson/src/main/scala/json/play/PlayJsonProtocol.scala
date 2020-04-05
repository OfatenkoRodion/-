package json.play

import json.Sex.Sex
import json.{Sex, Student}
import play.api.libs.json._

object PlayJsonProtocol {

  implicit val StudentWrites: Writes[Student] = new Writes[Student] {
    override def writes(student: Student): JsObject = Json.obj(
      "name" -> student.name,
      "lastName" -> student.lastName,
      "middleName" -> student.middleName, //write null if middleName empty
      "age" -> student.age,
      "sex" -> student.sex,
    )
  }

  implicit val StudentReads: Reads[Student] = new Reads[Student] {
    override def reads(json: JsValue): JsResult[Student] = for {
      name <- (json \ "name").validate[String]
      lastName <- (json \ "lastName").validate[String]
      middleName <- (json \ "middleName").validateOpt[String]
      age <- (json \ "age").validate[Int]
      sex <- (json \ "sex").validate[Sex]
    } yield Student(name, lastName, middleName, age, sex)
  }

  implicit val format: Format[json.Sex.Value] = Json.formatEnum(Sex)
  implicit val StudentFormat: Format[Student] = Format(StudentReads, StudentWrites)

}

//  import json.play.PlayJsonProtocol.StudentFormat
//  import play.api.libs.json.Json
// Json.toJson(Student("Bob", "Hot", None, 32, Sex.male))

//  val nameResult: JsResult[Student] = s.validate[Student]
//  nameResult match {
//    case JsSuccess(s, _) => println(s"Student: $s")
//    case e: JsError => println(s"Errors: ${JsError.toJson(e)}")
//  }
