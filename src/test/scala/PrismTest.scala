import jdk.nashorn.internal.parser.JSONParser
import monocle.Prism
import monocle.macros.GenPrism
import org.specs2.matcher.DisjunctionMatchers
import org.specs2.mutable.Specification

import scala.util.parsing.json.{JSON, JSONType, JSONObject}
import scalaz.{\/-, \/, -\/}


/**
  * Prism for tests.
  * TODO BOF better do Optional
  */
class PrismTest extends Specification {

  "Prism" should {

    sealed trait Person
    case class Hero(level:Int) extends Person
    case class NPC(name:String)extends Person

    object Deck {
      val _hero = GenPrism[Person, Hero]
      val _npm = GenPrism[Person, NPC]
    }

    "work" in {
      Deck._hero.getOption(NPC(name = "Dupa")) ==== None

      Deck._hero.getOrModify(Hero(213)) ==== \/-(Hero(213))
      Deck._hero.getOrModify(NPC(name = "Hhhh")) ==== -\/(NPC("Hhhh"))
    }
  }

}
