class Json

object Method {
  val GET = new Method("GET")
  val PUT = new Method("GET")
}

class Method(val name: String)

class Request[T](uri: String, method: Method, entityType: Class[T])

class Response[T](val status: Int, val value: T)

class Client1 {
  def execute[T](request: Request[T]): Response[T] = ???
}

object Main extends App {
  val client = new Client1()
  val response: Response[Json] = client.execute(new Request("http://example.com", Method.GET, classOf[Json]))
}
