package com.sesc.libraryservice.controller;

import com.sesc.libraryservice.model.Book;
import com.sesc.libraryservice.model.Student;
import com.sesc.libraryservice.model.Transaction;
import com.sesc.libraryservice.service.BookService;
import com.sesc.libraryservice.service.StudentService;
import com.sesc.libraryservice.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final BookService bookService;
    private final StudentService studentService;

    // CONSTRUCTOR //
    public TransactionController(
            TransactionService transactionService,
            BookService bookService,
            StudentService studentService
    ) {

        this.transactionService = transactionService;
        this.bookService = bookService;
        this.studentService = studentService;
    }

    /**
     * It gets the Borrow Book Page
     *
     * @return the borrow page
     */
    @GetMapping("/borrow")
    public String borrowBookPage() {
        return "borrow";
    }

    /**
     * Creates a transaction when a student borrows a book.
     *
     * @param studentId the studentId borrowing the book
     * @param bookIsbn  the bookIsbn borrowed by the student
     * @return the new Transaction with HTTP Status code
     */
    @PostMapping("/borrow")
    public String borrowBook(@RequestParam String studentId, @RequestParam String bookIsbn, Model model) {
        try {
            // Fetching Book and Student to create the Transaction
            Book book = bookService.findBookByIsbn(bookIsbn);
            Student student = studentService.getStudentById(studentId);
            Transaction transaction = transactionService.borrowTransaction(student, book);

            // Adding success message
            model.addAttribute("success", "Book borrowed successfully");
            return "borrow";
        } catch (Exception e) {
            model.addAttribute("error", "Error borrowing book");
            return "borrow";
        }
    }

    /**
     * Updates the transaction when a book is returned.
     *
     * @param transactionId the transactionId to update
     * @return the new Transaction with HTTP Status code
     */
    @PostMapping("/return/{transactionId}")
    public ResponseEntity<Transaction> returnBook(@PathVariable Long transactionId) {
        try {
            Transaction transaction = transactionService.findTransactionById(transactionId);
            return ResponseEntity.ok(transactionService.returnTransaction(transaction));

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
