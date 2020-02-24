import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object J1Tasks1 extends App {

  // Дано здоровье героя HP = 100
  // Герой начинает схватку с противником и получает урон. Урон представлен шквалом ударов и храниться в Seq(5, 15, 25, 30, 7, 40, 45)
  // При получение урона герой теряет жизни. т.е. 100 - 5 = 95; 95 - 15 = 80 ...
  // TODO: Получить HP героя после шквала ударов, если здоровье упало ниже 0 то выдать ошибку с сообщением о гибели героя.
  // val newHP = ???

  val HP = 100
  val damages = Seq(5, 15, 25, 30, 7, 40, 45)

  val newHP = damages.foldLeft(HP){ (hp, damage) =>
    val tempHP = hp - damage
    if (tempHP < 0) throw new Exception("Герой погиб") else tempHP
  }

  Option.apply()
  println(newHP)
}

object J1Tasks2 extends App {

  // Дано здоровье героя HP = 100
  // Герой начинает схватку с противником и получает урон. Иногда противник промахиваеться.
  // Урон и промахи храниться в Seq(Some(5), None, Some(15), Some(25), None, Some(30), Some(7), Some(40), Some(45))
  // При получение урона герой теряет жизни. т.е. 100 - 5 = 95; 95 - None = 95, 95 - 15 = 80 ...
  // TODO: Просчитать сколько ударов способен пережить герой
  // val hitCount = ???

  val HP = 100
  val damages = Seq(Some(5), None, Some(15), Some(25), None, Some(30), Some(7), Some(40), Some(45))

  val hitCount = damages.foldLeft((HP,0)){ (acc, damage) =>
    damage match {
      case Some(dd) if  acc._1 - dd > 0 => (acc._1 - dd, acc._2 + 1)
      case None if  acc._1 > 0 => (acc._1, acc._2 + 1)
      case _ => acc
    }
  }

  println(hitCount._2)
}

object J1Tasks3 extends App {

  // TODO: Отловить ошибки через transform и взять текст из ошибки. Получить на выходе Seq(Future(Успех1), Future(Успех2), Future(Ошибка1)..)
  // TODO: Работать со списком фьюx не очень. Получить 1 фьючу вместе этого
  // Подсказка
  // val OneFutureOfValues: Future[Seq[String]] = res.foldLeft(Future.successful[Seq[String]](Seq.empty)) {(futureAcc, currentFuture) => ... }

  implicit val ec = ExecutionContext.global

  val results: Seq[Future[String]] = Seq(
    Future.successful("Успех1"),
    Future.successful("Успех2"),
    Future.failed(new Exception("Ошибка1")),
    Future.failed(new Exception("Ошибка2")),
    Future.successful("Успех3"),
    Future.failed(new Exception("Ошибка3")),
  )

  val res = results.map { future =>
    future.transform {
      case Success(res) => Success(res)
      case Failure(th) => Success(th.getMessage)
    }
  }

  val OneFutureOfValues: Future[Seq[String]] = res.foldLeft(Future.successful[Seq[String]](Seq.empty)) {(futureAcc, currentFuture) =>
    //for {
    //  f1 <- futureAcc
    //  f2 <- currentFuture
    //} yield f1 :+ f2*

    // or
    futureAcc.flatMap(f1 => currentFuture.map(f2 => f1 :+ f2))
  }

  println(Await.result(OneFutureOfValues, Duration.Inf))

  //or just
  println(Await.result(Future.sequence(res), Duration.Inf))
}

object J1Tasks4 extends App {

  implicit val ec = ExecutionContext.global

  //TODO: получить сообщения из ошибок в переменные типа String

  def method1: Future[String] = throw new Exception("No result1")
  def method2: Future[String] = Future.failed(new Exception("No result2"))

  val stringFuture1 = Future.fromTry(Try(method1).recover{
    case e: Exception => e.getMessage
  })
  val stringResult1 = Await.result(stringFuture1, Duration.Inf)
  println(stringResult1)

  val stringFuture2 = method2.recover {
    case e: Exception => e.getMessage
  }
  val stringResult2 = Await.result(stringFuture2, Duration.Inf)
  println(stringResult2)
}

object J1Tasks5 extends App {

  implicit val ec = ExecutionContext.global

  //TODO: в чем разница между .fallbackTo и .recoverWith

  def method1: Future[String] = Future {
    Thread.sleep(5000)
    println("Result after 5 sec")
    throw new Exception("DNO")
  }

  val callFuture = method1

  val futureResult = callFuture.fallbackTo{
    Future(println("I am here fallbackTo!")) // Напишет сразу, хотя 5 секунд еще не прошло
  }

  val futureResult2 = callFuture.recoverWith {
    case e: Exception => Future(println("I am here recoverWith!")) // напишет через 5 секунд после окончания фьючи
  }

  Await.result(futureResult, Duration.Inf)
  Await.result(futureResult2, Duration.Inf)

}

object J1Tasks6 extends App {

  //TODO вызов по имени и выззов по значении, передача функции как аргумента

  implicit val ec = ExecutionContext.global

  lazy val str1: String = {
    println("THere")
    "Hello world"
  }

  lazy val str2: String = {
    println("THere")
    "Hello world"
  }

  //byName(str1)
  val res = higherOrderFunctions( _ => str1)

  def usual(str: String): Future[String] = {
    println("Method start")
    Future(str)
  }

  def higherOrderFunctions(str: Unit => String): Future[String] = {
    println("Method higherOrderFunctions")
    Future(str())
  }

  def byName(str: => String): Future[String] = {
    println("Method byName")
    Future(str)
  }

  Await.result(res, Duration.Inf)

}

object J1Tasks7 extends App {

  // Сделать цепочку модификаций над строкой

  trait SpamFilter {
    def filter(in: String): String
  }

  val matukFilter: SpamFilter = _.replaceAll("Блин", "б***")
  val javaOnScala: SpamFilter = _.replaceAll("Java", "Scala")
  val uaUp: SpamFilter = _.replaceAll("ua", "UA")

  implicit val seq_S = Seq(matukFilter, javaOnScala, uaUp)

  implicit def checkString(targetString: String)(implicit f: Seq[SpamFilter]): String =
    f.foldLeft(targetString) { (acc, cF) =>
      cF.filter(acc)
    }

  val newValue = checkString("Блин! Я так люблю Java. Java - это лучший язык. Поехали на Java ua")

  println(newValue)

}

object J1Tasks8 extends App {

  //TODO да это же пример замыкания (Closure)
  //INFO: хранит ссылки на внешние переменные, не хранит значения. Следит за изменениями

  val const = 5
  type MyFunction = Int => Int
  val addConst: MyFunction = _ + const // замкнулись на const, если передать addConst куда-то дальше, то 5 все еще доступна

  println(addConst(5))
}

object J1Tasks9 extends App {

  //TODO да это же пример замыкания #2
  //INFO: хранит ссылки на внешние переменные, не хранит значения. Следит за изменениями

  implicit val ec = ExecutionContext.global

  println(Await.result(J1Tasks9Help.сlosure(Future(4)), Duration.Inf))
  println(Await.result(J1Tasks9Help.сlosure(Future(-4)), Duration.Inf))
}

object J1Tasks9Help {

  implicit val ec = ExecutionContext.global

  type MyFunction = Future[Int] => Future[Int]
  private val withModule: Int => Int = in => Math.abs(in)

  val сlosure: MyFunction = inF => {
    for {
      in <- inF
    } yield withModule(in)
  }
}