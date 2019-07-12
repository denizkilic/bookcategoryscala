package example.denizkilic.bookcategoryapi.impl

import cats.effect.Sync
import cats.{Functor, Monad}
import cats.effect.concurrent.Ref
import example.denizkilic.bookcategoryapi.BookCategoryStorage
import example.denizkilic.bookcategoryapi.Models.{BookId, BookItem}

private class InMemoryBookCategoryStorage[F[_] : Monad](bookListState: Ref[F, List[BookItem]], getNextId: F[BookId]) extends BookCategoryStorage[F] {
  import cats.syntax.all._

  def getBooks: F[List[BookItem]] = bookListState.get

  def addBook(bookName: String, bookType: String, bookDescription: String): F[BookId] = {
    for {
      id <- getNextId
      _ <- bookListState.update(BookItem(id, bookName, bookType, bookDescription) :: _)
    } yield id
  }

  def updateBook(id: BookId, bookName: String, bookType: String, bookDescription: String): F[BookId] = {
    for {
      id <- getNextId
      _ <- bookListState.update(BookItem(id, bookName, bookType, bookDescription) :: _)
    } yield id
  }

  def deleteBook(id: BookId): F[Option[Error]] = bookListState.modify { state =>
    val newState =
      state
        .filter {
          case BookItem(`id`, _, _, _) => false
          case _ => true
        }
    (newState, None)
  }
}

/**
  * InMemoryTodoStorage.create is a side effectful operation which creates some shared state (initial value can be specified in arguments)
  * And this is why only factory methods are exposed, not class with implementation
  */
object InMemoryBookCategoryStorage {
  def create[F[_] : Functor : Sync](): F[BookCategoryStorage[F]] = create(Nil, BookId(1), { case BookId(v) => BookId(v+1) })

  def create[F[_] : Functor : Sync](
                                     initialState: List[BookItem],
                                     nextId: BookId,
                                     idGenerator: BookId => BookId
                                   ): F[BookCategoryStorage[F]] = {
    import cats.syntax.flatMap._
    import cats.syntax.functor._

    for {
      idGeneratorState <- Ref.of(nextId)
      bookListState <- Ref.of(initialState)
    } yield {
      val getNextId = idGeneratorState.modify(currId => (idGenerator(currId), currId))
      new InMemoryBookCategoryStorage(bookListState, getNextId)
    }
  }
}

