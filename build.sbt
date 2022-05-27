ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "tweet-streaming"
  )

libraryDependencies += "org.apache.kafka" %% "kafka" % "3.1.0"