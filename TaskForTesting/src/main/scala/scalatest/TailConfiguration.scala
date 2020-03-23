package scalatest

trait TailConfiguration {

  def tail: Tail
}

class TailConfigurationImpl() extends TailConfiguration {

  override def tail: Tail = defaultTail

  private val defaultTail = Tail("Value1", "Value2", 777)
}
