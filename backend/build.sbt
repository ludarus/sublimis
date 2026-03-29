name := """backend"""
organization := "com.burgermanjoe"

version := "1.0-SNAPSHOT"

libraryDependencies += filters
libraryDependencies += jdbc

// build.sbt

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.18"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.2" % Test

libraryDependencies ++= Seq(
  "org.postgresql" % "postgresql" % "42.7.10"
)
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.burgermanjoe.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.burgermanjoe.binders._"
