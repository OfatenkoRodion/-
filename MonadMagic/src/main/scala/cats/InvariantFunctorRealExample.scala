package cats

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._
import io.circe.{Decoder, Encoder, Json}
import io.scalaland.chimney.dsl.TransformerOps

object InvariantFunctorRealExample extends App {

  trait CodecFunctor[A] {
    self: CodecFunctor[A] =>

    def encode(value: A): Json
    def decode(value: Json): A

    def imap[B](dec: A => B, enc: B => A): CodecFunctor[B] = {
      new CodecFunctor[B] {
        override def encode(value: B): Json =
          self.encode(enc(value))

        override def decode(value: Json): B =
          dec(self.decode(value))
      }
    }
  }

  case class Employee(id: Int, name: String)

  case class EmployeeDTO(id: Int, name: String)

  val codecFunctorEmployee = new CodecFunctor[Employee] {
    override def encode(value: Employee): Json = {
      val encoderEmployee: Encoder[Employee] = deriveEncoder[Employee]
      encoderEmployee(value).asJson
    }

    override def decode(value: Json): Employee = {
      val decoderEmployee: Decoder[Employee] = deriveDecoder[Employee]
      decoderEmployee.decodeJson(value).toOption.get
    }
  }

  val codecFunctorEmployeeDTO = codecFunctorEmployee.imap[EmployeeDTO](_.transformInto[EmployeeDTO], _.transformInto[Employee])

  println(codecFunctorEmployeeDTO.encode(EmployeeDTO(500, "John")))

}