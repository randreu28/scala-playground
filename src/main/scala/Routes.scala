package main

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.ContentTypes

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

  val helloRoute: Route =
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

  val allRoutes: Route = rootRoute ~ helloRoute
}
