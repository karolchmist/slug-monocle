import java.time.{Year, Duration}

import org.specs2.mutable.Specification

/**
  * Created by karol on 1/20/16.
  */
class ComplexExampleTest extends Specification {

  import monocle.macros.Lenses

  @Lenses("_") case class Song(title: String, length: Duration, singleReleaseYear: Option[Year] = None)
  @Lenses("_") case class Album(title: String, songs: List[Song], year: Year)
  @Lenses("_") case class Artist(name: String, albums: List[Album])

  "Lens" should {

    val song01 = Song(title = "Beat It", length = Duration.ofMinutes(3).plusSeconds(10))
    val song02 = Song(title = "Billie Jean", length = Duration.ofMinutes(4).plusSeconds(20))

    val album = Album(title = "Thriller", songs = List(song01, song02), year = Year.of(1982))
    val artist = Artist(name = "Michael Jackson", albums = List(album))

    "composition + index + set" in {
      import monocle.function.Index.index
      import monocle.std.list.listIndex

      album.songs(1).singleReleaseYear ==== None

      val updatedAlbum = (Album._songs composeOptional index(1) composeLens Song._singleReleaseYear).set(Some(Year.of(1983)))(album)

      album.songs(1).singleReleaseYear ==== None
      updatedAlbum.songs(1).singleReleaseYear.map(_.getValue) ==== Some(1983)
    }

    "composition + fold" in {
      import monocle.Iso
      import monocle.function.Each.each
      import monocle.std.list.listEach
      import scalaz.Scalaz.longInstance

      val durationToSeconds = Iso[Duration,Long](_.getSeconds)(Duration.ofSeconds)

      val allSongsLength = (Album._songs composeTraversal each composeLens Song._length composeIso durationToSeconds).fold(album)

      allSongsLength ==== Duration.ofMinutes(7).plusSeconds(30).getSeconds

      // comparison to standard way
      album.songs.map(_.length.getSeconds).foldLeft(0L)(_ + _) ==== Duration.ofMinutes(7).plusSeconds(30).getSeconds
    }

  }
}
