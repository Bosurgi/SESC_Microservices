package com.sesc.libraryservice;

import com.sesc.libraryservice.model.Book;
import com.sesc.libraryservice.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LibraryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryServiceApplication.class, args);
    }


    @Bean
    public CommandLineRunner initBooks(BookRepository bookRepository) {
        return args -> {
            // Populate the database with 5 books
            bookRepository.save(new Book(null, "9780061120084", "To Kill a Mockingbird", "Harper Lee", 1960, 10));
            bookRepository.save(new Book(null, "9780143124171", "1984", "George Orwell", 1949, 8));
            bookRepository.save(new Book(null, "9780060850524", "The Catcher in the Rye", "J.D. Salinger", 1951, 12));
            bookRepository.save(new Book(null, "9780140389661", "Of Mice and Men", "John Steinbeck", 1937, 6));
            bookRepository.save(new Book(null, "9780743273565", "The Great Gatsby", "F. Scott Fitzgerald", 1925, 15));
        };
    }

}
