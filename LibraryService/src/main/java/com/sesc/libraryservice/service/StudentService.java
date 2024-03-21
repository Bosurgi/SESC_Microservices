package com.sesc.libraryservice.service;

import com.sesc.libraryservice.constants.LibraryConstants;
import com.sesc.libraryservice.model.Student;
import com.sesc.libraryservice.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Creates a new student with the given studentId.
     *
     * @param studentId the ID of the student
     * @return the created student entity
     */
    public Student createStudent(String studentId) throws RuntimeException {
        // Check if the student already exists
        if (studentRepository.findStudentByStudentId(studentId) != null) {
            throw new RuntimeException("Student with ID " + studentId + " already exists");
        }

        // Create a new student entity
        Student student = new Student();
        student.setStudentId(studentId);
        student.setPassword(LibraryConstants.DEFAULT_PIN.getStringValue());
        student.setRole(LibraryConstants.STUDENT_ROLE.getStringValue());

        // Save the student entity in the database
        return studentRepository.save(student);
    }

    public Student getStudentById(String studentId) {
        return studentRepository.findStudentByStudentId(studentId);
    }

    /**
     * Gets all the students from the database.
     * @return a List of Students
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}
