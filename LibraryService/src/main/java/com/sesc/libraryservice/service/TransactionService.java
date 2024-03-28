package com.sesc.libraryservice.service;

import com.sesc.libraryservice.constants.LibraryConstants;
import com.sesc.libraryservice.model.Book;
import com.sesc.libraryservice.model.Fine;
import com.sesc.libraryservice.model.Student;
import com.sesc.libraryservice.model.Transaction;
import com.sesc.libraryservice.repository.FineRepository;
import com.sesc.libraryservice.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    private final FineRepository fineRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            FineRepository fineRepository
    ) {
        this.transactionRepository = transactionRepository;
        this.fineRepository = fineRepository;
    }

    /**
     * It creates a transaction when a student borrows a book.
     *
     * @param student the student borrowing the book
     * @param book    the book borrowed by the student
     * @return the transaction created
     */
    public Transaction borrowTransaction(Student student, Book book) {
        Transaction transaction = new Transaction();
        LocalDate date = LocalDate.now();
        transaction.setStudent(student);
        transaction.setBook(book);
        transaction.setDateBorrowed(date);
        return transactionRepository.save(transaction);
    }

    /**
     * It updates the transaction when a student returns a book.
     *
     * @param transaction the transaction to be updated
     */
    public void returnTransaction(Transaction transaction) {
        transaction.setDateReturned(LocalDate.now());
        // Checking for late returns
        recordLateReturn(transaction);
        transactionRepository.save(transaction);
    }

    public List<Transaction> findAllTransactionsByStudent(Student student) {
        return transactionRepository.findAllByStudent(student);
    }

    public Transaction findTransactionById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public Transaction findTransactionByBook(Book book) {
        return transactionRepository.findTransactionByBook(book);
    }

    public Transaction findTransactionByBookAndStudent(Book book, Student student) {
        return transactionRepository.findTransactionByBookAndStudent(book, student);
    }

    /**
     * It records a fine when a book is returned late.
     *
     * @param transaction the transaction to be updated
     */
    public void recordLateReturn(Transaction transaction) {
        LocalDate dueDate = transaction.getDateBorrowed().plusDays(LibraryConstants.MAX_DAYS.getLongValue());
        LocalDate dateReturned = transaction.getDateReturned();

        // Calculating the fine is book returned late
        if (dateReturned.isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, dateReturned);
            double fineValue = daysLate * LibraryConstants.FINE_PER_DAY.getDoubleValue();
            Fine fine = new Fine();
            fine.setTransaction(transaction);
            fine.setAmount(fineValue);
            fine.setDue(LocalDate.now());
            fine.setType(LibraryConstants.FINE_NAME.getStringValue());
            fineRepository.save(fine);
        }
    }

    /**
     * It calculates the number of days a book is overdue only if exceeds the maximum days.
     *
     * @param transaction the transaction to be checked
     * @return the number of days the book is overdue
     */
    public Long getOverdue(Transaction transaction) {
        LocalDate dueDate = transaction.getDateBorrowed().plusDays(LibraryConstants.MAX_DAYS.getLongValue());
        LocalDate currentDate = LocalDate.now();
        if (currentDate.isAfter(dueDate)) {
            return ChronoUnit.DAYS.between(dueDate, currentDate);
        }
        return 0L;
    }

    /**
     * It gets all transactions of a student and their overdue days.
     *
     * @param student the student to get transactions for
     * @return the list of transactions with overdue days
     */
    public List<Transaction> getTransactionsAndOverdueDays(Student student) {
        // Find all transactions by Student
        List<Transaction> transactions = transactionRepository.findAllByStudent(student);
        // Setting the Overdue Days for each transaction
        for (Transaction transaction : transactions) {
            transaction.setOverdueDays(getOverdue(transaction));
        }
        return transactions;
    }

    /**
     * It gets all transactions in the database.
     *
     * @return the list of all transactions
     */
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        for (Transaction transaction : transactions) {
            transaction.setOverdueDays(getOverdue(transaction));
        }
        return transactions;
    }

    /**
     * It gets the number of overdue books of a student.
     *
     * @param student the student that might have overdue books
     * @return the number of overdue books
     */
    public Long getNumberOfOverdueBooks(Student student) {
        List<Transaction> transactions = getTransactionsAndOverdueDays(student);
        return transactions.stream().filter(transaction -> transaction.getOverdueDays() > 0).count();
    }
}
