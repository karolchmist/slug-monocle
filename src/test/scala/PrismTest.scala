import monocle.Prism
import monocle.macros.GenPrism
import org.specs2.mutable.Specification

class PrismTest extends Specification {

  "Prism" should {
    sealed trait Person
    case class Hero(level: Int) extends Person
    case class Enemy(name: String) extends Person

    object Character {
      val _hero = GenPrism[Person, Hero]
      val _enemy: Prism[Person, Enemy] = GenPrism[Person, Enemy]
    }

    "getOption" in {
      val hero: Person = Hero(level = 12)
      val enemy: Person = Enemy(name = "Dragon")

      Character._hero.getOption(hero) ==== Some(Hero(level = 12))
      Character._hero.getOption(enemy) ==== None
    }
  }
}
