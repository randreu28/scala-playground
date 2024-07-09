package DAOs

import io.getquill._
import zio._

trait Database {
  // The database connection is in application.properties
  val ctx = new SqliteZioJdbcContext(SnakeCase)
}

val sqliteDB =
  ZLayer.scoped {
    ZIO.acquireRelease {
      ZIO.attempt {
        val ds = new org.sqlite.SQLiteDataSource()
        ds.setUrl("jdbc:sqlite:sqlite.db")
        ds
      }
    } { ds =>
      ZIO.attempt(ds.getConnection.close()).orDie
    }
  }
