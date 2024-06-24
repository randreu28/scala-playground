package Main

import Persistence.DatabaseModule.*
import Routes.*
import Utils.Utils.*
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route

import scala.io.StdIn

@main
def main(args: String*): Unit = {
  val port = if (args.nonEmpty) args(0).toInt else 3000

  implicit val system = ActorSystem(Behaviors.empty, "main")
  implicit val executionContext = system.executionContext

  val route: Route = logRequest(httpLogs) {
    Routes.allRoutes
  }

  initializeDatabase()

  val bindingFuture =
    Http()
      .newServerAt("localhost", port)
      .bind(route)

  printf(s"Server running at http://localhost:$port\n")
  printf("You can check the logs in the .logs directory\n")
  printf("Press Enter to stop\n")

  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
