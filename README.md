mpd-scala
=========

A functional Scala library for writing [MPD](http://www.musicpd.org/) clients. It implements the [MPD protocol](http://www.musicpd.org/doc/protocol/).

Usage:
```scala
scala> import mpd._, mpd.AllInstances._
import mpd._
import mpd.AllInstances._

scala> val conn = implicitly[Base].Connect("localhost", 6600)
conn: mpd.MPDR[mpd.MPDS] = \/-(MPDS(true,MPDC(localhost/127.0.0.1:6600,Socket[addr=localhost/127.0.0.1,port=6600,localport=57001],java.io.BufferedReader@341fea78,java.net.SocketOutputStream@1d28dda)))

scala> for(c <- conn) yield implicitly[Playback].play(None).run(c)
res9: scalaz.\/[mpd.MPDFailure,mpd.MPDR[(mpd.MPDS, Unit)]] = \/-(\/-((MPDS(false,MPDC(localhost/127.0.0.1:6600,Socket[addr=localhost/127.0.0.1,port=6600,localport=57001],java.io.BufferedReader@341fea78,java.net.SocketOutputStream@1d28dda)),())))

scala> // commence dance party
```

All credit goes to [ekroth](http://github.com/ekroth). This is the common library from [ekroth/mpd-monad](http://github.com/ekroth/mpd-monad), isolated and updated to scala 2.11.5.


