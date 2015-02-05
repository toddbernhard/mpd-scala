package mpd

trait P2p {
  /* subscribe
   * unsubscribe
   * channels
   * readmessages
   * sendmessage
   */
}

trait P2pInstances {
  implicit val p2pImplicit = new P2p { }
}

final object P2pInstances extends P2pInstances
