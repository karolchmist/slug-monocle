import monocle.{Lens, PLens}
import org.specs2.mutable.Specification


/**
  * Optional for tests.
  */
class OptionalTest extends Specification {

  "Optional" should {

    import monocle.macros.Lenses

    @Lenses("_") case class Street(name: String, price: Double = 0)
    @Lenses("_") case class Address(street: Street)
    @Lenses("_") case class Company(address: Address)
    @Lenses("_") case class Employee(company: Company)

    "headOptional" in {
      import monocle.function.all.headOption
      import monocle.std.string._

      val address = Address(Street("rue lavoisier"))
      val newAddress: Address = (Address._street composeLens Street._name composeOptional headOption).modify(_.toUpper)(address)
      newAddress ==== Address(Street("Rue lavoisier"))
    }
    "index" in {
      import monocle.function.all.index
      import monocle.std.list.listIndex

      @Lenses
      case class Carte(streets: List[Street])

      val carte = Carte(List(Street("rue abc")))

      val newCarte: Carte = (Carte.streets composeOptional index(0) composeLens Street._name).modify(_.toUpperCase)(carte)
      newCarte ==== Carte(List(Street("RUE ABC")))
    }

  }

}
