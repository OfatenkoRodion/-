import java.awt.Color


sealed trait Gene{
  var eye: Array[Byte] = Array.fill(20)((scala.util.Random.nextInt(256) - 128).toByte)
}

sealed trait Animal{
  val uuid: String
  var x:Int
  var y: Int
  var hp: Int
  val color: Color
}

case class AnimalA(var x: Int, var y: Int, var hp: Int, color: Color = Color.red, uuid: String =  java.util.UUID.randomUUID.toString) extends Animal with Gene
case class AnimalB(var x: Int, var y: Int, var hp: Int, color: Color = Color.blue, uuid: String =  java.util.UUID.randomUUID.toString, var isReadyForSupport: Boolean = true) extends Animal with Gene
case class AnimalC(var x: Int, var y: Int, var hp: Int, color: Color = Color.yellow, uuid: String =  java.util.UUID.randomUUID.toString, var isReadyForSupport: Boolean = true) extends Animal with Gene
case class Resource(var x: Int, var y: Int ,var hp: Int = 500, color: Color = Color.green, uuid: String = java.util.UUID.randomUUID.toString) extends Animal


