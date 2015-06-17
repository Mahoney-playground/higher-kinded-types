class Json

object Method {
  val GET = new Method("GET")
  val DELETE = new Method("DELETE")
}

class Method(val name: String)

class Request[T](uri: String, method: Method, entityType: Class[T])

class Response[T](val status: Int, val value: T)

trait BaseClient[Result[_]] {
  def execute[T](request: Request[T]): Result[T]
}

class Client1 extends BaseClient[Response] {
  override def execute[T](request: Request[T]): Response[T] = ???
}

object Client2 {
  type Is[T] = T
}

class Client2(delegate: Client1) extends BaseClient[Client2.Is] {
  def execute[T](request: Request[T]): T = delegate.execute(request).value
}

object Client3 {
  type ResponseEitherString[T] = Response[Either[String, T]]
}

class Client3 extends BaseClient[Client3.ResponseEitherString] {
  def execute[T](request: Request[T]): Response[Either[String, T]] = ???
}

class ConvenientClient[Result[_]](delegate: BaseClient[Result]) {
  def get[T](uri: String, entityType: Class[T]) = delegate.execute(new Request(uri, Method.GET, classOf[Json]))
  def delete[T](uri: String, entityType: Class[T]) = delegate.execute(new Request(uri, Method.DELETE, classOf[Json]))
}

object Main extends App {

  val convenientClient1 = new ConvenientClient(new Client1)
  val response1: Response[Json] = convenientClient1.get("http://example.com", classOf[Json])

  val convenientClient2 = new ConvenientClient(new Client2(new Client1))
  val response2: Json = convenientClient2.get("http://example.com", classOf[Json])

  val convenientClient3 = new ConvenientClient(new Client3)
  val response3: Response[Either[String, Json]] = convenientClient3.get("http://example.com", classOf[Json])
}
