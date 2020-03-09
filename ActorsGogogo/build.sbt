name := "ActorsGogogo"

version := "0.1"

scalaVersion := "2.12.6"

// https://mvnrepository.com/artifact/com.typesafe.akka/akka-actor
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.3"
// https://mvnrepository.com/artifact/com.typesafe.akka/akka-actor-typed
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % "2.6.3"

libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % "2.6.3"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"