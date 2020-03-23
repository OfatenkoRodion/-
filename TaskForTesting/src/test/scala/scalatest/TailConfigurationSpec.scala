package scalatest

import org.scalatest.Suite
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


class TailConfigurationSpec extends AnyFlatSpec with Matchers with Suite{

  "tail" should "возвращать дефолтное значение" in new TestWiring {
    val testdefaultTail = Tail("Value1", "Value2", 777)

    tailConfiguration.tail shouldBe testdefaultTail
  }

  private class TestWiring {
    val tailConfiguration = new TailConfigurationImpl()
  }

}
