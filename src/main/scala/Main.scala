package Main

import cats.effect.*
import com.comcast.ip4s.*
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.ember.server.EmberServerBuilder
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.Logger

object Main extends IOApp {
  implicit val logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  // TODO: Create the actual routes with the services
  val helloWorldService = HttpRoutes.of[IO] {
    case GET -> Root / "hello"        => Ok("Hello, World!")
    case GET -> Root / "hello" / name => Ok(s"Hello, $name!")
  }

  val httpApp = Utils.Logger(helloWorldService.orNotFound)

  override def run(args: List[String]): IO[ExitCode] = {
    val port = args.headOption.map(_.toInt).getOrElse(3000)
    EmberServerBuilder
      .default[IO]
      .withHost(Host.fromString("localhost").get)
      .withPort(Port.fromInt(port).get)
      .withHttpApp(httpApp)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}
