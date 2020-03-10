import com.sun.net.httpserver.Authenticator.Failure

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object J1Tasks10 extends App {

  //TODO показать примеры работы с трансформацией

  implicit val ec = ExecutionContext.global

  def mkSuccessfulFuture = Future.successful(666)

  def mkFailedFuture = Future.failed(new Exception("Fail"))

  // 1) Используя трансформ мы, как бы тупо не звучало, отлавливаем результат выполнения фьючи и заменяем ее на свою
  // Мы можем заменять только успешный результат
  // Например тут мы заменяем число на строку, ошибку мы не ловим
  def `example1.1` = {
    mkSuccessfulFuture.transform {
      case Success(result) => Success(result.toString)
    }
  }
  def `example1.2` = {
    mkFailedFuture.transform {
      case Success(result) => Success(result.toString)
    }
  }
  // startExample(`example1.1`)
  // ==> 666
  // startExample(`example1.2`)
  // scala.MatchError
  // Вывод: не определив Success или Failure нет смысла вообще использовать transform. Лучше тогда просто .map или .recover


  // 2) Помним, что трансформ типизированный и никто не запрещает указывпать тип который мы хотим получить
  def `example2.1` = {
    mkSuccessfulFuture.transform[String] {
      case Success(result) => Success(result.toString)
      case Failure(th) => Success(th.toString)
    }
  }
  // А еще никто не запрещает менять тип результата с успешного на проваленный и на оборот
  def `example2.2` = {
    mkSuccessfulFuture.transform[String] {
      case Success(result) => Failure(new Exception(result.toString))
      case Failure(th) => Success(th.toString)
    }
  }

  // 3) Помним, что у нас два трансформа.
  def `example3` = {
    mkSuccessfulFuture.transform[String](
      in => in.toString,
      th => new Exception("!!!!" + th.getMessage)
    )
  }

  // 4) А еще есть трансформ для фючек. Так на всякий случай, вдруг надо еще что-то асинхронно вычислить
  def `example4` = {
    mkSuccessfulFuture.transformWith[String] {
      case Success(result) => Future.successful[String](result.toString)
      case Failure(th) => Future.successful(th.toString)
    }
  }


  private def startExample[T](f: Future[T]): Unit = {
    println(Await.result(f, Duration.Inf))
  }

}
