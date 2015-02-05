package mpd

trait Outputs {
  /* disableoutput
   * enableooutput
   * outputs
   */
}

trait OutputsInstances {
  implicit val outputsImplicit = new Outputs { }
}

final object OutputsInstances extends OutputsInstances
