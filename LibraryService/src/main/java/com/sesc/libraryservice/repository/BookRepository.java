package com.sesc.libraryservice.repository;

import com.sesc.libraryservice.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    Book findBookByIsbn(String isbn);

}
