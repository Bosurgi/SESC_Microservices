package com.sesc.libraryservice.repository;

import com.sesc.libraryservice.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Student findStudentByStudentId(String studentId);
}
