package com.sesc.libraryservice.controller;

import com.sesc.libraryservice.model.Book;
import com.sesc.libraryservice.model.Student;
import com.sesc.libraryservice.model.Transaction;
import com.sesc.libraryservice.service.BookService;
import com.sesc.libraryservice.service.StudentService;
import com.sesc.libraryservice.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final BookService bookService;

    private final StudentService studentService;

    public TransactionController(
            TransactionService transactionService,
            BookService bookService,
            StudentService studentService
    ) {

        this.transactionService = transactionService;
        this.bookService = bookService;
        this.studentService = studentService;
    }

    @PostMapping("/return")
    public ResponseEntity<Transaction> returnBook(@RequestParam Long transactionId) {
        Transaction transaction = transactionService.findTransactionById(transactionId);
        return ResponseEntity.ok(transactionService.returnTransaction(transaction));
    }

    @PostMapping("/borrow")
    public ResponseEntity<Transaction> borrowBook(@RequestParam String studentId, @RequestParam String bookIsbn) {
        // Fetching Book and Student to create the Transaction
        Book book = bookService.findBookByIsbn(bookIsbn);
        Student student = studentService.getStudentById(studentId);
        Transaction transaction = transactionService.borrowTransaction(student, book);
        return ResponseEntity.ok(transaction);
    }
}
