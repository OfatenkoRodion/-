import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{pathPrefix, _}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import client.{ConnectionClient, ElasticsearchClientImpl, RedisClientDemoImpl}
import controller.ClickController
import model.{ElasticSearchConf, ElasticSearchFields}
import service.ClickServiceImpl
import utils.Scripts

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.Try

object DemoProject extends App {

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  // TODO use macwire or other
  val restClient = ConnectionClient.createClient()
  val demoClient = new ElasticsearchClientImpl(restClient)
  val redisClientDemoImpl = new RedisClientDemoImpl("localhost", 6379)
  val clickService = new ClickServiceImpl(demoClient, redisClientDemoImpl)
  val clickController = new ClickController(clickService)


  val routes: Route =
    pathPrefix("api" / "v1") {
      clickController.route
    }
  val bindingFuture = Http().bindAndHandle(routes, "localhost", 8080)
  println(s"Server online at http://localhost:8080/")

  try {
    val initFuture = Future {
      Scripts.initElasticsearchAndDefaultData(demoClient) // TODO rework this

      // ElasticSearchConf insertion, TODO rework this
      val elasticSearchConf = ElasticSearchConf(1, ElasticSearchFields(Set("click")))
      redisClientDemoImpl.setObject("1", elasticSearchConf)
      val elasticSearchConf2 = ElasticSearchConf(2, ElasticSearchFields(Set("purchase")))
      redisClientDemoImpl.setObject("2", elasticSearchConf2)
    }

    Await.result(initFuture, Duration.Inf)
  }
  catch {
    case th: Throwable =>
      println(th.getMessage)
  }
  finally {
    println("press any key to stop app")
    scala.io.StdIn.readLine()
    Try(demoClient.deleteIndex("click"))
    Try(restClient.close)
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())

  }
}