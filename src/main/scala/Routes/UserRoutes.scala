package Routes

import zio._
import zio.http._
import Services._
import Services.UserService._
import DAOs.User
import zio.json._
import DAOs.NewUser

val userRoutes =
  Routes(
    Method.GET / "users" -> handler {
      getUsers.map(users => Response.json(users.toJson))
    },
    Method.GET / "users" / int("id") -> handler { (id: Int, req: Request) =>
      getUserById(id).map {
        case Some(user) => Response.json(user.toJson)
        case None       => Response.status(Status.NotFound)
      }
    },
    Method.POST / "users" -> handler { (req: Request) => ??? },
    Method.PUT / "users" -> handler { (req: Request) => ??? }
  ).handleError((apiError) => {
    val status = Status.fromInt(apiError.statusCode)
    Response(status, Headers.empty, Body.fromString(apiError.message))
  })
