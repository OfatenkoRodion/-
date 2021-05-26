package controller

import akka.http.scaladsl.marshalling.ToResponseMarshaller
import akka.http.scaladsl.model.{HttpEntity, HttpResponse, MediaTypes}
import akka.http.scaladsl.server.Route
import io.circe.{Encoder, Printer}
import model.ConfigIdDTO
import utils.Controller
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import service.ClickService

import scala.util.{Failure, Success, Try}

class ClickController(clickService: ClickService) extends Controller {
  override def route: Route = clickDemo

  protected def clickDemo: Route =
    path("click" / "demo") {
      parameters("from", "size") { (from, size) =>
        (post & entity(as[ConfigIdDTO])) { configIdDTO =>
          val res = clickService.getData(configIdDTO.configId, Try(from.toInt).getOrElse(1), Try(size.toInt).getOrElse(10))

          onComplete(res) { // TODO Brrr
            case Success(None) => complete(errorResp)
            case Success(value) => complete(value)
            case Failure(th) =>
              println(th)
              complete(errorResp)
          }
        }
      }
    }

  // TODO move this to some separate file
  implicit def jsonMarshaller[A](implicit encoder: Encoder[A], e: ToResponseMarshaller[HttpResponse]): ToResponseMarshaller[A] =
    e.compose(v =>
      HttpResponse(
        status = 200,
        entity = HttpEntity(MediaTypes.`application/json`, encoder.apply(v).printWith(Printer(true, " ")))
      ))

  // TODO move this to some separate file
  private val errorResp = HttpResponse(status = 400)

}
