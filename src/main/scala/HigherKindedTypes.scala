class Json

object Method {
  val GET = new Method("GET")
  val DELETE = new Method("DELETE")
}

class Method(val name: String)

class Request[T](
  uri: String,
  method: Method,
  entityType: Class[T]
)

class Response[T](
  val status: Int,
  val value: T
)

object Types {
  type Is[T] = T
  type ResponseEitherString[T] = Response[Either[String, T]]
}

trait BaseClient[Result[_]] {
  def execute[T](request: Request[T]): Result[T]
}

class Client1 extends BaseClient[Response] {
  def execute[T](request: Request[T]): Response[T] = ???
}

class Client2(delegate: Client1 = new Client1) extends BaseClient[Types.Is] {
  def execute[T](request: Request[T]): T = delegate.execute(request).value
}

class Client3 extends BaseClient[Types.ResponseEitherString] {
  def execute[T](request: Request[T]): Response[Either[String, T]] = ???
}

class ConvenientClient[Result[_]](delegate: BaseClient[Result]) {
  def get[T](uri: String, entityType: Class[T]) = delegate.execute(new Request(uri, Method.GET, entityType))
  def delete[T](uri: String, entityType: Class[T]) = delegate.execute(new Request(uri, Method.DELETE, entityType))
}

object Main extends App {

  val client1 = new ConvenientClient(new Client1)
  val client3 = new ConvenientClient(new Client3)
  val client2 = new ConvenientClient(new Client2)

  val response1: Response[Json]                 = client1.get("http://example.com", classOf[Json])
  val response2: Json                           = client2.get("http://example.com", classOf[Json])
  val response3: Response[Either[String, Json]] = client3.get("http://example.com", classOf[Json])
}
