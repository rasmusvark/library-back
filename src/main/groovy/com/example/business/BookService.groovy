package com.example.business

import javax.inject.Singleton
import javax.transaction.Transactional
import com.example.domain.Book
import com.example.domain.BookRepository

@Singleton
class BookService {

    private final BookRepository bookRepository;

    BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    Book saveBook(Book book) {
        book.setAvailable(true); // Make sure you have setter method or direct access is allowed.
        return bookRepository.save(book); // Return the saved book.
    }

    Iterable<Book> listBooks() {
        return bookRepository.findAll(); // Return the result of findAll.
    }

    @Transactional
    Book borrowBook(Long id) throws IllegalStateException {
        Book book = bookRepository.findById(id).orElseThrow(() -> new IllegalStateException("Book not found"));
        if (!book.getAvailable()) { // Assuming 'available' has getter method.
            throw new IllegalStateException("Book is already borrowed");
        }
        book.setAvailable(false); // Use setters if available.
        return bookRepository.update(book); // Return the updated book.
    }

    @Transactional
    Book returnBook(Long id) throws IllegalStateException {
        Book book = bookRepository.findById(id).orElseThrow(() -> new IllegalStateException("Book not found"));
        if (book.getAvailable()) { // Again, assuming getters and setters are used.
            throw new IllegalStateException("Book is already returned");
        }
        book.setAvailable(true);
        return bookRepository.update(book); // Return the updated book.
    }
}
