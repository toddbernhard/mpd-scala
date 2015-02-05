package mpd

import java.net.{ Socket => JSocket, InetSocketAddress, ConnectException }
import java.io.{ BufferedReader, InputStreamReader, OutputStream }

import scalaz._
import Scalaz._

final case class MPDC(addr: InetSocketAddress, socket: JSocket, read: BufferedReader, out: OutputStream)
final case class MPDS(flushed: Boolean, con: MPDC)

sealed trait MPDFailure
final case class MPDUnknown(e: Exception) extends MPDFailure
final case class MPDAck(err: String, num: String, cmd: String, msg: String) extends MPDFailure
final case class MPDBogus(response: String) extends MPDFailure
final case class MPDTimeout(err: String) extends MPDFailure

trait Base {
  import BaseInstances._

  /** get state */
  def get = MPDMonadState.get

  /** put state */
  def put(s: MPDS) = MPDMonadState put s

  /** connect to address */
  def Connect(addr: String, port: Int): MPDR[MPDS] = {
    val socket = new JSocket()
    val sockAddr = new InetSocketAddress(addr, port)
    socket.setSoTimeout(Timeout)

    try {
      socket.connect(sockAddr, Timeout)

      val in = new BufferedReader(new InputStreamReader(socket.getInputStream), BuffSize)
      val line = in.readLine
      line match {
        case OKConnect(ver) => {
          val input = new BufferedReader(new InputStreamReader(socket.getInputStream, Encoding), BuffSize)
          val output = socket.getOutputStream()
          MPDS(true, MPDC(sockAddr, socket, input, output)).right
        }
        case ACK(err, num, cmd, msg) => MPDAck(err, num, cmd, msg).left
        case x => MPDBogus(s"Unknown response: $x").left
      }
    } catch {
      case e: Exception => MPD.handleException(e)
    }

  }

  /** connect to current address  */
  def connect(): MPD[Unit] = for {
    s <- get
    MPDS(_, MPDC(a, _, _, _)) = s
  } yield connect(a.getHostName, a.getPort)


  /** connect to address */
  def connect(addr: String, port: Int) = MPDF {
    _ => {
      Connect(addr, port) match {
	case \/-(x) => (x, ()).right
	case -\/(x) => x.left
      }
    }
  }

  /** disconnect */
  def disconnect() = MPD {
    case MPDS(_, c@MPDC(a, s, r, o)) => {
      o.close
      r.close
      s.close

      (MPDS(false, c), ())
    }
  }

  /** clear input */
  def clear() = MPD {
    case x@MPDS(_, MPDC(_, socket, in, _)) => (x, in.skip(socket.getInputStream.available))
  }

  /** write */
  def write(cmd: String) = MPD {
    case s@MPDS(_, MPDC(_, _, _, o)) => {
      o.write(cmd.getBytes(Encoding))
      (s copy (flushed = false), ())
    }
  }

  /** read lines until end, blocking until first line */
  def read() = MPDF[Vector[String]] {
    case s@MPDS(_, MPDC(_, _, in, _)) => {
      @annotation.tailrec def readEnd(out: Vector[String]): MPDR[Vector[String]] =
	in.readLine match {
          case null => MPDBogus("empty line").left
	  case ACK(err, num, cmd, msg) => MPDAck(err, num, cmd, msg).left
          case OK() => out.right
          case x => readEnd(out :+ x)
	}

      try {
        readEnd(Vector.empty) match {
	  case \/-(x) => (s, x).right
	  case -\/(x) => x.left
        }
      } catch {
        case e: Exception => MPD.handleException(e)
      }
    }
  }

  /** flush output */
  def flush() = MPD {
    case s@MPDS(_, MPDC(_, _, _, o)) => {
      o.flush
      (s copy (flushed = true), ())
    }
  }

  /** helpers */

  /** disconnect and connect */
  def reconnect() = for {
    _ <- disconnect
    _ <- connect
  } yield ()

  /** write with new line */
  def writeln(cmd: String) = write(cmd + "\n")

  /** write command list begin */
  def clbegin() = writeln(CmdBegin)

  /** write command list end */
  def clend() = writeln(CmdEnd)

  /** encapsulate in command list */
  def cl[A](f: MPD[A]) = for {
    _ <- clbegin
    x <- f
    _ <- clend
  } yield x

  /** write and read
    * more specifically:
    * clear, write in command list, flush and read */
  def wread(cmd: String): MPD[Vector[String]] = for {
    _ <- clear
    _ <- clbegin
    _ <- write(cmd)
    _ <- clend
    _ <- flush
    s <- read
  } yield s
}

trait BaseInstances {
  /** MPD with explicit MPDFailure */
  final object MPDF extends StateTInstances with StateTFunctions {
    def apply[A](f: MPDS => MPDFailure \/ (MPDS, A)): MPD[A] = new MPD[A] {
      def apply(s: MPDS) = f(s)
    }
  }

  /** MPD using try catch */
  final object MPD extends StateTInstances with StateTFunctions {
    /** match exception to MPDFailure */
    def handleException(e: Exception) = e match {
      case t: ConnectException => MPDTimeout(t.toString).left
      case e: Exception => MPDUnknown(e).left
    }

    def apply[A](f: MPDS => (MPDS, A)): MPD[A] = new MPD[A] {
      def apply(s: MPDS) =
	try {
          f(s).right
        } catch {
          case e: Exception => handleException(e)
        }
    }
  }

  implicit val baseImplicit: Base = new Base { }

  val OKStr = "OK"
  val ACKStr = "ACK"
  val BuffSize = 1024
  val OKConnect = s"""$OKStr MPD (.*)""".r
  val OK = OKStr.r
  val ACK = """$ACKStr [(.*)@(.*)] \{(.*)\}(.*)""".r
  val Encoding = "UTF-8"
  val Timeout = 5000
  val CmdBegin = "command_list_begin"
  val CmdEnd = "command_list_end"
}

final object BaseInstances extends BaseInstances
