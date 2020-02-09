package json.spray

import json.{Sex, Student}
import spray.json.{DefaultJsonProtocol, DeserializationException, JsString, JsValue, RootJsonFormat}


object SprayJsonProtocol extends DefaultJsonProtocol {

  implicit def enumFormat[T <: Enumeration](implicit enu: T): RootJsonFormat[T#Value] =
    new RootJsonFormat[T#Value] {
      def write(obj: T#Value): JsValue = JsString(obj.toString)
      def read(json: JsValue): T#Value = {
        json match {
          case JsString(txt) => enu.withName(txt)
          case somethingElse => throw DeserializationException(s"Expected a value from enum $enu instead of $somethingElse")
        }
      }
    }

  implicit val sexFormat = enumFormat(Sex)
  implicit val studentFormat = jsonFormat5(Student)
}

//import json.spray.SprayJsonProtocol._
//import spray.json._
//Student("Bob", "Hot", None, 32, Sex.male).toJson
