package DAOs

import io.getquill._

case class User(id: Int, name: String, email: String, password: String)

object UserDAO extends Database {
  import ctx._

  def selectAll = ctx.run(query[User])

  def selectById(id: Int) = ctx.run(
    quote {
      query[User]
        .filter(u => u.id == lift(id))
    }
  )

  def insert(user: User) = ctx.run(
    query[User]
      .insertValue(lift(user))
  )

  def update(user: User) = ctx
    .run(
      query[User]
        .filter(u => u.id == lift(user.id))
        .updateValue(lift(user))
    )

}
