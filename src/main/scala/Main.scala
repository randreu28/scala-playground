package main

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import scala.io.StdIn

@main
def main(args: String*): Unit = {
  val port = if (args.nonEmpty) args(0).toInt else 3000

  implicit val system = ActorSystem(Behaviors.empty, "my-system")
  implicit val executionContext = system.executionContext

  val bindingFuture =
    Http().newServerAt("localhost", port).bind(Routes.allRoutes)

  println(s"Server running at http://localhost:$port")
  println("Press Enter to stop")

  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
