package DAOs

import doobie._
import doobie.implicits._
import cats.effect.IO
import cats.implicits._

final case class User(
    id: Int,
    name: String,
    email: String,
    password: String
)

final case class NewUser(
    name: String,
    email: String,
    password: String
)

final case class UpdateUser(
    id: Int,
    name: String,
    email: String,
    password: String
)

object UserDAO extends Database {
  def selectById(id: Int): IO[Option[User]] = {
    sql"SELECT * FROM users WHERE id = $id"
      .query[User]
      .option
      .transact(xa)
  }

  def selectAll(): IO[List[User]] = {
    sql"SELECT * FROM users"
      .query[User]
      .to[List]
      .transact(xa)
  }

  def insert(newUser: NewUser): IO[Int] = {
    sql"""
      INSERT INTO users (name, email, password) 
      VALUES (${newUser.name}, ${newUser.email}, ${newUser.password})
    """.update
      .withUniqueGeneratedKeys[Int]("id")
      .transact(xa)
  }

  def update(updateUser: UpdateUser): IO[Int] = {
    sql"""
      UPDATE users 
      SET name = ${updateUser.name}, 
          email = ${updateUser.email}, 
          password = ${updateUser.password} 
      WHERE id = ${updateUser.id}
      """.update.run
      .transact(xa)
  }
}
