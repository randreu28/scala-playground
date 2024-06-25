package Routes

import Models.JsonSupport
import Persistence.DatabaseModule.*
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Routes extends JsonSupport {
  val rootRoute: Route =
    path("") {
      get {
        val currentDate =
          LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        complete(
          HttpEntity(
            ContentTypes.`application/json`,
            s"""{"date": "$currentDate", "status": "ok"}"""
          )
        )
      }
    }

  val usersRoute: Route =
    path("users") {
      get {
        val users = getUsers()
        complete(users)
      }
    }

  val userRoute: Route =
    path("user" / IntNumber) { id =>
      val user = getUserById(id)
      user match {
        case Some(u) => complete(u)
        case None    => complete(StatusCodes.NotFound)
      }
    }

  val allRoutes: Route = rootRoute ~ usersRoute ~ userRoute
}
