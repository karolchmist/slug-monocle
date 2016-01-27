import monocle.{Optional, POptional, Lens, PLens}
import org.specs2.mutable.Specification


/**
  * Optional for tests.
  */
class OptionalTest extends Specification {

  "Optional" should {

    import monocle.macros.Lenses

    @Lenses("_") case class Street(name: String)
    @Lenses("_") case class Address(street: Street)
    @Lenses("_") case class Company(address: Address)
    @Lenses("_") case class Employee(company: Company)

    "headOptional" in {
      import monocle.function.all.headOption
      import monocle.std.string._

      val address = Address(Street("rue Servient"))
      val optionlAddressToFirstLetter: Optional[Address, Char] = Address._street composeLens Street._name composeOptional headOption
      val newAddress: Address = optionlAddressToFirstLetter.modify(_.toUpper)(address)
      newAddress ==== Address(Street("Rue Servient"))
    }

    "index + modify" in {
      import monocle.function.all.index
      import monocle.std.list.listIndex

      @Lenses("_") case class Carte(streets: List[Street])

      val carte = Carte(List(Street("rue Servient"), Street("cours gambetta")))

      val optionalCarteToFirstStreetName: Optional[Carte, String] = Carte._streets composeOptional index(1) composeLens Street._name

      val modifiedCarte: Carte = optionalCarteToFirstStreetName.modify(_.toUpperCase)(carte)

      modifiedCarte ==== Carte(List(Street("rue Servient"), Street("COURS GAMBETTA")))
    }

  }

}
