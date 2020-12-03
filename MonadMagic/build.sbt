name := "MonadMagic"
version := "0.1"
scalaVersion := "2.12.6"
val circeVersion = "0.13.0"

// https://mvnrepository.com/artifact/org.typelevel/cats-core
libraryDependencies += "org.typelevel" %% "cats-core" % "2.3.0"
libraryDependencies += "io.scalaland" %% "chimney" % "0.4.1"

libraryDependencies ++=
  Seq(
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-generic-extras" % circeVersion
  )