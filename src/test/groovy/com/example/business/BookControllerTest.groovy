package com.example.business

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.HttpResponse
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.core.type.Argument
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import javax.inject.Inject
import java.util.List

import com.example.domain.Book

@MicronautTest
class BookControllerTest {

    @Inject
    @Client("/")
    HttpClient client

    private Book createTestBook() {
        return new Book(null, "John Doe", "A Great Book", 2021, "Fiction", true)
    }

    private Book createAndPostBook() {
        Book book = createTestBook()
        HttpResponse<Book> postResponse = client.toBlocking().exchange(
            HttpRequest.POST("/books/", book), Book.class
        )
        Assertions.assertEquals(HttpStatus.CREATED, postResponse.getStatus())
        return postResponse.body()
    }

    private void assertBookDetails(Book book, Book expectedBook) {
        Assertions.assertNotNull(book)
        Assertions.assertEquals(expectedBook.getAuthor(), book.getAuthor())
        Assertions.assertEquals(expectedBook.getTitle(), book.getTitle())
        Assertions.assertEquals(expectedBook.getPublishingYear(), book.getPublishingYear())
        Assertions.assertEquals(expectedBook.getCategory(), book.getCategory())
        Assertions.assertEquals(expectedBook.getAvailable(), book.getAvailable())
    }

    @Test
    void testBookCreation() {
        Book expectedBook = createTestBook()
        Book createdBook = createAndPostBook()
        assertBookDetails(createdBook, expectedBook)
    }

    @Test
    void testListBooks() {
        Book createdBook = createAndPostBook()
        HttpResponse<List<Book>> getResponse = client.toBlocking().exchange(
            HttpRequest.GET("/books"), Argument.listOf(Book.class)
        )
        Assertions.assertEquals(HttpStatus.OK, getResponse.getStatus())
        List<Book> books = getResponse.body()
        Assertions.assertFalse(books.isEmpty())
        Assertions.assertTrue(books.stream().anyMatch(b -> b.getId().equals(createdBook.getId())))
    }

    @Test
    void testBorrowAndReturnBook() {
        Book book = createAndPostBook()
        HttpResponse<Book> borrowResponse = client.toBlocking().exchange(
            HttpRequest.PATCH("/books/" + book.getId() + "/borrow", null), Book.class
        )
        Assertions.assertEquals(HttpStatus.OK, borrowResponse.getStatus())
        Assertions.assertFalse(borrowResponse.body().getAvailable())
        HttpResponse<Book> returnResponse = client.toBlocking().exchange(
            HttpRequest.PATCH("/books/" + book.getId() + "/return", null), Book.class
        )
        Assertions.assertEquals(HttpStatus.OK, returnResponse.getStatus())
        Assertions.assertTrue(returnResponse.body().getAvailable())
    }

    @Test
    void testDeleteBook() {
        Book book = createAndPostBook()
        HttpResponse deleteResponse = client.toBlocking().exchange(
            HttpRequest.DELETE("/books/" + book.getId())
        )
        Assertions.assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatus())
        HttpResponse<List<Book>> getResponse = client.toBlocking().exchange(
            HttpRequest.GET("/books"), Argument.listOf(Book.class)
        )
        Assertions.assertTrue(getResponse.body().stream().noneMatch(b -> b.getId().equals(book.getId())))
    }

    @Test
    void testBorrowNonExistentBook() {
        Long nonExistentId = 999999L
        HttpRequest<Object> borrowRequest = HttpRequest.PATCH("/books/" + nonExistentId + "/borrow", null)
        try {
            client.toBlocking().exchange(borrowRequest, Book.class)
            Assertions.fail("Expected a HttpClientResponseException due to non-existent book ID")
        } catch (HttpClientResponseException e) {
            Assertions.assertEquals(HttpStatus.NOT_FOUND, e.getStatus())
        }
    }

    @Test
    void testDeleteIdempotency() {
        Book book = createAndPostBook()
        HttpResponse firstDeleteResponse = client.toBlocking().exchange(
            HttpRequest.DELETE("/books/" + book.getId())
        )
        Assertions.assertEquals(HttpStatus.NO_CONTENT, firstDeleteResponse.getStatus())
        try {
            client.toBlocking().exchange(
                HttpRequest.DELETE("/books/" + book.getId())
            )
            Assertions.fail("Expected a HttpClientResponseException due to non-existent book ID")
        } catch (HttpClientResponseException e) {
            Assertions.assertEquals(HttpStatus.NOT_FOUND, e.getStatus())
        }
    }

    @Test
    void testBookValidation() {
        Book invalidBook = new Book(null, null, "Missing Title", 2021, "Fiction", true)
        try {
            client.toBlocking().exchange(
                HttpRequest.POST("/books/", invalidBook), Book.class
            )
            Assertions.fail("Expected a HttpClientResponseException due to validation error")
        } catch (HttpClientResponseException e) {
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, e.getStatus())
        }
    }
}
