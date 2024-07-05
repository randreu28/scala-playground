package Services

import DAOs.NewUser
import DAOs.UpdateUser
import DAOs.User
import DAOs.UserDAO
import cats.effect.IO
import org.mindrot.jbcrypt.BCrypt

//TODO: Use different throwables type
object UserService {
  def createUser(user: NewUser): IO[Int] = {
    for {
      _ <- validateEmail(user.email)
      hashedPassword = hashPassword(user.password)
      hashedUser = user.copy(password = hashedPassword)
      affectedRows <- UserDAO.insert(hashedUser)
    } yield affectedRows
  }

  def getUserById = UserDAO.selectById
  def getUsers() = UserDAO.selectAll()

  def updateUser(updateUser: UpdateUser): IO[Int] = {
    for {
      userOption <- getUserById(updateUser.id)
      user <- validateUserExists(userOption)
      _ <- validateEmail(updateUser.email)
      _ <- validateNewPassword(user.password, updateUser.password)
      hashedPassword = hashPassword(updateUser.password)
      updatedUser = updateUser.copy(password = hashedPassword)
      affectedRows <- UserDAO.update(updatedUser)
    } yield affectedRows
  }

  private def hashPassword(password: String): String = {
    BCrypt.hashpw(password, BCrypt.gensalt())
  }

  private def validateNewPassword(
      prevPassword: String,
      newPassword: String
  ): IO[Unit] = {
    if (BCrypt.checkpw(newPassword, prevPassword)) {
      return IO.raiseError(
        new IllegalArgumentException(
          "New password must be different from the current one"
        )
      )
    }

    IO.unit
  }

  private def validateEmail(email: String): IO[Unit] = {
    if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
      return IO.raiseError(new IllegalArgumentException("Invalid email format"))
    }

    IO.unit
  }

  private def validateUserExists(userOption: Option[User]): IO[User] = {
    userOption match {
      case Some(u) => IO.pure(u)
      case None => IO.raiseError(new IllegalArgumentException("User not found"))
    }
  }
}
