package mpd.messages

import scala.concurrent.Future

object State extends Enumeration {
  type State = Value
  val Play, Stop, Pause = Value

  def apply(s: String) = s match {
    case "play" => Play
    case "stop" => Stop
    case "pause" => Pause
  }
}

import State._

case class Status(volume: Int, repeat: Int, random: Int, single: Int, consume: Int, playlist: Int, playlistlength: Int, xfade: Int, mixrampdb: String, mixrampdelay: String, state: State, song: Int, songid: Int, time: String, elapsed: String, bitrate: Int, audio: String, nextsong: Int, nextsongid: Int)

trait InfoMessages extends ServerMessages {
  def status(): Future[DefaultT[Status]]
  
  abstract override def required = super.required ++ Set(
    "status",
    "stats",
    "outputs",
    "commands",
    "notcommands",
    "tagtypes",
    "urlhandlers"
  )
}

