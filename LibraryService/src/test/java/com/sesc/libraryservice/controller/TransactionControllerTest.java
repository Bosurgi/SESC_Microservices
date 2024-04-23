package com.sesc.libraryservice.controller;

import com.sesc.libraryservice.dto.Invoice;
import com.sesc.libraryservice.integration.IntegrationService;
import com.sesc.libraryservice.model.Book;
import com.sesc.libraryservice.model.Student;
import com.sesc.libraryservice.model.Transaction;
import com.sesc.libraryservice.service.BookService;
import com.sesc.libraryservice.service.StudentService;
import com.sesc.libraryservice.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ui.Model;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class TransactionControllerTest {

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private BookService bookService;

    @MockBean
    private StudentService studentService;

    @Autowired
    private TransactionController transactionController;

    @Mock
    private IntegrationService integrationService;

    @BeforeEach
    void setUp() {
        transactionService = mock(TransactionService.class);
        bookService = mock(BookService.class);
        studentService = mock(StudentService.class);
        transactionController = new TransactionController(
                transactionService,
                bookService,
                studentService,
                mock(IntegrationService.class)
        );
    }

    @Test
    void testBorrowBook_Success() {
        // Mock
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("student123");
        Student student = new Student();
        when(studentService.getStudentById("student123")).thenReturn(student);
        Book book = new Book();
        when(bookService.findBookByIsbn(anyString())).thenReturn(book);

        // Execute
        String viewName = transactionController.borrowBook(principal, "isbn123", mock(Model.class));

        // Verify
        assertEquals("borrow", viewName);
        verify(transactionService, times(1)).borrowTransaction(student, book);
        verify(bookService, times(1)).updateBookCopies(book.getId(), 1);
    }

    @Test
    void testBorrowBook_Exception() {
        // Mocking principal
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("student123");

        // Mocking bookService
        BookService bookService = mock(BookService.class);
        when(bookService.findBookByIsbn("isbn123")).thenThrow(new RuntimeException());

        // Mocking transactionService
        TransactionService transactionService = mock(TransactionService.class);

        // Mocking model
        Model model = mock(Model.class);

        // Creating TransactionController instance with mocked dependencies
        TransactionController transactionController = new TransactionController(transactionService, bookService, studentService, integrationService);

        // Execute
        String viewName = transactionController.borrowBook(principal, "isbn123", model);

        // Verify
        assertEquals("borrow", viewName);
        // Verify that the bookService.findBookByIsbn() method was called exactly once
        verify(bookService, times(1)).findBookByIsbn("isbn123");
        // Verify that no further interactions occur with transactionService
        verifyNoMoreInteractions(transactionService);
    }

    @Test
    void testReturnBook_Success() {
        // Mock
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("student123");
        Student student = new Student();
        when(studentService.getStudentById("student123")).thenReturn(student);
        Book book = new Book();
        when(bookService.findBookByIsbn(anyString())).thenReturn(book);
        Transaction transaction = new Transaction();
        when(transactionService.findTransactionByBookAndStudent(book, student)).thenReturn(transaction);
        Invoice invoice = new Invoice();
        when(transactionService.returnTransaction(transaction)).thenReturn(invoice);

        // Execute
        String viewName = transactionController.returnBook(mock(Model.class), principal, "isbn123");

        // Verify
        assertEquals("return", viewName);
        verify(bookService, times(1)).updateBookCopies(book.getId(), -1);
        verify(transactionService, times(1)).returnTransaction(transaction);
    }

    @Test
    void testBorrowBook_NonExistentBook() {
        // Mocking principal
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("student123");

        // Mocking bookService to return null when book is not found
        when(bookService.findBookByIsbn("isbn123")).thenReturn(null);

        // Mocking model
        Model model = mock(Model.class);

        // Execute
        String viewName = transactionController.borrowBook(principal, "isbn123", model);

        // Verify
        assertEquals("borrow", viewName);
        // Verify that the bookService.findBookByIsbn() method was called exactly once
        verify(bookService, times(1)).findBookByIsbn("isbn123");
        // Verify that no interactions occur with transactionService
        // Verify that the model contains the appropriate error message
        verify(model, times(1)).addAttribute(eq("error"), eq("Error borrowing book"));
    }

    @Test
    void testReturnBook_Success_NoInvoice() {
        // Mock
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("student123");
        Student student = new Student();
        when(studentService.getStudentById("student123")).thenReturn(student);
        Book book = new Book();
        when(bookService.findBookByIsbn(anyString())).thenReturn(book);
        Transaction transaction = new Transaction();
        when(transactionService.findTransactionByBookAndStudent(book, student)).thenReturn(transaction);
        when(transactionService.returnTransaction(transaction)).thenReturn(null);

        // Execute
        String viewName = transactionController.returnBook(mock(Model.class), principal, "isbn123");

        // Verify
        assertEquals("return", viewName);
        verify(bookService, times(1)).updateBookCopies(book.getId(), -1);
        verify(transactionService, times(1)).returnTransaction(transaction);
    }

}