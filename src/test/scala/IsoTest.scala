import monocle.Iso
import org.specs2.mutable.Specification


/**
  * Iso for tests.
  */
class IsoTest extends Specification {

  "Iso" should {

    case class Kilogram(v: Double)
    case class Pound(v: Double)

    val kilogramToPound = Iso[Kilogram, Pound](
      get = k => Pound(k.v / 0.45359237))(
      reverseGet = p => Kilogram(p.v * 0.45359237)
    )

    "get" in {
      kilogramToPound.get(Kilogram(23)) ==== Pound(50.70632030252184)
    }

    case class Stone(v: Double)

    val stoneToPound = Iso[Stone, Pound](
      get = s => Pound(s.v * 14)) (
      reverseGet = p => Stone(p.v / 14)
    )

    "compose" in {
      val kgToStone = kilogramToPound composeIso stoneToPound.reverse
      kgToStone.get(Kilogram(23)) ==== Stone(3.621880021608703)
    }

  }
}
