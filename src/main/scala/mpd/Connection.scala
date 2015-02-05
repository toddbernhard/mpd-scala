package mpd

trait Connection {
  /* close
   * kill
   * password
   * ping
   */
}

trait ConnectionInstances {
  implicit val connectionImplicit = new Connection { }
}

final object ConnectionInstances extends ConnectionInstances
