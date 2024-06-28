package Routes

import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Routes {
  val rootRoute =
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

  val helloRoute =
    path("hello") {
      get {
        complete(
          HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            "<h1>Hello from Akka!</h1>"
          )
        )
      }
    }

  val allRoutes = rootRoute ~ helloRoute
}
