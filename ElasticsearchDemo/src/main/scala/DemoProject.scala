import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{pathPrefix, _}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import client.{ConnectionClient, ElasticsearchClientImpl}
import controller.ClickController
import model.{ElasticSearchConf, ElasticSearchFields}
import service.ClickServiceImpl
import utils.Scripts

import scala.util.Try

object DemoProject extends App {

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  // TODO use macwire or other
  val restClient = ConnectionClient.createClient()
  val demoClient = new ElasticsearchClientImpl(restClient)
  val clickService = new ClickServiceImpl(demoClient)
  val clickController = new ClickController(clickService)


  val routes: Route =
    pathPrefix("api" / "v1") {
      clickController.route
    }
  val bindingFuture = Http().bindAndHandle(routes, "localhost", 8080)
  println(s"Server online at http://localhost:8080/")

  try {
    Scripts.initElasticsearchAndDefaultData(demoClient) // TODO rework this

    val elasticSearchConf = ElasticSearchConf(1, ElasticSearchFields(Set("item_id", "name")))
    println(demoClient.viewData("click", "demo", elasticSearchConf))
  }
  finally {

/*    Try(demoClient.deleteIndex("click"))
    Try(restClient.close)

    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate()) */
  }
}