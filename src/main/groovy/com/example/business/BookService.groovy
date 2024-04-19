package com.example.business

import javax.inject.Singleton
import javax.transaction.Transactional
import io.micronaut.data.exceptions.DataAccessException

import com.example.domain.Book
import com.example.domain.BookRepository
import com.example.exceptions.BookNotFoundException
import com.example.exceptions.BookNotAvailableException
import com.example.exceptions.BookPersistenceException

@Singleton
class BookService {

    private final BookRepository bookRepository

    BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository
    }

    @Transactional
    Book saveBook(Book book) {
        book.setAvailable(true)
        try {
            return bookRepository.save(book)
        } catch (DataAccessException e) {
            throw new BookPersistenceException("Failed to save the book", e)
        }
    }
  
    Iterable<Book> listBooks() {
        return bookRepository.findAll()
    }

    Iterable<Book> listAvailableBooks() {
        return bookRepository.findByAvailable(true)
    }

    Iterable<Book> listBorrowedBooks() {
        return bookRepository.findByAvailable(false)
    }

    List<Book> searchByTitle(String title) {
        return bookRepository.findByTitleContainsIgnoreCase(title)
    }

    @Transactional
    Book borrowBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
            new BookNotFoundException("Book with ID " + id + " not found"))
        if (!book.getAvailable()) {
            throw new BookNotAvailableException("Book with ID " + id + " is already borrowed")
        }
        book.setAvailable(false)
        return bookRepository.update(book)
    }

    @Transactional
    Book returnBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
            new BookNotFoundException("Book with ID " + id + " not found"))
        if (book.getAvailable()) {
            throw new IllegalStateException("Book with ID " + id + " is already returned")
        }
        book.setAvailable(true)
        return bookRepository.update(book)
    }

    @Transactional
    boolean deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Book with ID " + id + " not found for deletion")
        }
        try {
            bookRepository.deleteById(id)
            return true
        } catch (DataAccessException e) {
            throw new BookPersistenceException("Error accessing the database while deleting book with ID " + id, e)
        }
    }
}
