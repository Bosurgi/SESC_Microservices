package com.sesc.libraryservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Book entity
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String isbn;

    private String title;

    private String author;

    @Column(name = "publication_year")
    private int year;

    private int copies;

    // CONSTRUCTOR WITHOUT ID//
    public Book(String isbn, String title, String author, int year, int copies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.year = year;
        this.copies = copies;
    }

    public Book(String isbn, String title, String author, int year) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.year = year;
    }
}
