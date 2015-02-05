import sbt._
import Keys._

object build extends Build {
  val projectName = "mpd-scala"

  val buildSettings = Seq(
    scalaVersion := "2.11.5",
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
    //resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/snapshots/"
  )

  lazy val root = Project(
    projectName,
    file("."),
    settings = buildSettings ++ Seq(
      libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.1.0"
    )
  )

  /*
	    libraryDependencies ++= Seq(
        "org.scalaz" %% "scalaz-core" % "7.1.0"
  	    //"com.typesafe.akka" %% "akka-actor" % "2.3.9"
      ),
	initialCommands +=
	  """import mpd._
	  object AllInstancesDebug extends AllInstances with DebugInstances
	  import AllInstancesDebug._

	  println("usage: \"useCon { c => (scImplicit.load(SCURL(https://soundcloud.com/steve-cobby/heeds))).run(c) }\"")

	  val con = (implicitly[Base]).Connect("127.0.0.1", 6600)
	  def useCon[T](f: mpd.MPDS => T) = for { m <- con } yield f(m)"""
    )
  )
 */
}
