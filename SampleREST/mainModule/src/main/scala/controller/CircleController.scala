package controller

import akka.http.scaladsl.marshalling.ToResponseMarshaller
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, MediaTypes}
import akka.http.scaladsl.server.Route
import io.circe.generic.auto._
import io.circe.{Encoder, Printer}
import service.CircleService
import util.Controller

import scala.concurrent.ExecutionContext
import scala.util.Success

class CircleController(circleService: CircleService)
                      (implicit executionContext: ExecutionContext) extends Controller {

  override def route: Route = {
    pathPrefix("math"){
      path("circle" / DoubleNumber) { area =>
        getAreaForCircle(area)
      }
    }
  }



  protected def getAreaForCircle(r: BigDecimal): Route = {
    get {
        onComplete(circleService.getArea(r)) {
          case Success(v) => complete(v)
        }
      }
  }

  implicit def jsonMarshaller[A](implicit encoder: Encoder[A], e: ToResponseMarshaller[HttpResponse]): ToResponseMarshaller[A] =
    e.compose(v =>
      HttpResponse(
      status = 200,
        entity = HttpEntity(MediaTypes.`application/json`, encoder.apply(v).printWith(Printer(true," ")))
    ))

}
