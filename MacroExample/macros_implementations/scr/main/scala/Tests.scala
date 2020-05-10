import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros

object Tests {

  def twoParams(word1: String, word2: String): Unit = macro twoParamsImplementation
  def twoParamsImplementation(c: Context)(word1: c.Tree, word2: c.Tree): c.Tree = {
    import c.universe._

    q"""
      println($word1 +" " + $word2)
    """
  }



  def addImplicit(in: Int): Unit = macro addImplicitImplementation
  def addImplicitImplementation(c: Context)(in: c.Tree): c.Tree = {
    import c.universe._



    q"""
    object Temp {
       implicit val in2: Int = 5;
       def sum(in1: Int)(implicit in2: Int) = {

        println("class:" + this.getClass())
        in1 + in2
       }
    }

    implicit val in2: Int = 6

    println("res: " + Temp.sum($in))
    """
  }

}
