package mpd

trait Reflection {
  /* config
   * commands
   * notcommands
   * tagtypes
   * urlhandlers
   * decoders
   */
}

trait ReflectionInstances {
  implicit val reflectionImplicit = new Reflection { }
}

final object ReflectionInstances extends ReflectionInstances
