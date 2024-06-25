package Models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol
import spray.json.DeserializationException
import spray.json.JsString
import spray.json.JsValue
import spray.json.JsonFormat
import spray.json.RootJsonFormat

import java.text.SimpleDateFormat
import java.util.Date
import scala.util.matching.Regex

case class User(id: Int, email: String, password: String, createdAt: Date)
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val dateFormat: JsonFormat[Date] = new JsonFormat[Date] {
    def write(date: Date) = JsString(User.dateFormat.format(date))
    def read(json: JsValue) = json match {
      case JsString(dateString) =>
        User.dateFormat.parse(dateString)
      case _ => throw new DeserializationException("Date expected")
    }
  }

  implicit val userFormat: RootJsonFormat[User] = jsonFormat4(User.apply)
  implicit val usersFormat: RootJsonFormat[List[User]] = listFormat[User]
}

object User {
  val emailRegex =
    """^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$""".r
  val passwordRegex =
    """^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*_).{6,}$""".r

  val dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

  def validateEmail(email: String): Boolean = emailRegex.matches(email)
  def validatePassword(password: String): Boolean =
    passwordRegex.matches(password)

  def fromResultSet(rs: java.sql.ResultSet): User = {
    User(
      rs.getInt("id"),
      rs.getString("email"),
      rs.getString("password"),
      dateFormat.parse(rs.getString("createdAt"))
    )
  }
}
