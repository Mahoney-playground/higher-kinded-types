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

class Client2(delegate: Client1) {
  def execute[T](request: Request[T]): T = delegate.execute(request).value
}

class ConvenientClient1(delegate: Client1) {
  def get[T](uri: String, entityType: Class[T]) = delegate.execute(new Request(uri, Method.GET, classOf[Json]))
}

object Main extends App {
  val client = new Client2(new Client1)
  val response: Json = client.execute(new Request("http://example.com", Method.GET, classOf[Json]))

  val convenientClient1 = new ConvenientClient1(new Client1)
  val response2: Response[Json] = convenientClient1.get("http://example.com", classOf[Json])
}
