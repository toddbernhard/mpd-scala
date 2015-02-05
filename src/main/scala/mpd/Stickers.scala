package mpd

trait Stickers {
  /* sticker <>
   * get
   * set
   * delete
   * list
   * find
   */
}

trait StickersInstances {
  implicit val stickersImplicit = new Stickers { }
}

final object StickersInstances extends StickersInstances
