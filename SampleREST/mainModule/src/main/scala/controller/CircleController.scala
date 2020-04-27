package controller

import io.swagger.annotations._
import akka.http.scaladsl.marshalling.ToResponseMarshaller
import akka.http.scaladsl.model.{HttpEntity, HttpResponse, MediaTypes}
import akka.http.scaladsl.server.Route
import io.circe.generic.auto._
import io.circe.{Encoder, Printer}
import service.CircleService
import util.Controller
import javax.ws.rs.Path
import scala.concurrent.ExecutionContext
import scala.util.Success


@Api(value = "Пример [MATH]")
@Path("/")
class CircleController(circleService: CircleService)
                      (implicit executionContext: ExecutionContext) extends Controller {

  override def route: Route = {
    route1 ~
    route2
  }

  @ApiOperation(value = "Считает площадь фигуры", httpMethod = "GET")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "area", paramType = "path", required = true, dataType = "number")
  ))
  @Path("/math/circle/{area}")
  protected def route1: Route = pathPrefix("math") {
    path("circle" / DoubleNumber) { area =>
      get {
        onComplete(circleService.getArea(area)) {
          case Success(v) => complete(v)
        }
      }
    }
  }

  @ApiOperation(value = "Считает площадь фигуры через квери парам", httpMethod = "GET")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "area", paramType = "query", required = true, dataType = "number")
  ))
  @Path("/math/circle/")
  protected def route2: Route = pathPrefix("math") {
    parameter("area".as[Double]){ area =>
      get {
        onComplete(circleService.getArea(area)) {
          case Success(v) => complete(v)
        }
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
