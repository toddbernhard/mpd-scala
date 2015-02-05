package object mpd {
  import scalaz._

  val MPDMonadState = StateT.stateTMonadState[MPDS, MPDR]
  type MPDR[+A] = MPDFailure \/ A
  type MPD[A] = StateT[MPDR, MPDS, A]
  type ValueMap = Map[String, Seq[String]]
  type Song = ValueMap

  object SubSystem extends Enumeration {
    type SubSystem = Value
    val database, update, stored_playlist,
	playlist, player, mixer,
	output, options, sticker,
	subscription, message = Value
  }

  type SubSystem = SubSystem.Value
}
