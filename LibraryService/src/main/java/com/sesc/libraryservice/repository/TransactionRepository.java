package com.sesc.libraryservice.repository;

import com.sesc.libraryservice.model.Book;
import com.sesc.libraryservice.model.Student;
import com.sesc.libraryservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Transaction findTransactionByStudent(Student student);

    List<Transaction> findAllByStudent(Student student);

    Transaction findTransactionByBook(Book book);

    Transaction findTransactionByBookAndStudent(Book book, Student student);
    
}
