package com.sesc.libraryservice.repository;

import com.sesc.libraryservice.model.Student;
import com.sesc.libraryservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{

    Transaction findTransactionByStudent(Student student);
}
