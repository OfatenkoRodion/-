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

  @ApiOperation(value = "Считает площадь фигуры", httpMethod = "GET")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "area", paramType = "path", required = true, dataType = "number")
  ))
  @Path("/math/circle/{area}")
  override def route: Route = {
    pathPrefix("math"){
      path("circle" / DoubleNumber) { area =>
        get {
          onComplete(circleService.getArea(area)) {
            case Success(v) => complete(v)
          }
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
