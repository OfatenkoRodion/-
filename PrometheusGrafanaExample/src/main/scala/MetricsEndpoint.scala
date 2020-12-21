import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import io.prometheus.client.{CollectorRegistry, Counter}

object MetricsEndpoint extends App {

  val collectorRegistryDefault = CollectorRegistry.defaultRegistry
  implicit val system = ActorSystem("my-system")
  val counter = buildCounter.register(collectorRegistryDefault)

  counter.labels("eventLabelName1", "eventDetailsLabelName1").inc()

  private def buildCounter = Counter
    .build()
    .name("metricName")
    .help("metricHelp")
    .labelNames("eventLabelName", "eventDetailsLabelName")


  val prometheusRoute: Route = new PrometheusRoute {
    val collectorRegistry: CollectorRegistry = collectorRegistryDefault
  }.prometheusRoute

  val bindingFuture = Http().bindAndHandle(prometheusRoute, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")

}
