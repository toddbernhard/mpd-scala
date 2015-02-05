package mpd

trait Status {
  import scala.language.postfixOps
  import scalaz._
  import Scalaz._

  /** clear error */
  def clearError()(implicit b: Base) = b.writeln("clearerror")

  /** current song */
  def currentSong()(implicit b: Base): MPD[Option[Song]] = 
    b.wread("currentsong") map { x =>
      val song = util.MpdParse.mapValues(x)
      if (song.nonEmpty) song.some else none
  }

  /** idle for new event */
  def idle(xs: Set[SubSystem])(implicit b: Base): MPD[Set[SubSystem]] =
    b.wread(s"""idle ${xs.mkString(" ")}""") map {
      _ flatMap {
        val reg = "changed: (.*)".r
        _ match {
          case reg(s) => SubSystem.withName(s).some
          case _ => None
        }
      } toSet
    }
  
  /** status */
  def status()(implicit b: Base): MPD[ValueMap] = 
    b.wread("status") map { util.MpdParse.mapValues(_) }
}

trait StatusInstances {
  implicit val statusImplicit = new Status { }
}

final object StatusInstances extends StatusInstances
