package com.example.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import io.micronaut.core.annotation.Introspected

@Entity
class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id
    String author
    String name
    Integer publishingYear
    String category
    Boolean available = true
}