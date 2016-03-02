name := """catsApi"""

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.5",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "com.typesafe.play" %% "play-json" % "2.4.6",
  "ch.qos.logback" % "logback-classic" % "1.1.3"
)

