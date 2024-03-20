package com.sesc.libraryservice.service;

import com.sesc.libraryservice.model.Book;
import com.sesc.libraryservice.model.Student;
import com.sesc.libraryservice.model.Transaction;
import com.sesc.libraryservice.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * It creates a transaction when a student borrows a book.
     * @param student the student borrowing the book
     * @param book the book borrowed by the student
     * @return the transaction created
     */
    public Transaction createTransaction(Student student, Book book) {
        Transaction transaction = new Transaction();
        LocalDate date = LocalDate.now();
        transaction.setStudent(student);
        transaction.setBook(book);
        transaction.setDateBorrowed(date);
        return transactionRepository.save(transaction);
    }

    /**
     * It updates the transaction when a student returns a book.
     * @param transaction the transaction to be updated
     * @return the updated transaction
     */
    public Transaction updateTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction findTransactionByStudent(Student student) {
        return transactionRepository.findTransactionByStudent(student);
    }

    public Transaction findTransactionById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }
}
