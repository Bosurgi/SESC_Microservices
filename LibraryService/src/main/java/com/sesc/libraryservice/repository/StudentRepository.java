package com.sesc.libraryservice.repository;

import com.sesc.libraryservice.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findStudentByStudentId(String studentId);
}
