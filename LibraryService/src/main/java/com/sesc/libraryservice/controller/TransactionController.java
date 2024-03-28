package com.sesc.libraryservice.controller;

import com.sesc.libraryservice.model.Book;
import com.sesc.libraryservice.model.Student;
import com.sesc.libraryservice.model.Transaction;
import com.sesc.libraryservice.service.BookService;
import com.sesc.libraryservice.service.StudentService;
import com.sesc.libraryservice.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/api/v1/")
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
    @GetMapping("transactions/borrow")
    public String borrowBookPage() {
        return "borrow";
    }

    /**
     * It gets the Return Book Page
     *
     * @return the return page
     */
    @GetMapping("transactions/return")
    public String returnBookPage() {
        return "return";
    }

    /**
     * Creates a transaction when a student borrows a book.
     *
     * @param principal the current logged user
     * @param bookIsbn  the bookIsbn borrowed by the student
     * @return the new Transaction with HTTP Status code
     */
    @PostMapping("transactions/borrow")
    public String borrowBook(Principal principal, @RequestParam String bookIsbn, Model model) {
        try {
            // Fetching Book and Student to create the Transaction
            Book book = bookService.findBookByIsbn(bookIsbn);
            Student student = studentService.getStudentById(principal.getName());
            transactionService.borrowTransaction(student, book);
            // Update the book copies to reflect the borrowed book
            bookService.updateBookCopies(book.getId(), 1);

            // Adding success message
            model.addAttribute("success", "Book borrowed successfully");
            return "borrow";
        } catch (Exception e) {
            model.addAttribute("error", "Error borrowing book");
            return "borrow";
        }
    }

    /**
     * It updates the transaction when a student returns a book.
     * It also checks for late returns and records them.
     *
     * @param model     the model to add the success or error message to.
     * @param principal the current logged user.
     * @param bookIsbn  the bookIsbn returned by the student.
     * @return the return page with the success or error message.
     */
    @PostMapping("transactions/return")
    public String returnBook(Model model, Principal principal, @RequestParam String bookIsbn) {
        try {
            // Fetching Book and Student to update the Transaction
            Book bookToReturn = bookService.findBookByIsbn(bookIsbn);
            Student currentStudent = studentService.getStudentById(principal.getName());
            // Fetching the transaction based on book and student
            Transaction transactionToUpdate = transactionService.findTransactionByBookAndStudent(bookToReturn, currentStudent);
            // Updating the transaction and book copies
            bookService.updateBookCopies(bookToReturn.getId(), -1);
            transactionService.returnTransaction(transactionToUpdate);

            model.addAttribute("success", "Book returned successfully");
            return "return";

        } catch (Exception e) {
            model.addAttribute("error", "Error returning book");
            System.out.println(e.getMessage());
            return "return";
        }
    }

    /**
     * It gets the Account Page with all transactions of a student
     *
     * @param principal the current logged user
     * @param model     the model to add the transactions to and display on the page
     * @return the account page
     */
    @GetMapping("/transactions")
    public String getTransactions(Principal principal, Model model) {
        // Fetching current student
        Student currentStudent = studentService.getStudentById(principal.getName());

        // Getting the transactions and overdue days
        List<Transaction> transactions = transactionService.getTransactionsAndOverdueDays(currentStudent);

        // Adding the transactions to the model to display on the page
        model.addAttribute("transactions", transactions);
        return "account";
    }

    /**
     * It gets the Admin Loans Page with all transactions
     *
     * @param model the model to add the transactions to and display on the page
     * @return the admin loans page
     */
    @GetMapping("admin/loans")
    public String getTransactions(Model model) {
        List<Transaction> transactions = transactionService.getAllTransactions();
        model.addAttribute("transactions", transactions);
        return "admin/loans";
    }

    /**
     * It gets the Admin Overdue Page with all overdue transactions
     *
     * @param model the model to add the transactions to and display on the page
     * @return the admin overdue page
     */
    @GetMapping("admin/overdue")
    public String getOverdueTransactions(Model model) {
        List<Transaction> transactions = transactionService.getAllOverdueTransactions();
        model.addAttribute("transactions", transactions);
        return "admin/overdue";
    }
}
