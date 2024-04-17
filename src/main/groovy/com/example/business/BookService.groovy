package com.example.business

import javax.inject.Singleton
import javax.transaction.Transactional
import com.example.domain.Book
import com.example.domain.BookRepository

@Singleton
class BookService {

    private final BookRepository bookRepository

    BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository
    }

    Book saveBook(Book book) {
        book.availability = true  // Ensure books are always available when added
        bookRepository.save(book)
    }

    Iterable<Book> listBooks() {
        bookRepository.findAll()
    }

    @Transactional
    Book borrowBook(Long id) throws IllegalStateException {
        Book book = bookRepository.findById(id).orElseThrow(() -> new IllegalStateException("Book not found"))
        if (!book.available) {
            throw new IllegalStateException("Book is already borrowed")
        }
        book.available = false
        return bookRepository.update(book)
    }

    @Transactional
    Book returnBook(Long id) throws IllegalStateException {
        Book book = bookRepository.findById(id).orElseThrow(() -> new IllegalStateException("Book not found"))
        if (book.available) {
            throw new IllegalStateException("Book is already returned")
        }
        book.available = true
        return bookRepository.update(book)
    }
}