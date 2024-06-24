package Persistence

import java.sql.{Connection, DriverManager, Statement}
import java.time.LocalDateTime

case class User(id: Int, email: String, password: String, createdAt: String)

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
      .map { case (rs, _) =>
        User(
          rs.getInt("id"),
          rs.getString("email"),
          rs.getString("password"),
          rs.getString("createdAt")
        )
      }
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
      Some(
        User(
          resultSet.getInt("id"),
          resultSet.getString("email"),
          resultSet.getString("password"),
          resultSet.getString("createdAt")
        )
      )
    } else {
      None
    }
    resultSet.close()
    statement.close()
    connection.close()
    user
  }

  // TODO: Validate email and password
  def createUser(email: String, password: String): Unit = {
    val connection = connect()
    val statement = connection.prepareStatement(
      "INSERT INTO users (email, password, createdAt) VALUES (?, ?, ?)"
    )
    statement.setString(1, email)
    statement.setString(2, password)
    statement.setString(3, LocalDateTime.now().toString)
    statement.executeUpdate()
    statement.close()
    connection.close()
  }

  // TODO: Don't let user update if password is the same as last one
  def updateUser(id: Int, email: String, password: String): Unit = {
    val connection = connect()
    val statement = connection.prepareStatement(
      "UPDATE users SET email = ?, password = ? WHERE id = ?"
    )
    statement.setString(1, email)
    statement.setString(2, password)
    statement.setInt(3, id)
    statement.executeUpdate()
    statement.close()
    connection.close()
  }
}
