name := "PrometheusGrafanaExample"

version := "0.1"

scalaVersion := "2.13.3"


libraryDependencies += "io.prometheus" % "simpleclient" % "0.9.0"
libraryDependencies += "io.prometheus" % "simpleclient_hotspot" % "0.9.0"
libraryDependencies += "io.prometheus" % "simpleclient_common" % "0.9.0"

// https://mvnrepository.com/artifact/com.typesafe.akka/akka-http
libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.2.1"
// https://mvnrepository.com/artifact/com.typesafe.akka/akka-actor
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.10"
// https://mvnrepository.com/artifact/com.typesafe.akka/akka-stream
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.6.10"


