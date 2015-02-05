package mpd

trait PlaybackOpts {
  /* consume
   * crossfade
   * mixrampdb
   * mixrampdelay
   * random
   * repeat
   * setvol
   * single
   * replay_gain_mode
   * replay_gain_status
   */
}

trait PlaybackOptsInstances {
  implicit val playbackOptsImplicit = new PlaybackOpts { }
}

final object PlaybackOptsInstances extends PlaybackOptsInstances
