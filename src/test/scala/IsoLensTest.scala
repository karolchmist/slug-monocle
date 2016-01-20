import monocle.macros.GenLens
import monocle.macros.Lenses
import monocle.{Lens, Iso}
import org.specs2.mutable.Specification

/**
  * Lenses for traversing hierarchy of objects. Get, set, modify
  */
class IsoLensTest extends Specification {
  @Lenses("_") case class Kilogram(v: Double)
  @Lenses("_") case class Pound(v: Double=0)
  @Lenses("_") case class Stone(v: Double)

  val kilogramToPound = Iso[Kilogram, Pound](
    get = k => Pound(k.v / 0.45359237))(
    reverseGet = p => Kilogram(p.v * 0.45359237)
  )

  val stoneToPound = Iso[Stone, Pound](
    get = s => Pound(s.v * 14))(
    reverseGet = p => Stone(p.v / 14)
  )

  "lens + iso compose" in {
    val _v: Lens[Pound, Double] = GenLens[Pound](_.v)
    val x = kilogramToPound.composeLens(_v)
    x.modify(_ + 10)(Kilogram(23)) ==== Kilogram(27.535923699999998)

    // WTF
    val y = kilogramToPound.composeLens(Pound._v)
    y.modify(_ + 10)(Kilogram(23)) ==== Kilogram(27.535923699999998)
  }
}
