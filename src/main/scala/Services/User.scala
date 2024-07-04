package Services

import DAOs.UserDAO
import cats.effect.IO
import org.mindrot.jbcrypt.BCrypt
import DAOs.User

object UserService {
  def validateEmail(email: String): Boolean = {
    email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
  }

  def hashPassword(password: String): String = {
    BCrypt.hashpw(password, BCrypt.gensalt())
  }

  def createUser(user: User): IO[Either[String, Int]] = {
    if (!validateEmail(user.email)) {
      return IO.pure(Left("Invalid email format"))
    }

    val hashedUser = user.copy(password = hashPassword(user.password))
    UserDAO.insert(hashedUser).map(Right(_))
  }

  def getUserById(id: Int): IO[Option[User]] = {
    UserDAO.selectById(id)
  }

  def getUsers: IO[List[User]] = ???
  def updateUser(id: Int, user: User): IO[Option[User]] = ???
}
