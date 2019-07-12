package example.denizkilic.bookcategoryapi

object Models {

  case class BookId(value: Int)

  case class BookItem(
      id: BookId,
      bookName: String,
      bookType: String,
      bookDescription: String
   )
}
