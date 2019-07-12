package example.denizkilic

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.dsl.Http4sDsl
import org.http4s.server.blaze.BlazeBuilder

import bookcategoryapi.impl.{InMemoryBookCategoryStorage, BookCategoryHttpAPI}

object BookCategoryAPIServer  extends IOApp {

  implicit val http4sDsl: Http4sDsl[IO] = Http4sDsl[IO]

  override def run(args: List[String]): IO[ExitCode] = {
    for {
      storage <- InMemoryBookCategoryStorage.create[IO]()
      _ <- {
        BlazeBuilder[IO]
          .bindHttp(8080, "localhost")
          .mountService(BookCategoryHttpAPI.create(storage), "/api")
          .serve
          .compile
          .drain
      }
    } yield ExitCode.Success
  }


}
