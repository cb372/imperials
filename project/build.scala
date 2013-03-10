import sbt._
import Keys._
import play.Project._

object ImperialsBuild extends Build {
    lazy val root = Project(
      id = "imperials",
      base = file("."),
      settings = standardSettings) aggregate(client, dashboard)

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

    lazy val dashboard = play.Project(
      "dashboard",
      "0.1",
      commonDeps ++ Seq(
        // deps
      ),
      path = file("dashboard")
    )

    lazy val commonDeps = Seq(
      "org.mongodb" %% "casbah-core" % "2.5.0",
      "ch.qos.logback" % "logback-classic" % "1.0.9",
      "org.json4s" %% "json4s-jackson" % "3.1.0",
      "org.json4s" %% "json4s-mongo" % "3.1.0",
      "org.json4s" %% "json4s-ext" % "3.1.0",
      "org.scalatest" %% "scalatest" % "2.0.M6-SNAP8" % "test"
    )

    lazy val standardSettings = Defaults.defaultSettings ++ Seq(
      organization := "com.github.cb372",
      version      := "0.1-SNAPSHOT",
      scalaVersion := "2.10.0",
      libraryDependencies ++= commonDeps,
      parallelExecution in Test := false
    )
}
