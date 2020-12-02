package cats

object ContraFunctor extends App {

  trait Printable[A] {
    self: Printable[A] =>

    def contramap[B](func: B => A): Printable[B] =
      new Printable[B] {
        override def format(value: B): String =
          self.format(func(value))
      }

    def format(value: A): String
  }

  implicit val stringPrintable: Printable[String] =
    new Printable[String] {
      override def format(value: String): String = "\"" + value + "\""
    }

  implicit def optionPrintable[A](implicit pr: Printable[A]): Printable[Option[A]] =
    new Printable[Option[A]] {
      override def format(value: Option[A]): String =
        pr.format(value.get)
    }

  implicit def optionPrintable2[A](implicit pr: Printable[A]): Printable[Option[A]] =
    pr.contramap[Option[A]](v => v.get)


  def printFormatted[A](value: A)(implicit p: Printable[A]) = println(p.format(value))

  printFormatted[String]("hello world")
  printFormatted[Option[String]](Option("hello world"))(optionPrintable(stringPrintable))

}