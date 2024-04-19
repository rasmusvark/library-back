package com.example.exceptions

class BookPersistenceException extends RuntimeException {
    BookPersistenceException(String message, Throwable cause) {
        super(message, cause)
    }
}