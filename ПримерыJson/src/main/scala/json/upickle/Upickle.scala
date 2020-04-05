package json.upickle

import json.upickle.SexType.Male
import upickle.default.{macroRW, ReadWriter => RW}

object Upickle {

  implicit val rw: RW[Student] = macroRW

  implicit val rw1: RW[SexType] = macroRW
}

//  import upickle.default._
//  import Upickle._
// val student = Student("Bob", "Hot", None, 32, Male)
//write(student)
//read[Student]("""{"name":"Bob","lastName":"Hot","middleName":[],"age":32,"sex":{"$type":"json.upickle.SexType.Male"}}""")


