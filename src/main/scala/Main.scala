import scala.io.Source
import scala.util.{Try, Success, Failure}
import java.net.{HttpURLConnection, URL}

@main
def main(): Unit =
  val url = new URL("https://rickandmortyapi.com/api/character")
  val response = fetchResponse(url)
  response match {
    case Success((statusCode, content)) => {
      println(s"Status Code: $statusCode")
      println(content)
    }
    case Failure(exception) => println(exception.getMessage)
  }

def fetchResponse(url: URL): Try[(Int, String)] = Try {
  val connection = url.openConnection().asInstanceOf[HttpURLConnection]
  connection.setRequestMethod("GET")
  val statusCode = connection.getResponseCode
  val content = Source.fromInputStream(connection.getInputStream).mkString
  connection.disconnect()

  (statusCode, content)
}
