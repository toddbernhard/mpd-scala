package mpd

trait AllInstances 
  extends BaseInstances
  with ConnectionInstances
  with MusicDbInstances
  with OutputsInstances
  with P2pInstances
  with PlaybackOptsInstances
  with PlaybackInstances
  with PlaylistDbInstances
  with PlaylistInstances
  with ReflectionInstances
  with SCInstances
  with StatusInstances
  with StickersInstances

final object AllInstances extends AllInstances
