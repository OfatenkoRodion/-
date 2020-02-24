import Dependencies.MainModule

val mainModuleVersion = "0.0.1"

lazy val mainProj: Project = project.in(file("."))
  .aggregate(mainModule)

lazy val mainModule: Project = project
  .settings(
    name:=  "mainModule",
    version := mainModuleVersion,
    scalaVersion := "2.12.6",
    libraryDependencies := MainModule.dependencies,
    Compile / mainClass := Some("Main") 
  )
