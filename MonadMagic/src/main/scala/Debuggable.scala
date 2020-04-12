object Main extends App {

  def f(a: Int): Debuggable = {
    val result = a * 2
    val message = s"f: a($a) * 2 = $result"
    Debuggable(result, message)
  }

  def g(a: Int): Debuggable = {
    val result = a * 3
    val message = s"g: a($a) * 3 = $result"
    Debuggable(result, message)
  }

  def h(a: Int): Debuggable = {
    val result = a * 4
    val message = s"h: a($a) * 4 = $result"
    Debuggable(result, message)
  }


  val finalResult = for {
    fResult <- f(100)
    fResult <- g(fResult)
    hResult <- h(fResult)
  } yield hResult


  println(s"value: ${finalResult.value}")
  println(s"message: ${finalResult.message}")
}

case class Debuggable(value: Int, message: String) {

  def map(f: Int => Int): Debuggable = Debuggable(f(value), message)

  def flatMap(f: Int  => Debuggable): Debuggable = {
    val newValue: Debuggable = f(value)
    Debuggable(newValue.value, message + "\n" + newValue.message)
  }
}