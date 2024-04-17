package com.example.domain

import io.micronaut.data.annotation.*
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAvailable(Boolean available)
    List<Book> findByNameContainsIgnoreCase(String name)
}