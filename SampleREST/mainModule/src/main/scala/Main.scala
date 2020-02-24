import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{pathPrefix, _}
import akka.http.scaladsl.server.{Route, RouteConcatenation}
import akka.stream.ActorMaterializer
import com.softwaremill.macwire._
import configuretion.CircleServiceConfiguration
import controller.CircleController
import service.{CircleService, CircleServiceImpl}

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object Main extends App {

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher



  val circleServiceConfiguration = CircleServiceConfiguration() // TODO make separate module for configurations
  val circleService: CircleService = wire[CircleServiceImpl]  // TODO make separate module for services
  val circleController: CircleController = wire[CircleController]

  val controllers = Seq(
    circleController
  )

  private val routes: Route =
      pathPrefix("api" / "v1") {
        circleController.route ~
          path("hello") {
            get {
              complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
            }
          }
      }

  val bindingFuture = Http().bindAndHandle(routes, "localhost", 8080)



  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done

}
