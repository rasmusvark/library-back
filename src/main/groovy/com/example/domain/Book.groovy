package com.example.domain

import jakarta.persistence.*

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