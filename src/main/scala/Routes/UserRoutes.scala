package Routes

import zio._
import zio.http._
import Services._
import Services.UserService
import DAOs.User
import zio.json._
import DAOs.NewUser
import DAOs.UpdateUser

val userRoutes =
  Routes(
    Method.GET / "users" -> handler {
      UserService.getUsers.map(users => Response.json(users.toJson))
    },
    Method.GET / "users" / int("id") -> handler { (id: Int, req: Request) =>
      UserService.getUserById(id).map {
        case Some(user) => Response.json(user.toJson)
        case None       => Response.status(Status.NotFound)
      }
    },
    Method.POST / "users" -> handler { (req: Request) =>
      for {
        newUser <- req.body.toString().fromJson[NewUser] match {
          case Left(_)        => ZIO.fail(ApiError("Invalid JSON", 400))
          case Right(newUser) => ZIO.succeed(newUser)
        }
        userId <- UserService.createUser(newUser)
      } yield Response.text(s"User created with ID: $userId")
    },
    Method.PUT / "users" -> handler { (req: Request) =>
      for {
        user <- req.body.toString().fromJson[UpdateUser] match {
          case Left(_)     => ZIO.fail(ApiError("Invalid JSON", 400))
          case Right(user) => ZIO.succeed(user)
        }
        _ <- UserService.updateUser(user)
      } yield Response.ok
    }
  ).handleError((apiError) => {
    val status = Status.fromInt(apiError.statusCode)
    Response(status, Headers.empty, Body.fromString(apiError.message))
  })
