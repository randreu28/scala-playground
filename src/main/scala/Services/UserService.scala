package Services

import DAOs.NewUser
import DAOs.UpdateUser
import DAOs.User
import DAOs.UserDAO
import zio._
import org.mindrot.jbcrypt.BCrypt
import DAOs.UserDAO.update
import java.sql.SQLException

case class ApiError(message: String, statusCode: Int)

object UserService {
  def getUserById = UserDAO.selectById
  def getUsers = UserDAO.selectAll

  def createUser(user: NewUser): ZIO[javax.sql.DataSource, ApiError, Int] = {
    for {
      _ <- validateEmail(user.email)
      hashedPassword = hashPassword(user.password)
      hashedUser = user.copy(password = hashedPassword)
      id <- UserDAO
        .insert(hashedUser)
        .mapError(handleSqlException)
    } yield id
  }

  def updateUser(
      updateUser: UpdateUser
  ): ZIO[javax.sql.DataSource, ApiError, Long] = {
    for {
      userOption <- getUserById(updateUser.id).mapError(handleSqlException)
      prevUser <- getUserOrFail(userOption)
      _ <- validateEmail(prevUser.email)
      _ <- validateNewPassword(prevUser.password, updateUser.password)

      hashedPassword = hashPassword(updateUser.password)
      hashedUser = updateUser.copy(password = hashedPassword)

      affectedRows <- UserDAO
        .update(hashedUser)
        .mapError(handleSqlException)
    } yield affectedRows
  }

  private def getUserOrFail(
      userOption: Option[User]
  ): ZIO[Any, ApiError, User] = {
    userOption match {
      case None        => ZIO.fail(new ApiError("User not found", 404))
      case Some(value) => ZIO.succeed(value)
    }
  }

  private def hashPassword(password: String): String = {
    BCrypt.hashpw(password, BCrypt.gensalt())
  }

  private def validateNewPassword(
      prevPassword: String,
      newPassword: String
  ): ZIO[Any, ApiError, Unit] = {
    if (BCrypt.checkpw(newPassword, prevPassword)) {
      return ZIO.fail(
        new ApiError(
          "New password must be different from the current one",
          400
        )
      )
    }

    ZIO.unit
  }

  private def validateEmail(email: String): ZIO[Any, ApiError, Unit] = {
    if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
      return ZIO.fail(new ApiError("Invalid email format", 400))
    }

    ZIO.unit
  }

  // This you couldn't really do with IO! It was simply a 'ghost error'
  private def handleSqlException(e: SQLException): ApiError = e match {
    case sqlEx: java.sql.SQLException if sqlEx.getSQLState == "23505" =>
      new ApiError("Email already exists", 400)
    case _ => ApiError(e.getMessage, 500)
  }
}
