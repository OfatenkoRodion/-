import sbt._
import sbt.librarymanagement.ModuleID

object Dependencies {

  object MainModule extends VersionedDependenciesSource {

    val akkaVersion = "2.6.3"
    val akkaHttpVersion = "10.1.9"

    override protected def distributionDependencies: Seq[ModuleID] =
      Seq(
        "com.typesafe.akka" %% "akka-actor" % akkaVersion,
        "com.typesafe.akka" %% "akka-stream" % akkaVersion,
        "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
        "com.softwaremill.macwire" %% "macros" % "2.3.3" % "provided",
        "io.swagger.core.v3" % "swagger-core" % "2.1.0",
        "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
        "ch.qos.logback" % "logback-classic" % "1.2.3" % Test,
        "io.circe" %% "circe-parser" % "0.13.0-RC1",
        "io.circe" %% "circe-generic" % "0.13.0-RC1"
      )

    override protected def testDependencies: Seq[ModuleID] =
      Seq()
  }
}
