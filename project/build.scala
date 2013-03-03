import sbt._
import Keys._

object ImperialsBuild extends Build {
    lazy val root = Project(
      id = "imperials",
      base = file("."),
      settings = standardSettings) aggregate(client)

    lazy val client = Project(
      id = "client",
      base = file("client"),
      settings = standardSettings ++ Seq(
        libraryDependencies ++= Seq(
          "com.typesafe.akka" %% "akka-actor" % "2.2-M1",
          "net.databinder.dispatch" %% "dispatch-core" % "0.9.5"
        )
      )
    )

    lazy val standardSettings = Defaults.defaultSettings ++ Seq(
      organization := "com.github.cb372",
      version      := "0.1-SNAPSHOT",
      scalaVersion := "2.10.0",
      libraryDependencies ++= Seq(
        "org.mongodb" %% "casbah-core" % "2.5.0",
        "ch.qos.logback" % "logback-classic" % "1.0.9",
        "org.scalatest" %% "scalatest" % "2.0.M6-SNAP8" % "test"
      ),
      parallelExecution in Test := false
    )
}
