ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "tweet-streaming"
  )

libraryDependencies += "com.typesafe" % "config" % "1.4.2"
libraryDependencies += "org.apache.kafka" %% "kafka" % "3.1.0"

libraryDependencies ++= Seq(
  "com.lihaoyi" %% "upickle" % "2.0.0",
  "com.lihaoyi" %% "requests" % "0.7.1"
)

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "1.7.36",
  "org.slf4j" % "slf4j-simple" % "1.7.36"
)
