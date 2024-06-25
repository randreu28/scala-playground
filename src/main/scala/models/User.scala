package Models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol
import spray.json.RootJsonFormat

case class User(id: Int, email: String, password: String, createdAt: String)

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val userFormat: RootJsonFormat[User] = jsonFormat4(User.apply)
  implicit val usersFormat: RootJsonFormat[List[User]] = listFormat[User]
}
