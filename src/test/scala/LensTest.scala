import org.specs2.mutable.Specification


/**
  * Lenses for traversing hierarchy of objects. Get, set, modify
  */
class LensTest extends Specification {

  import monocle.macros.Lenses

  @Lenses("_") case class Street(name: String)
  @Lenses("_") case class Address(street: Street)
  @Lenses("_") case class Company(address: Address)
  @Lenses("_") case class Employee(company: Company)

  "Lens" should {

    val street = Street(name = "rue Lavoisier")
    val address = Address(street = street)
    val company = Company(address = address)

    "get" in {
      Street._name.get(street) ==== "rue Lavoisier"
    }
    "set" in {
      Street._name.set("rue Maronier")(street) ==== Street("rue Maronier")
    }
    "composition + modify" in {
      val newAddress: Address = (Address._street composeLens Street._name).modify(_.toUpperCase)(address)
      newAddress ==== Address(Street("RUE LAVOISIER"))

      val newAddress2: Address = (Address._street ^|-> Street._name).modify(_.toUpperCase)(address)
      newAddress2 ==== Address(Street("RUE LAVOISIER"))
    }
    "lift" in {
//      import monocle.std.all
//      import monocle.std.list._
//      import monocle.function.all
      // FIXME why necessary?
      import scalaz.std.list._

      val address1: Address = Address(Street("Rue Maronier"))

      val newAddress: List[Address] = (Address._street composeLens Street._name).modifyF(n => List(n.toLowerCase, n.toUpperCase))(address1)
      newAddress ==== List(Address(Street("rue maronier")), Address(Street("RUE MARONIER")))
    }
  }
}
