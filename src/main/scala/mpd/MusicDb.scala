package mpd

trait MusicDb {
  /* count
   * find
   * findadd
   * list
   * listall
   * listallinfo
   * lsinfo
   * search
   * searchadd
   * searchaddpl
   * update
   * rescan
   */
}

trait MusicDbInstances {
  implicit val musicDbImplicit = new MusicDb { }
}

final object MusicDbInstances extends MusicDbInstances
