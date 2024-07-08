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
        res <- Ok(users.asJson)
      } yield res

    case GET -> Root / "users" / id =>
      id.toIntOption match {
        case Some(validId) =>
          for {
            user <- UserService.getUserById(validId)
            res <- user match {
              case Some(user) => Ok(user.asJson)
              case None       => NotFound(s"User with id $validId not found")
            }
          } yield res
        case None => BadRequest("Invalid user ID")
      }

    case req @ POST -> Root / "users" =>
      req.as[NewUser].attempt.flatMap {
        case Right(newUser) =>
          UserService.createUser(newUser).attempt.flatMap {
            case Right(user)           => Ok(user.asJson)
            case Left(error: ApiError) => toErrorResponse(error)
            // Type system is not smart enough to figure out there cannot be any other error than ApiError
            case _ => InternalServerError("Unknown error")
          }
        case Left(_) => BadRequest("Invalid JSON for NewUser")
      }

    case req @ PATCH -> Root / "users" =>
      req.as[UpdateUser].attempt.flatMap {
        case Right(updatedUser) =>
          UserService.updateUser(updatedUser).attempt.flatMap {
            case Right(user)           => Ok(user.asJson)
            case Left(error: ApiError) => toErrorResponse(error)
            case _                     => InternalServerError("Unknown error")
          }
        case Left(_) => BadRequest("Invalid JSON for UpdateUser")
      }
  }

  private def toErrorResponse(error: ApiError): IO[Response[IO]] = {
    IO[Response[IO]](
      Response[IO](Status(error.status)).withEntity(error.message)
    )
  }
}
