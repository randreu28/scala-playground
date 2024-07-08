package Utils

import cats.data.Kleisli
import cats.effect.*
import org.http4s.*
import org.typelevel.log4cats.Logger

object Logger {
  def logInfo(
      req: Request[IO],
      res: Response[IO]
  )(implicit logger: Logger[IO]): IO[Unit] = {
    val method = req.method
    val path = req.uri.path
    val statusCode = res.status.code
    logger.info(s"$method $path | $statusCode")
  }

  def apply(service: HttpApp[IO])(implicit
      logger: Logger[IO]
  ): HttpApp[IO] =
    Kleisli { req =>
      service(req).flatTap(res => logInfo(req, res))
    }
}
