import monocle._
import monocle.macros.{GenLens, GenPrism}
import org.specs2.mutable.Specification

class PrismTest extends Specification {

  "Prism" should {
    sealed trait Character
    case class Hero(level: Int, name: String = "Nameless") extends Character
    case class Enemy(name: String) extends Character

    object Character {
      val _hero = GenPrism[Character, Hero]
      val _enemy: Prism[Character, Enemy] = GenPrism[Character, Enemy]
    }

    "getOption" in {
      val hero: Character = Hero(level = 12)
      val enemy: Character = Enemy(name = "Dragon")

      Character._hero.getOption(hero) ==== Some(Hero(level = 12))
      Character._hero.getOption(enemy) ==== None
    }
  }
}
