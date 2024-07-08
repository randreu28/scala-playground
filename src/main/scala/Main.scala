import zio._
import zio.http._

object Main extends ZIOAppDefault {
  val routes =
    Routes(
      Method.GET / Root -> handler(Response.text("Hello world")),
      Method.GET / "users" / int("id") ->
        handler { (id: Int, req: Request) =>
          Response.text(s"Requested User ID: $id")
        }
    )

  def run = Server.serve(routes).provide(Server.defaultWithPort(3000))
}
