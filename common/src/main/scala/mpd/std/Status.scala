package mpd
package std

import scala.concurrent._
import ExecutionContext.Implicits.global

import messages.{ StatusMsg, Status, State => MState, ExecutorComponent, SubSystem }
import messages.SubSystem._
import mpd.util.MpdParse

trait StatusMsgStd extends StatusMsg {
  this: ExecutorComponent =>
  import scalaz._
  import Scalaz._

  override def currentsong() = wread("currentsong") map { x =>
   x map { v =>
      val song = MpdParse.parseSong(MpdParse.mapValues(v))
      if (song.file.isDefined) song.some
      else none
    }
  }

  override def idle(xs: SubSystem*) = wread(s"""idle ${xs.mkString(" ")}""") map { x =>
    x map { v =>
      v flatMap { l: String =>
        val reg = "changed: (.*)".r
        l match {
	  case reg(s) => SubSystem.withName(s).some
          case _ => None
        }
      }
    }
  }

  override def status() = wread("status") map { x =>
    x map { v =>
      val s = MpdParse.mapValues(v)

      Status(
        s("volume").head.toInt,
        s("repeat").head.toInt,
        s("random").head.toInt,
        s("single").head.toInt,
        s("consume").head.toInt,
        s("playlist").head.toInt,
        s("playlistlength").head.toInt,
        s("xfade").head.toInt,
        s("mixrampdb").head,
        s("mixrampdelay").head,
        MState.withName(s("state").head),
        s("song").head.toInt,
        s("songid").head.toInt,
        s("time").head,
        s("elapsed").head,
        s("bitrate").head.toInt,
        s("audio").head,
        s("nextsong").head.toInt,
        s("nextsongid").head.toInt)
    }
  }
}
