package mpd

import scalaz._

case class SCTrack(id: Int) {
  def path = s"${SCInstances.URI}://track/$id"  
}

case class SCPlaylist(id: Int) {
  def path = s"${SCInstances.URI}://playlist/$id"
}

case class SCURL(url: String) {
  def path = s"${SCInstances.URI}://url/${url.replace("https", "http")}"
}

trait SC {
  import SCInstances._

  /** load track by id */
  def load(id: SCTrack)(implicit b: Base) = for {
    _ <- b.clbegin
    _ <- b.writeln("load " + id.path)
    _ <- b.clend
  } yield ()

  /** load playlist by id */
  def load(id: SCPlaylist)(implicit b: Base) = for {
    _ <- b.clbegin
    _ <- b.writeln("load " + id.path)
    _ <- b.clend
  } yield ()

  /** load playlist/track by url */
  def load(url: SCURL)(implicit b: Base) = for {
    _ <- b.clbegin
    _ <- b.writeln("load " + url.path)
    _ <- b.clend
  } yield ()
}

trait SCInstances {
  val URI = "soundcloud"

  implicit val scImplicit = new SC { }
}

final object SCInstances extends SCInstances
