package mpd

trait PlaylistDb {
  /* listplaylist
   * listplaylistinfo
   * listplaylists
   * load
   * playlistadd
   * playlistclear
   * playlistdelete
   * playlistmove
   * rename
   * rm
   * save
   */
}

trait PlaylistDbInstances {
  implicit val playlistDbImplicit = new PlaylistDb { }
}

final object PlaylistDbInstances extends PlaylistDbInstances
