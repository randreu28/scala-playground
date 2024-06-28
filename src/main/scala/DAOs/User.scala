package DAOs

import doobie._
import doobie.implicits._
import cats.effect.IO

case class User(id: Int, name: String, email: String, password: String)

object UserDAO extends Database {

  // Should the application be in charged of initalizing the db?
  def createTable: IO[Int] = {
    sql"""
      CREATE TABLE IF NOT EXISTS users (
        id INTEGER PRIMARY KEY,
        name TEXT NOT NULL,
        email TEXT NOT NULL UNIQUE,
        password TEXT NOT NULL
      )
    """.update.run.transact(xa)
  }

  // TODO: Add password hashing
  // TODO: email and password verification
  def insert(user: User): IO[Int] = {
    sql"INSERT INTO users (id, name, email, password) VALUES (${user.id}, ${user.name}, ${user.email}, ${user.password})".update.run
      .transact(xa)
  }

  def selectById(id: Int): IO[Option[User]] = {
    sql"SELECT * FROM users WHERE id = $id"
      .query[User]
      .option
      .transact(xa)
  }
}
