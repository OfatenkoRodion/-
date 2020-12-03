package cats

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._
import io.circe.{Decoder, Encoder, Json}

object InvariantFunctorRealExample extends App {

  trait CodecFunctor[A] {

    def encode(value: A): Json
    def decode(value: Json): A

  }

  case class Employee(id: Int, name: String)
  case class EmployeeDTO(id: Int, name: String)

  val codecFunctorEmployee = new CodecFunctor[Employee]{
    override def encode(value: Employee): Json = {
      val encoderEmployee: Encoder[Employee] = deriveEncoder[Employee]
      encoderEmployee(value).asJson
    }

    override def decode(value: Json): Employee = {
      val decoderEmployee: Decoder[Employee] = deriveDecoder[Employee]
      decoderEmployee.decodeJson(value).toOption.get
    }
  }

  println(codecFunctorEmployee.encode(Employee(500, "John")))


}