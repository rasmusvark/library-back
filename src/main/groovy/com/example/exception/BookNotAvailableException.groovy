package com.example.exceptions

class BookNotAvailableException extends RuntimeException {
    BookNotAvailableException(String message) {
        super(message)
    }
}