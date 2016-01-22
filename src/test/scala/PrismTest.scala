import monocle.Prism
import monocle.macros.GenPrism
import org.specs2.mutable.Specification

import scalaz.{-\/, \/-}


/**
  * Prism for tests.
  * TODO BOF better do Optional
  */
class PrismTest extends Specification {

  "Prism" should {

    sealed trait Person
    case class Hero(level: Int) extends Person
    case class NPC(name: String) extends Person

    object Character {
      val _hero = GenPrism[Person, Hero]
      val _npc: Prism[Person, NPC] = GenPrism[Person, NPC]
    }

    // Not using, boring, better show Optional as Lesn + Prism"
    "work" in pending {
      Character._hero.getOption(Hero(level = 12)) ==== Some(Hero(level = 12))
      Character._hero.getOption(NPC(name = "Dupa")) ==== None

      Character._hero.getOrModify(Hero(213)) ==== \/-(Hero(213))
      Character._hero.getOrModify(NPC(name = "Hhhh")) ==== -\/(NPC("Hhhh"))
    }
  }

}
