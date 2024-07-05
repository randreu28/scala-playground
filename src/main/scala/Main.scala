package Main

import cats.effect.*
import com.comcast.ip4s.*
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.*
import org.http4s.server.Router
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.Logger

object Main extends IOApp {
  implicit def logger[F[_]: Sync]: Logger[F] = Slf4jLogger.getLogger[F]

  val helloWorldService = HttpRoutes.of[IO] { case GET -> Root / "hello" =>
    Ok("Hello, World!")
  }

  // TODO: Create the actual routes with the services
  // TODO: Add logging to slf4j
  val httpApp = Router("/" -> helloWorldService).orNotFound

  override def run(args: List[String]): IO[ExitCode] = {
    val port = args.headOption.map(_.toInt).getOrElse(3000)
    println(s"Starting server on port $port")
    EmberServerBuilder
      .default[IO]
      .withHost(Host.fromString("localhost").get)
      .withPort(Port.fromInt(port).get)
      .withHttpApp(httpApp)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)
      .guarantee(IO(println("Shutting down server")))
  }
}
