package json

import json.Sex.Sex

case class Student(name: String, lastName: String, middleName: Option[String], age: Int, sex: Sex)
