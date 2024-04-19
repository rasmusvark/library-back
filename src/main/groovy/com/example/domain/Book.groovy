package com.example.domain

import jakarta.persistence.*
import io.micronaut.serde.annotation.Serdeable
import groovy.transform.TupleConstructor
import jakarta.validation.constraints.NotNull

@Serdeable
@Entity
@Table(name = "books")
@TupleConstructor
class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "author", nullable = false)
    @NotNull
    String author;

    @Column(name = "title", nullable = false)
    @NotNull
    String title;

    @Column(name = "publishing_year")
    Integer publishingYear;

    @Column(name = "category")
    String category;

    @Column(name = "available")
    Boolean available;
    
}