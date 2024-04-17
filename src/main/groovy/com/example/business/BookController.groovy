package com.example.business

import io.micronaut.http.annotation.*  
import javax.inject.Inject  
import com.example.domain.Book
import com.example.business.BookService

@Controller("/books")
class BookController {

    private final BookService bookService

    @Inject
    BookController(BookService bookService) {
        this.bookService = bookService
    }

    @Post("/")
    Book save(@Body Book book) {
        return bookService.saveBook(book)
    }

    @Get("/")
    Iterable<Book> list() {
        return bookService.listBooks()
    }

    @Patch("/{id}/borrow")
    Book borrowBook(Long id) {
        return bookService.borrowBook(id)
    }

    @Patch("/{id}/return")
    Book returnBook(Long id) {
        return bookService.returnBook(id)
    }
}
