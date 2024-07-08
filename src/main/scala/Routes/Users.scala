package Routes

import org.http4s.HttpRoutes
import cats.effect.IO
import org.http4s.dsl.io.*
import org.http4s.circe.CirceEntityEncoder.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import Services.UserService

object Users {
  val routes = HttpRoutes.of[IO] {
    case GET -> Root / "users" =>
      for {
        users <- UserService.getUsers()
        response <- Ok(users.asJson)
      } yield response

    case GET -> Root / "users" / id =>
      id.toIntOption match {
        case Some(validId) =>
          for {
            user <- UserService.getUserById(validId)
            response <- user match {
              case Some(user) => Ok(user.asJson)
              case None       => NotFound(s"User with id $validId not found")
            }
          } yield response
        case None => BadRequest("Invalid user ID")
      }
  }
}
