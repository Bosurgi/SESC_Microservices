package com.sesc.libraryservice.service;

import com.sesc.libraryservice.constants.LibraryConstants;
import com.sesc.libraryservice.dto.Invoice;
import com.sesc.libraryservice.model.Book;
import com.sesc.libraryservice.model.Student;
import com.sesc.libraryservice.model.Transaction;
import com.sesc.libraryservice.repository.BookRepository;
import com.sesc.libraryservice.repository.FineRepository;
import com.sesc.libraryservice.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private FineRepository fineRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Student student;
    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Create a valid Student object
        student = new Student("c123456", "12345", "ROLE", false);

        // Create a valid Book object
        book = new Book("1234567890", "Title", "Author", 2024, 5);
    }

    @Test
    void testFindAllTransactionsByStudent() {
        // Arrange
        Student student = new Student();
        when(transactionRepository.findAllByStudent(student)).thenReturn(Arrays.asList(new Transaction(), new Transaction()));

        // Act
        List<Transaction> result = transactionService.findAllTransactionsByStudent(student);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testFindTransactionById_ExistingId() {
        // Arrange
        long transactionId = 1;
        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setId(transactionId);
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(expectedTransaction));

        // Act
        Transaction actualTransaction = transactionService.findTransactionById(transactionId);

        // Assert
        assertNotNull(actualTransaction);
        assertEquals(expectedTransaction.getId(), actualTransaction.getId());
    }

    @Test
    void testFindTransactionById_NonExistingId() {
        // Arrange
        long nonExistingId = 999;
        when(transactionRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Act
        Transaction actualTransaction = transactionService.findTransactionById(nonExistingId);

        // Assert
        assertNull(actualTransaction);
    }

    @Test
    void testFindTransactionByBook() {
        // Arrange
        Book book = new Book();
        when(transactionRepository.findTransactionByBook(book)).thenReturn(new Transaction());

        // Act
        Transaction result = transactionService.findTransactionByBook(book);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testBorrowTransaction_BookNotFound() {
        // Arrange
        Student student = new Student();
        Book book = new Book();
        book.setIsbn("1234567890");
        when(bookRepository.findBookByIsbn(book.getIsbn())).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> transactionService.borrowTransaction(student, book));
    }

    @Test
    void testBorrowTransaction_Success() {
        // Arrange
        Student student = new Student();
        Book book = new Book();
        book.setIsbn("1234567890");
        when(bookRepository.findBookByIsbn(book.getIsbn())).thenReturn(new Book());

        // Act
        assertDoesNotThrow(() -> transactionService.borrowTransaction(student, book));
    }


    @Test
    void testFindTransactionByBookAndStudent() {
        // Arrange
        Book book = new Book();
        Student student = new Student();
        when(transactionRepository.findTransactionByBookAndStudent(book, student)).thenReturn(new Transaction());

        // Act
        Transaction result = transactionService.findTransactionByBookAndStudent(book, student);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testPunctualReturn() {
        // Arrange
        LocalDate borrowDate = LocalDate.now().minusDays(2); // Assuming the book was borrowed 2 days ago
        Transaction transaction = new Transaction();
        transaction.setDateBorrowed(borrowDate);
        LocalDate returnDate = LocalDate.now();

        // Act
        Invoice invoice = transactionService.returnTransaction(transaction);

        // Assert
        assertNull(invoice); // Assuming the book was returned on time it shouldn't produce any invoice
        assertEquals(returnDate, transaction.getDateReturned());
    }

    @Test
    void testLateReturn() {
        // Arrange
        LocalDate borrowDate = LocalDate.now().minusDays(30); // Assuming the book was borrowed 30 days ago
        Transaction transaction = new Transaction(student, book, borrowDate, null);
        LocalDate returnDate = LocalDate.now();

        // Act
        Invoice invoice = transactionService.returnTransaction(transaction);

        // Assert
        assertNotNull(invoice); // Assuming the book was returned late it should produce an invoice
        assertEquals(returnDate, transaction.getDateReturned());
    }

    @Test
    void testLateReturn_FineGenerated() {
        // Arrange
        LocalDate borrowDate = LocalDate.now().minusDays(30); // Assuming the book was borrowed 30 days ago
        LocalDate returnDate = LocalDate.now();
        Transaction transaction = new Transaction(student, book, borrowDate, returnDate);

        // Act
        Invoice invoice = transactionService.recordLateReturn(transaction);

        // Assert
        assertNotNull(invoice); // Assuming the book was returned late, it should produce an invoice
        assertEquals(16 * LibraryConstants.FINE_PER_DAY.getDoubleValue(), invoice.getAmount()); // Check if fine amount is correct considering 14 days to return the book
        assertEquals(Invoice.Type.LIBRARY_FINE, invoice.getType()); // Check if invoice type is correct
        assertNotNull(invoice.getDueDate()); // Check if due date is set
    }

    @Test
    void testIsLateReturn_ReturnOnTime() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setDateBorrowed(LocalDate.now());
        transaction.setDateReturned(LocalDate.now().plusDays(LibraryConstants.MAX_DAYS.getLongValue()));

        // Act
        boolean result = transactionService.isLateReturn(transaction);

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsLateReturn_ReturnLate() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setDateBorrowed(LocalDate.now());
        transaction.setDateReturned(LocalDate.now().plusDays(LibraryConstants.MAX_DAYS.getLongValue() + 10));

        // Act
        boolean result = transactionService.isLateReturn(transaction);

        // Assert
        assertTrue(result);
    }

    @Test
    void testGetOverdue_NoOverdue() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setDateBorrowed(LocalDate.now());
        transaction.setDateReturned(LocalDate.now().plusDays(LibraryConstants.MAX_DAYS.getLongValue()));

        // Act
        Long overdue = transactionService.getOverdue(transaction);

        // Assert
        assertEquals(0, overdue);
    }

    @Test
    void testGetNumberOfOverdueBooks() {
        // Arrange
        Student student = new Student();
        List<Transaction> transactions = Arrays.asList(
                createOverdueTransaction(),
                createOverdueTransaction(),
                createOnTimeTransaction()
        );
        when(transactionRepository.findAllByStudent(student)).thenReturn(transactions);

        // Act
        Long overdueCount = transactionService.getNumberOfOverdueBooks(student);

        // Assert
        assertEquals(2, overdueCount);
    }

    /**
     * Helper method to create an overdue transaction
     *
     * @return the created transaction
     */
    private Transaction createOverdueTransaction() {
        Transaction transaction = new Transaction();
        transaction.setDateBorrowed(LocalDate.now().minusDays(30));
        transaction.setDateReturned(LocalDate.now());
        return transaction;
    }

    /**
     * Helper method to create an on-time transaction
     *
     * @return the created transaction
     */
    private Transaction createOnTimeTransaction() {
        Transaction transaction = new Transaction();
        transaction.setDateBorrowed(LocalDate.now().minusDays(10));
        transaction.setDateReturned(LocalDate.now().plusDays(LibraryConstants.MAX_DAYS.getLongValue()));
        return transaction;
    }

}
