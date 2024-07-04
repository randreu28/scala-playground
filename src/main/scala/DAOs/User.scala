package DAOs

import doobie._
import doobie.implicits._
import cats.effect.IO
import cats.implicits._

sealed trait UserBase {
  def name: String
  def email: String
  def password: String
}

final case class User(
    id: Int,
    name: String,
    email: String,
    password: String
) extends UserBase

final case class NewUser(
    name: String,
    email: String,
    password: String
) extends UserBase

final case class UpdateUser(
    id: Int,
    name: Option[String],
    email: Option[String],
    password: Option[String]
)

object UserDAO extends Database {
  def selectById(id: Int): IO[Option[User]] = {
    sql"SELECT * FROM users WHERE id = $id"
      .query[User]
      .option
      .transact(xa)
  }

  def selectAll: IO[List[User]] = {
    sql"SELECT * FROM users"
      .query[User]
      .to[List]
      .transact(xa)
  }

  def insert(newUser: NewUser): IO[Int] = {
    sql"INSERT INTO users (name, email, password) VALUES (${newUser.name}, ${newUser.email}, ${newUser.password})".update
      .withUniqueGeneratedKeys[Int]("id")
      .transact(xa)
  }

  def update(updateUser: UpdateUser): IO[Int] = {
    val query = fr"UPDATE users SET" ++
      updateUser.name.map(name => fr"name = $name").getOrElse(fr"") ++
      updateUser.email.map(email => fr", email = $email").getOrElse(fr"") ++
      updateUser.password
        .map(password => fr", password = $password")
        .getOrElse(fr"") ++
      fr"WHERE id = ${updateUser.id}"

    query.update.run.transact(xa)
  }
}
