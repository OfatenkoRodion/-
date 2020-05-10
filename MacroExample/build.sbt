name := "MacroExample"
version := "0.1"
scalaVersion := "2.12.7"

val paradiseVersion = "2.1.1"

lazy val macros_implementations = (project in file("macros_implementations"))
  .settings(
    libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value
  )

lazy val root = (project in file(".")).aggregate(macros_implementations).dependsOn(macros_implementations)

addCompilerPlugin("org.scalamacros" % "paradise" % paradiseVersion cross CrossVersion.full)