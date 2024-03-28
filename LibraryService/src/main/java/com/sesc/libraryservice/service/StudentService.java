package com.sesc.libraryservice.service;

import com.sesc.libraryservice.constants.LibraryConstants;
import com.sesc.libraryservice.model.Student;
import com.sesc.libraryservice.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    private final TransactionService transactionService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public StudentService(StudentRepository studentRepository, PasswordEncoder passwordEncoder, TransactionService transactionService) {
        this.studentRepository = studentRepository;
        this.transactionService = transactionService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a new student with the given studentId.
     *
     * @param studentId the ID of the student
     * @return the created student entity
     */
    public Student createStudent(String studentId) throws RuntimeException {
        // Check if the student already exists
        if (studentRepository.findStudentByStudentId(studentId).isPresent()) {
            throw new RuntimeException("Student with ID " + studentId + " already exists");
        }

        // Create a new student entity
        Student student = new Student();
        student.setStudentId(studentId);
        // Encoding the default pin using BCrypt encoder
        String encodedPassword = passwordEncoder.encode(LibraryConstants.DEFAULT_PIN.getStringValue());
        student.setPassword(encodedPassword);
        student.setRole(LibraryConstants.STUDENT_ROLE.getStringValue());
        student.setFirstLogin(true);

        // Save the student entity in the database
        return studentRepository.save(student);
    }

    public Student updateStudent(Student student) {
        Student studentToUpdate = studentRepository.findStudentByStudentId(student.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student with ID " + student.getStudentId() + " not found"));

        studentToUpdate.setPassword(student.getPassword());
        studentToUpdate.setRole(student.getRole());
        studentToUpdate.setFirstLogin(student.isFirstLogin());

        return studentRepository.save(studentToUpdate);
    }

    /**
     * Gets a student by the studentId.
     *
     * @param studentId the ID of the student
     * @return the student entity
     */
    public Student getStudentById(String studentId) {
        return studentRepository.findStudentByStudentId(studentId).orElse(null);
    }

    /**
     * Gets all the students from the database.
     *
     * @return a List of Students
     */
    public List<Student> getAllStudents() {
        // TODO: Refactor this operation to Repository
        // The list of users to return
        List<Student> studentList = new ArrayList<>();
        // All the Student will include the Admin user which we are going to filter out
        List<Student> userList = studentRepository.findAll();
        for (Student student : userList) {
            if (!student.getRole().equals(LibraryConstants.ADMIN_ROLE.getStringValue())) {
                student.setOverdueCount(transactionService.getNumberOfOverdueBooks(student));
                studentList.add(student);
            }
        }
        return studentList;
    }
}
