package com.example.exceptions

class BookNotFoundException extends RuntimeException {
    BookNotFoundException(String message) {
        super(message)
    }
}