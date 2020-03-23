package scalatest

import scala.concurrent.{ExecutionContext, Future}

trait Dino {
  this: {
    def name: String
  } =>

  def nameUpper(): String = name.trim.toUpperCase

  def copeName(count: Int): Future[String]

  def motherName(): Future[Option[String]]

  def showMeYourTail(): Future[Tail]

  def sumThis(one: Int, two: Int, three: Int, four: Int): Int

  def sumThis(one: Int)(two: Int)(three: Int): Int

  def sumThis(one: Int)(two: Int)(three: Int)(four: Int)(implicit five: Int): Int
}

class DinoImpl(val name: String,
               dinoMother: Option[Dino],
               tailConfiguration: TailConfiguration)
              (implicit ec: ExecutionContext) extends Dino {

  override def copeName(count: Int): Future[String] = {
    if (count > 0) {
      Future.successful(1.to(count).map(_ => name).mkString(","))
    } else Future.failed(new CountLessThenZiroException("Чет меньше 0. Аяяй!"))
  }

  override def motherName(): Future[Option[String]] = Future(dinoMother.map(_.nameUpper()))

  override def showMeYourTail(): Future[Tail] = Future.successful(tailConfiguration.tail)

  override def sumThis(one: Int, two: Int, three: Int, four: Int): Int = one + two + three + four

  override def sumThis(one: Int)(two: Int)(three: Int): Int = one + two + three

  override def sumThis(one: Int)(two: Int)(three: Int)(four: Int)(implicit five: Int): Int = one + two + three + four + five
}
