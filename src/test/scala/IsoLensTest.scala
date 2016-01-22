import monocle.macros.GenLens
import monocle.macros.Lenses
import monocle.{Lens, Iso}
import org.specs2.mutable.Specification

/**
  * Lenses for traversing hierarchy of objects. Get, set, modify
  */
class IsoLensTest extends Specification {

  val kilogramToPound = Iso[Kilogram, Pound](
    get = k => Pound(k.v / 0.45359237))(
    reverseGet = p => Kilogram(p.v * 0.45359237)
  )
  val stoneToPound = Iso[Stone, Pound](
    get = s => Pound(s.v * 14))(
    reverseGet = p => Stone(p.v / 14)
  )

  @Lenses("_") case class Kilogram(v: Double)

  @Lenses("_") case class Pound(v: Double = 0)

  @Lenses("_") case class Stone(v: Double)

  "lens + iso compose" in {
    // false negative compilation error in idea..
    val y = kilogramToPound.composeLens(Pound._v)
    y.modify(_ + 10)(Kilogram(23)) ==== Kilogram(27.535923699999998)
  }
}
