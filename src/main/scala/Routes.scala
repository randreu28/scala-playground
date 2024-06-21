package Routes

import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route
import Persistence.DatabaseModule.*

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Routes {
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
        complete(
          HttpEntity(
            ContentTypes.`application/json`,
            users.toString // TODO: return actual JSON
          )
        )
      }
    }

  val userRoute: Route =
    path("user" / IntNumber) { id =>
      get {
        val user = getUserById(id)
        complete(
          HttpEntity(
            ContentTypes.`application/json`,
            user.toString // TODO: return actual JSON
          )
        )
      }
    }

  val allRoutes: Route = rootRoute ~ usersRoute ~ userRoute
}
