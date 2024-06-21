package Utils

import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.directives.LogEntry
import org.slf4j.LoggerFactory

object Utils {
  val log = LoggerFactory.getLogger("CLFLogger")

  def httpLogs(request: HttpRequest): LogEntry = {
    val requestLine =
      s"${request.method.value} ${request.uri} ${request.protocol.value}"

    LogEntry(requestLine, akka.event.Logging.InfoLevel)
  }
}
