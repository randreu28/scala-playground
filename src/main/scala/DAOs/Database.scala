package DAOs

import io.getquill._
import zio._

trait Database {
  // The database connection is in application.properties
  val ctx = new SqliteZioJdbcContext(SnakeCase)
}
