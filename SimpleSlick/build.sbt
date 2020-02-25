name := "SimpleRest2"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies += "com.typesafe.slick" %% "slick" % "3.3.2"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3" % Test

libraryDependencies += "org.postgresql" % "postgresql" % "42.2.10"
libraryDependencies += "com.github.tminglei" %% "slick-pg_core" % "0.18.1"
