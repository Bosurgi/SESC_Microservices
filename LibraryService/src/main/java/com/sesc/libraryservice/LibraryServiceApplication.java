package com.sesc.libraryservice;

import com.sesc.libraryservice.model.Book;
import com.sesc.libraryservice.model.Student;
import com.sesc.libraryservice.model.Transaction;
import com.sesc.libraryservice.repository.BookRepository;
import com.sesc.libraryservice.repository.StudentRepository;
import com.sesc.libraryservice.repository.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@SpringBootApplication
public class LibraryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryServiceApplication.class, args);
    }


    @Bean
    public CommandLineRunner initTestEntries(
            BookRepository bookRepository,
            StudentRepository studentRepository,
            PasswordEncoder passwordEncoder,
            TransactionRepository transactionRepository
    ) {
        return args -> {
            // Populate the database with 5 books
            bookRepository.save(new Book(null, "9780061120084", "To Kill a Mockingbird", "Harper Lee", 1960, 10));
            bookRepository.save(new Book(null, "9780143124171", "1984", "George Orwell", 1949, 8));
            bookRepository.save(new Book(null, "9780060850524", "The Catcher in the Rye", "J.D. Salinger", 1951, 12));
            bookRepository.save(new Book(null, "9780140389661", "Of Mice and Men", "John Steinbeck", 1937, 6));
            bookRepository.save(new Book(null, "9780743273565", "The Great Gatsby", "F. Scott Fitzgerald", 1925, 15));

            // Testing Accounts
            studentRepository.save(new Student("c12345", passwordEncoder.encode("123"), "REGISTERED", false));
            studentRepository.save(new Student("admin", passwordEncoder.encode("123"), "ADMIN", false));

            // Testing Transactions
            transactionRepository.save(new Transaction(null, studentRepository.findStudentByStudentId("c12345").get(), bookRepository.findBookByIsbn("9780061120084"), LocalDate.now(), null));
            transactionRepository.save(new Transaction(null, studentRepository.findStudentByStudentId("c12345").get(), bookRepository.findBookByIsbn("9780743273565"), LocalDate.of(2024, 3, 1), null));
            transactionRepository.save(new Transaction(null, studentRepository.findStudentByStudentId("c12345").get(), bookRepository.findBookByIsbn("9780140389661"), LocalDate.of(2024, 3, 13), null));
        };
    }

}
