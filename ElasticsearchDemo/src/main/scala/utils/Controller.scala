package utils

import akka.http.scaladsl.server.{Directives, Route}

trait Controller extends Directives {
  def route: Route
}