name := """backend"""
organization := "com.burgermanjoe"

version := "1.0-SNAPSHOT"

libraryDependencies += filters
libraryDependencies += jdbc


// build.sbt

lazy val root = (project in file(".")).enablePlugins(PlayScala)
libraryDependencies += "com.google.api-client" % "google-api-client" % "1.32.1"
libraryDependencies += "com.google.http-client" % "google-http-client-jackson2" % "1.43.3"
scalaVersion := "2.13.18"
libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.2" % Test

libraryDependencies += "io.lettuce" % "lettuce-core" % "6.3.2.RELEASE"

libraryDependencies ++= Seq(
  "org.postgresql" % "postgresql" % "42.7.10"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.burgermanjoe.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.burgermanjoe.binders._"
