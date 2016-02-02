import monocle.{PLens, Lens}
import monocle.macros.GenLens
import org.specs2.mutable.Specification

/**
  * Lenses for traversing hierarchy of objects.
  */
class LensTest extends Specification {

  import monocle.macros.Lenses

  @Lenses("_") case class Facture(client: Client, id: String)

  @Lenses("_") case class Client(firstName: String, lastName: String, address: Address)

  @Lenses("_") case class Address(street: Street, city: String)

  @Lenses("_") case class Street(name: String, number: Int)

  // manual Lens creation
  object Facture {
    val _client1: Lens[Facture, Client] = Lens[Facture, Client](_.client)(c => f => f.copy(client = c))
    val _client2: Lens[Facture, Client] = GenLens[Facture](_.client)
  }

  "Lens" should {
    val street = Street(name = "rue mercière", number = 23)
    val address = Address(city = "Lyon", street = street)
    val client = Client(firstName = "Jean", lastName = "Dutronc", address = address)
    val facture = Facture(id = "321", client = client)

    def toCamelCase(s: String): String =
      s.split(" ")
        .map(w => w.head.toUpper + w.tail)
        .mkString(" ")

    "get" in {
      Street._name.get(street) ==== "rue mercière"
    }

    "set" in {
      val newName: String = "Saint Etienne"
      Street._name.set(newName)(street) ==== street.copy(name = newName)
    }

    "modify" in {
      Client._lastName.modify(_.toUpperCase)(client) ==== client.copy(lastName = client.lastName.toUpperCase)
    }

    "compose + modify" in {
      val lensFactureToStreetName: Lens[Facture, String] =
        (Facture._client
          composeLens Client._address
          composeLens Address._street
          composeLens Street._name)

      val newFacture: Facture = lensFactureToStreetName.modify(toCamelCase)(facture)

      newFacture.client.address.street.name ==== "Rue Mercière"

      // the same with DSL symbols
      val lensFactureToStreetnameDSL =
        (Facture._client
          ^|-> Client._address
          ^|-> Address._street
          ^|-> Street._name)

      val newFacture2: Facture = lensFactureToStreetnameDSL
          .modify(toCamelCase)(facture)
      newFacture2.client.address.street.name ==== "Rue Mercière"

      // standard way....
      val newFacture3 = facture.copy(
        client = facture.client.copy(
          address = facture.client.address.copy(
            street = facture.client.address.street.copy(
              name = toCamelCase(facture.client.address.street.name)
            ))))
      newFacture3.client.address.street.name ==== "Rue Mercière"
    }

    "modifyF aka lift" in {
      // to get Functor[List] instance
      import scalaz.std.list.listInstance

      val address: Address = Address(Street("rue Maronier", 33), "Lyon")

      val lensAddressToStreetNumber: Lens[Address, Int] =
        (Address._street composeLens Street._number)

      val neighbours: List[Address] = lensAddressToStreetNumber.modifyF(n => List(n - 1, n + 1))(address)
      neighbours ==== List(Address(Street("rue Maronier", 32), "Lyon"), Address(Street("rue Maronier", 34), "Lyon"))
    }
  }
}
