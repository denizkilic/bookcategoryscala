package example.denizkilic.bookcategoryapi

import Models._

trait BookCategoryStorage[F[_]] {
  def getBooks: F[List[BookItem]]
  def addBook(bookName: String, bookType: String, bookDescription: String): F[BookId]
  def updateBook(id: BookId, bookName: String, bookType: String, bookDescription: String): F[BookId]
  def deleteBook(id: BookId): F[Option[Error]]
}
