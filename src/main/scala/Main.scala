package Main

import Routes.*
import Utils.Utils.httpLogs
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route

import scala.io.StdIn

@main
def main(args: String*) = {
  val port = args.headOption.flatMap(_.toIntOption).getOrElse(3000)

  implicit val system = ActorSystem(Behaviors.empty, "main")
  implicit val executionContext = system.executionContext

  val httpServer = Http()
    .newServerAt("localhost", port)
    .bind {
      logRequest(httpLogs) {
        Routes.allRoutes
      }
    }

  println(s"Server running at http://localhost:$port")
  println("You can check the logs in the .logs directory")
  println("Press Enter to stop")
  StdIn.readLine() // Wait for the user to press Enter

  httpServer
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
