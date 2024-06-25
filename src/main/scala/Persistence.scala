package Persistence

import Models.User
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol
import spray.json.RootJsonFormat

import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement
import java.time.LocalDateTime

object DatabaseModule {
  val dbUrl = "jdbc:sqlite:sqlite.db"

  def connect(): Connection = {
    Class.forName("org.sqlite.JDBC")
    DriverManager.getConnection(dbUrl)
  }

  def initializeDatabase(): Unit = {
    val connection = connect()
    val statement = connection.createStatement()
    statement.execute("""
        |CREATE TABLE IF NOT EXISTS users (
        |  id INTEGER PRIMARY KEY AUTOINCREMENT,
        |  email TEXT NOT NULL,
        |  password TEXT NOT NULL,
        |  createdAt TEXT NOT NULL
        |);
        |""".stripMargin)
    statement.close()
    connection.close()
  }

  def getUsers(): List[User] = {
    val connection = connect()
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery("SELECT * FROM users")
    val users = Iterator
      .continually((resultSet, resultSet.next()))
      .takeWhile(_._2)
      .map { case (rs, _) => User.fromResultSet(rs) }
      .toList
    resultSet.close()
    statement.close()
    connection.close()
    users
  }

  def getUserById(id: Int): Option[User] = {
    val connection = connect()
    val statement =
      connection.prepareStatement("SELECT * FROM users WHERE id = ?")
    statement.setInt(1, id)
    val resultSet = statement.executeQuery()
    val user = if (resultSet.next()) {
      Some(User.fromResultSet(resultSet))
    } else {
      None
    }
    resultSet.close()
    statement.close()
    connection.close()
    user
  }

  def createUser(email: String, password: String): Unit = {
    val connection = connect()
    val statement = connection.prepareStatement(
      "INSERT INTO users (email, password, createdAt) VALUES (?, ?, ?)"
    )
    if (!User.validateEmail(email)) {
      throw new IllegalArgumentException("Invalid email")
    }
    if (!User.validatePassword(password)) {
      throw new IllegalArgumentException("Invalid password")
    }

    statement.setString(1, email)
    statement.setString(2, password)
    statement.setString(3, LocalDateTime.now().toString)
    statement.executeUpdate()
    statement.close()
    connection.close()
  }

  def isUserPasswordSame(id: Int, newPassword: String): Boolean = {
    val connection = connect()
    val checkStatement =
      connection.prepareStatement("SELECT password FROM users WHERE id = ?")
    checkStatement.setInt(1, id)
    val checkResultSet = checkStatement.executeQuery()
    val isSame = if (checkResultSet.next()) {
      val currentPassword = checkResultSet.getString("password")
      currentPassword.equals(newPassword)
    } else {
      false
    }
    checkResultSet.close()
    checkStatement.close()
    connection.close()
    isSame
  }

  def updateUser(id: Int, email: String, password: String): Unit = {
    val connection = connect()
    if (isUserPasswordSame(id, password)) {
      throw new IllegalArgumentException(
        "New password is the same as the old one"
      )
    }

    val updateStatement = connection.prepareStatement(
      "UPDATE users SET email = ?, password = ? WHERE id = ?"
    )
    if (!User.validateEmail(email)) {
      throw new IllegalArgumentException("Invalid email")
    }

    if (!User.validatePassword(password)) {
      throw new IllegalArgumentException("Invalid password")
    }

    updateStatement.setString(1, email)
    updateStatement.setString(2, password)
    updateStatement.setInt(3, id)
    updateStatement.executeUpdate()
    updateStatement.close()
    connection.close()
  }
}
