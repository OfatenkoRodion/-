name := "ElasticsearchDemo"

version := "0.1"

scalaVersion := "2.13.6"

val akkaVersion = "2.6.3"
val akkaHttpVersion = "10.1.9"

// https://mvnrepository.com/artifact/org.elasticsearch.client/elasticsearch-rest-high-level-client
libraryDependencies ++= Seq(
  "org.elasticsearch.client" % "elasticsearch-rest-high-level-client" % "7.12.1",
  "de.heikoseeberger" %% "akka-http-circe" % "1.36.0" excludeAll(ExclusionRule(organization = "de.heikoseeberger")),
  "io.circe" %% "circe-parser" % "0.13.0-RC1",
  "io.circe" %% "circe-generic" % "0.13.0-RC1",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "net.debasishg" %% "redisclient" % "3.30")
