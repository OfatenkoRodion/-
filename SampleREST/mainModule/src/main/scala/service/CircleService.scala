package service

import configuretion.CircleServiceConfiguration
import model.Result

import scala.concurrent.{ExecutionContext, Future}

trait CircleService {
  def getArea(r: BigDecimal): Future[Result]
}

class CircleServiceImpl(circleServiceConfiguration: CircleServiceConfiguration)
                       (implicit executionContext: ExecutionContext) extends CircleService {

  import circleServiceConfiguration.Pi

  override def getArea(r: BigDecimal): Future[Result] = Future(Result(Pi * r * r))

}

