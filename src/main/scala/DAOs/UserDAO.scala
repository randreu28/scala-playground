package DAOs

import io.getquill._

final case class User(id: Int, name: String, email: String, password: String)
final case class NewUser(name: String, email: String, password: String)
final case class UpdateUser(
    id: Int,
    name: Option[String],
    email: Option[String],
    password: Option[String]
)

object UserDAO extends Database {
  import ctx._

  def selectAll = ctx.run {
    query[User]
  }

  def selectById(id: Int) = ctx
    .run {
      query[User]
        .filter(u => u.id == lift(id))
    }
    .map(_.headOption)

  def insert(user: NewUser) = ctx.run {
    query[User]
      .insert(
        _.name -> lift(user.name),
        _.password -> lift(user.password),
        _.email -> lift(user.email)
      )
      .returningGenerated(_.id)
  }

  def update(user: UpdateUser) = ctx.run {
    dynamicQuery[User]
      .filter(u => u.id == lift(user.id))
      .update(
        setOpt(u => u.name, user.name),
        setOpt(u => u.password, user.password),
        setOpt(u => u.email, user.email)
      )
  }

}
