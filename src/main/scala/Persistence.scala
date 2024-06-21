package Persistence

import java.sql.{Connection, DriverManager, Statement}
import java.time.LocalDateTime

object DatabaseModule {
  val dbUrl = "jdbc:sqlite:sqlite.db"

  def connect(): Connection = {
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
}
