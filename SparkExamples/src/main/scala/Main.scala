object ImplicitExample extends App {


  def s(r: Int, pi: Double): Double = {
    pi * (r * r)
  }

  println(s(5, 3.14))


  // каррирование - 2 или более групп скобочек
  def sWithCarring(r: Int)(implicit pi: Double): Double = {
    pi * (r * r)
  }

  implicit val PI: Double = 3.14


  //Ctrl + Alt + Shift + “+”    показать как идея подставляет имплиситы
  //Ctrl + Alt + Shift + “-”

  sWithCarring(5)
  sWithCarring(4)
  sWithCarring(6)
  sWithCarring(67)

}