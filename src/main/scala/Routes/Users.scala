package Routes

import DAOs.NewUser
import DAOs.UpdateUser
import DAOs.User
import Main.Main.logger
import Services.ApiError
import Services.UserService
import cats.effect.IO
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.Headers
import org.http4s.HttpRoutes
import org.http4s.HttpVersion
import org.http4s.Response
import org.http4s.Status
import org.http4s.circe.CirceEntityDecoder.*
import org.http4s.circe.CirceEntityEncoder.*
import org.http4s.dsl.io.*

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

    /** TODO: Error handling is nonexistent. Something goes wrong? Internal
      * server error(500). Does not even get logged...
      */
    case req @ POST -> Root / "users" =>
      for {
        newUser <- req.as[NewUser]
        user <- UserService.createUser(newUser)
        res <- Ok(user.asJson)
      } yield res

    case req @ PATCH -> Root / "users" =>
      for {
        updatedUser <- req.as[UpdateUser]
        user <- UserService.updateUser(updatedUser)
        res <- Ok(user.asJson)
      } yield res
  }
}
