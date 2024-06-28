package DAOs

import cats.effect.IO
import doobie.*
import doobie.implicits.*
import doobie.util.transactor.Transactor

trait Database {
  val xa: Transactor[IO] = Transactor.fromDriverManager[IO](
    "org.sqlite.JDBC",
    "jdbc:sqlite:sqlite.db",
    None
  )
}
