package com.sesc.libraryservice.controller;

import com.sesc.libraryservice.model.Book;
import com.sesc.libraryservice.model.Student;
import com.sesc.libraryservice.model.Transaction;
import com.sesc.libraryservice.service.BookService;
import com.sesc.libraryservice.service.StudentService;
import com.sesc.libraryservice.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/return/{transactionId}")
    public ResponseEntity<Transaction> returnBook(@PathVariable Long transactionId) {
        try {
            Transaction transaction = transactionService.findTransactionById(transactionId);
            return ResponseEntity.ok(transactionService.returnTransaction(transaction));

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/borrow")
    public ResponseEntity<Transaction> borrowBook(@RequestParam String studentId, @RequestParam String bookIsbn) {
        try{
        Book book = bookService.findBookByIsbn(bookIsbn);
        Student student = studentService.getStudentById(studentId);
        Transaction transaction = transactionService.borrowTransaction(student, book);
        // Fetching Book and Student to create the Transaction
        return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
