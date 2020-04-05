package json.tethys

import tethys.{JsonObjectWriter, JsonReader, JsonWriter}
import scala.reflect.ClassTag

object Tethys {

  def classWriter[A](implicit ct: ClassTag[A]): JsonObjectWriter[A] = {
    JsonWriter.obj[A].addField("Student")(_ => ct.toString())
  }

  implicit val studentWriter: JsonObjectWriter[Student] = {
    classWriter[Student] ++ JsonWriter.obj[Student]
      .addField("name")(_.name)
      .addField("lastName")(_.lastName)
      .addField("middleName")(_.middleName)
      .addField("age")(_.age)
      .addField("sex")(_.sex)
  }

  implicit val studentReader: JsonReader[Student] = JsonReader.builder
    .addField[String]("name")
    .addField[String]("lastName")
    .addField[Option[String]]("middleName")
    .addField[Int]("age")
    .addField[SexType]("sex")
    .buildReader(Student.apply)

}

//  import Tethys._
//  import tethys._
//  import tethys.jackson._
//
//  val student = Student("Bob", "Hot", None, 32, SexType.Male)
//  val json =
//    """
//      |{
//      |  "name" : "Bob",
//      |  "lastName" : "Hot",
//      |  "middleName" : "MID",
//      |  "age" : 32,
//      |  "sex": "Male"
//      |}
//      """.stripMargin
//  println(json.jsonAs[Student])
//  println(student.asJson)

