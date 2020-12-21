import java.io.StringWriter

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.exporter.common.TextFormat

import scala.collection.JavaConverters._

trait PrometheusRoute extends Directives {

  def collectorRegistry: CollectorRegistry

  private val `text/plain; version=0.0.4; charset=utf-8` = ContentType {
    MediaType.customWithFixedCharset(
      "text",
      "plain",
      HttpCharsets.`UTF-8`,
      params = Map("version" -> "0.0.4")
    )
  }

  def renderMetrics(registry: CollectorRegistry, names: Set[String]): String = {
    val writer = new StringWriter()
    TextFormat.write004(writer, registry.filteredMetricFamilySamples(names.toSet.asJava))
    writer.toString
  }

  val prometheusRoute =
    (get & path("metrics" / "prometheus") & parameter('name.*)) { names =>
      val content = renderMetrics(collectorRegistry, names.toSet)
      complete {
        HttpResponse(entity = HttpEntity(`text/plain; version=0.0.4; charset=utf-8`, content))
      }
    }
} 