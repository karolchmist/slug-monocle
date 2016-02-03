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

    "lense tuple" in {
      val hero: Hero = Hero(level = 12, name = "John")

      val _heroLevelName = Lens[Hero, (Int, String)](h => (h.level, h.name))
        {case (l,n) => h => h.copy(level = l, name = n)}

      _heroLevelName.modify{case (l,n) => (l+1, n.toUpperCase)}(hero) ==== Hero(13,"JOHN")
    }

    "each + optional composition" in {
      import monocle.function.Each.each
      import monocle.std.list.listEach

      val _heroLevel = GenLens[Hero](h => h.level)
      val _characterLevel = Character._hero composeLens _heroLevel
      val _gainLevel = each[List[Character], Character] composeOptional _characterLevel

      val characters: List[Character] = List(Hero(level = 12, name = "John"), Enemy(name = "Dragon"))

      val betterHeroes = _gainLevel.modify(_ + 1)(characters)

      betterHeroes  ==== List(Hero(level = 13, name = "John"), Enemy(name = "Dragon"))
    }
  }
}
