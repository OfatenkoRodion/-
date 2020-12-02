import java.io.StringWriter

import io.prometheus.client.exporter.common.TextFormat
import io.prometheus.client.{CollectorRegistry, Counter, Gauge, Histogram, Summary}

// Считает кол-во вызовов метода .inc
// Можно через него считать кол-во вызовов контроллера
object CounterExample extends App {
  SomeCounter.counter.inc()
  SomeCounter.counter.inc()
  println(SomeCounter.counter.get())

  object SomeCounter {
    val counter: Counter = Counter.build.name("counter").help("counter description").register
  }
}

// Тоже самое что и counter но еще можно уменьшать
// Можно использовать для отслеживания кол-ва тяжелых задач ставя .inc на візов метода и .dec на конец метода
object GaugeExample extends App {
  SomeGauge.gauge.inc()
  SomeGauge.gauge.inc()
  SomeGauge.gauge.dec()
  println(SomeGauge.gauge.get())

  object SomeGauge {
    val gauge: Gauge = Gauge.build.name("gauge").help("gauge description").register
  }
}


object SummaryExampleTime extends App {
  val summaryTime: Summary = Summary.build.name("summaryTime").help("summary description").register

  def method: Double = {
    val timer = summaryTime.startTimer
    Thread.sleep(1000)
    timer.observeDuration
  }

  println(method)
  println(method)
}

object SummaryExampleSize extends App {
  val summarySize: Summary = Summary.build
    .quantile(0.5, 0.05)   // Add 50th percentile (= median) with 5% tolerated error
    .quantile(0.9, 0.01)   // Add 90th percentile with 1% tolerated error
   .name("summaryTime").help("summary description").register

  summarySize.observe(100)
  summarySize.observe(300)
  summarySize.observe(1)
  summarySize.observe(100)
  summarySize.observe(300)
  summarySize.observe(1)


  println("count " + summarySize.get().count) //6.0
  println("sum " + summarySize.get().sum) // 802.0
  println("quantiles " + summarySize.get().quantiles) // {0.5=100.0, 0.9=300.0}
}


object HistogramExample extends App {
  val histogram: Histogram = Histogram.build.name("histogram").help("histogram description").register

  def method: Double = {
    val timer = histogram.startTimer
    Thread.sleep(1000)
    timer.observeDuration
  }

  println(method)
  println(method)
}

object LabelExample extends App {
  SomeCounter.counter.labels("place1").inc()
  SomeCounter.counter.labels("place2").inc()
  SomeCounter.counter.labels("place1").inc()

  println(SomeCounter.counter.collect())

  object SomeCounter {
    val counter: Counter = Counter.build.name("counter").help("counter description").labelNames("method").register
  }
}