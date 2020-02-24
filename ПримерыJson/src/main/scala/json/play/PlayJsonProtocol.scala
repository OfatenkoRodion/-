package json.play

import json.{Sex, Student}
import play.api.libs.json._

object PlayJsonProtocol {

  implicit val StudentWrites = new Writes[Student] {
    def writes(student: Student) = Json.obj(
      "name"  -> student.name,
      "lastName"  -> student.lastName,
      "middleName"  -> student.middleName, //write null if middleName empty
      "age"  -> student.age,
      "sex"  -> student.sex,
    )
  }


  implicit val format = Json.formatEnum(Sex)
  implicit val StudentFormat = Json.format[Student]

}

//import json.play.PlayJsonProtocol.StudentFormat
//import play.api.libs.json.Json
//print(Json.toJson(Student("Bob", "Hot", None, 32, Sex.male)))