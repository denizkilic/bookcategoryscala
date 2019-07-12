package example.denizkilic.bookcategoryapi
package impl

import Models._
import cats.effect.Sync
import cats.{ApplicativeError, Monad}
import io.circe.Json

import scala.util.Try
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl


object BookCategoryHttpAPI {
 object JsonProtocols {
    import io.circe._
    import io.circe.generic.semiauto._
    implicit val bookIdEncoder: Encoder[BookId] = id => Json.fromString(id.value.toString)
    implicit val bookItemEncoder: Encoder[BookItem] = deriveEncoder
  }

  def create[F[_] : Monad : Sync](bookService: BookCategoryStorage[F])(implicit http4sDsl: Http4sDsl[F], ev: ApplicativeError[F, Throwable]): HttpRoutes[F] = {
    import cats.syntax.all._
    import io.circe.syntax._
    import JsonProtocols._
    import http4sDsl._

    val BookIdPath = new Object {
      def unapply(arg: String): Option[BookId] = Try(arg.toInt).toOption.map(BookId)
    }

    HttpRoutes.of[F] {
      case GET -> Root / "books" =>
        for {
          items <- bookService.getBooks
          resp <- Ok(items.asJson)
        } yield resp

      case req @ POST -> Root / "addBook" =>
        for {
          bookName <- req.as[String]
          bookType <- req.as[String]
          bookDescription <- req.as[String]
          maybeId <- bookService.addBook(bookName, bookType, bookDescription).attempt
          resp <- maybeId match {
            case Right(id) => Ok(Json.fromFields(("id" -> id.asJson) :: Nil))
            case Left(err) => InternalServerError("")
          }
        } yield resp

      case DELETE -> Root / "book" / BookIdPath(id) =>
        for {
          _ <- bookService.deleteBook(id)
          resp <- Ok("book was deleted")
        } yield resp

      case PATCH -> Root / "updateBook" / BookIdPath(id) =>
        for {
//          _ <- bookService.updateBook(id, bookName, bookType, bookDestination)
          resp <- Ok("book was updated")
        } yield resp
    }
  }
}
