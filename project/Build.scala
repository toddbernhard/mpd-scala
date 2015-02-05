import sbt._
import Keys._

object build extends Build {
  val projectName = "mpd-scala"

  val buildSettings = Seq(
    scalaVersion := "2.11.5",
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
  )

  lazy val root = Project(
    projectName,
    file("."),
    settings = buildSettings ++ Seq(
      organization := "net.toddb",
      version := "0.1-SNAPSHOT",
      libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.1.0"
    )
  )
}
