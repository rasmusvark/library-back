package com.example.business

import io.micronaut.http.annotation.*
import javax.inject.Inject  
import com.example.domain.Book
import com.example.business.BookService
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.HttpHeaders
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.core.annotation.Nullable
import com.example.exceptions.BookNotFoundException
import com.example.exceptions.BookNotAvailableException
import com.example.exceptions.BookPersistenceException

@Controller("/books")
class BookController {

    private final BookService bookService

    @Inject
    BookController(BookService bookService) {
        this.bookService = bookService
    }

    @Post("/")
    HttpResponse<Book> save(@Body Book book) {
        try {
            Book savedBook = bookService.saveBook(book)
            return HttpResponse.created(savedBook)
        } catch (BookPersistenceException e) {
            return HttpResponse.serverError().body(e.message)
        }
    }

    @Get("/")
    HttpResponse<List<Book>> list(@Nullable @QueryValue String title) {
        try {
            List<Book> books = title ? bookService.searchByTitle(title) : bookService.listBooks()
            return HttpResponse.ok(books)
        } catch (Exception e) {
            return HttpResponse.serverError().body("Error retrieving books")
        }
    }

    @Get("/available")
    HttpResponse<List<Book>> listAvailableBooks() {
        try {
            List<Book> availableBooks = bookService.listAvailableBooks()
            return HttpResponse.ok(availableBooks)
        } catch (Exception e) {
            return HttpResponse.serverError().body("Error retrieving available books")
        }
    }

    @Get("/borrowed")
    HttpResponse<List<Book>> listBorrowedBooks() {
        try {
            List<Book> borrowedBooks = bookService.listBorrowedBooks()
            return HttpResponse.ok(borrowedBooks)
        } catch (Exception e) {
            return HttpResponse.serverError().body("Error retrieving borrowed books")
        }
    }

    @Patch("/{id}/borrow")
    HttpResponse<Book> borrowBook(Long id) {
        try {
            Book borrowedBook = bookService.borrowBook(id)
            return HttpResponse.ok(borrowedBook)
        } catch (BookNotAvailableException e) {
            return HttpResponse.status(HttpStatus.BAD_REQUEST).body(e.message)
        } catch (BookNotFoundException e) {
            return HttpResponse.notFound().body(e.message)
        }
    }


    @Patch("/{id}/return")
    HttpResponse<Book> returnBook(Long id) {
        try {
            Book returnedBook = bookService.returnBook(id)
            return HttpResponse.ok(returnedBook)
        } catch (BookNotFoundException e) {
            return HttpResponse.notFound().body(e.message)
        } catch (IllegalStateException e) {
            return HttpResponse.status(HttpStatus.BAD_REQUEST).body(e.message)
        }
    }

    @Delete("/{id}")
    HttpResponse deleteBook(Long id) {
        try {
            boolean deleted = bookService.deleteBook(id)
            return deleted ? HttpResponse.noContent() : HttpResponse.notFound()
        } catch (BookNotFoundException e) {
            return HttpResponse.notFound().body(e.message)
        } catch (BookPersistenceException e) {
            return HttpResponse.serverError().body(e.message)
        }
    }

    @Options("/{+path}")
    HttpResponse options(String path) {
        return HttpResponse.ok()
            .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8081")
            .header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, GET, PUT, PATCH, DELETE, OPTIONS")
            .header(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Authorization, Content-Type")
            .header(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true")
    }
}
