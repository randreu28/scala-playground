import zio._
import zio.http._
import _root_.Routes.userRoutes
import DAOs.sqliteDB

object Main extends ZIOAppDefault {
  def run = Server
    .serve(userRoutes)
    .provide(
      Server.defaultWithPort(3000),
      sqliteDB
    )
}
