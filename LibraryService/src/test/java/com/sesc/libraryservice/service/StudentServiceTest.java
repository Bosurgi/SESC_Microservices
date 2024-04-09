package com.sesc.libraryservice.service;

import com.sesc.libraryservice.constants.LibraryConstants;
import com.sesc.libraryservice.model.Student;
import com.sesc.libraryservice.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class StudentServiceTest {

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateStudent_Successful() {
        // Arrange
        String studentId = "test123";
        when(studentRepository.findStudentByStudentId(studentId)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(LibraryConstants.DEFAULT_PIN.getStringValue())).thenReturn("encodedPassword");
        Student expectedStudent = new Student();
        expectedStudent.setStudentId(studentId);
        expectedStudent.setPassword("encodedPassword");
        expectedStudent.setRole(LibraryConstants.STUDENT_ROLE.getStringValue());
        expectedStudent.setFirstLogin(true);
        when(studentRepository.save(any(Student.class))).thenReturn(expectedStudent);

        // Act
        Student createdStudent = studentService.createStudent(studentId);

        // Assert
        assertNotNull(createdStudent);
        assertEquals(studentId, createdStudent.getStudentId());
        assertEquals("encodedPassword", createdStudent.getPassword());
        assertEquals(LibraryConstants.STUDENT_ROLE.getStringValue(), createdStudent.getRole());
        assertTrue(createdStudent.isFirstLogin());
    }

    @Test
    void testCreateStudent_ExistingStudentId() {
        // Arrange
        String existingStudentId = "test123";
        when(studentRepository.findStudentByStudentId(existingStudentId)).thenReturn(Optional.of(new Student()));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> studentService.createStudent(existingStudentId));
    }

    @Test
    void testUpdateStudent_Successful() {
        // Arrange
        String studentId = "test123";
        Student existingStudent = new Student();
        existingStudent.setStudentId(studentId);
        when(studentRepository.findStudentByStudentId(studentId)).thenReturn(Optional.of(existingStudent));
        Student updatedStudent = new Student();
        updatedStudent.setStudentId(studentId);
        updatedStudent.setPassword("newPassword");
        updatedStudent.setRole("ROLE_ADMIN");
        updatedStudent.setFirstLogin(false);
        when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);

        // Act
        Student result = studentService.updateStudent(updatedStudent);

        // Assert
        assertNotNull(result);
        assertEquals(studentId, result.getStudentId());
        assertEquals("newPassword", result.getPassword());
        assertEquals("ROLE_ADMIN", result.getRole());
        assertFalse(result.isFirstLogin());
    }

    @Test
    void testUpdateStudent_NonExistingStudentId() {
        // Arrange
        String nonExistingStudentId = "nonExistingId";
        when(studentRepository.findStudentByStudentId(nonExistingStudentId)).thenReturn(Optional.empty());
        Student nonExistingStudent = new Student();
        nonExistingStudent.setStudentId(nonExistingStudentId);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> studentService.updateStudent(nonExistingStudent));
    }

    @Test
    void testGetStudentById_ExistingStudentId() {
        // Arrange
        String studentId = "test123";
        Student expectedStudent = new Student();
        expectedStudent.setStudentId(studentId);
        when(studentRepository.findStudentByStudentId(studentId)).thenReturn(Optional.of(expectedStudent));

        // Act
        Student result = studentService.getStudentById(studentId);

        // Assert
        assertNotNull(result);
        assertEquals(studentId, result.getStudentId());
    }

    @Test
    void testGetStudentById_NonExistingStudentId() {
        // Arrange
        String nonExistingStudentId = "nonExistingId";
        when(studentRepository.findStudentByStudentId(nonExistingStudentId)).thenReturn(Optional.empty());

        // Act
        Student result = studentService.getStudentById(nonExistingStudentId);

        // Assert
        assertNull(result);
    }

    @Test
    void testGetAllStudents_NonAdminUsers() {
        // Arrange
        List<Student> userList = new ArrayList<>();
        userList.add(createStudentWithRole("test123", LibraryConstants.STUDENT_ROLE.getStringValue()));
        userList.add(createStudentWithRole("admin123", LibraryConstants.ADMIN_ROLE.getStringValue()));
        when(studentRepository.findAll()).thenReturn(userList);
        when(transactionService.getNumberOfOverdueBooks(any(Student.class))).thenReturn(0L);

        // Act
        List<Student> result = studentService.getAllStudents();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test123", result.get(0).getStudentId());
    }

    @Test
    void testGetAllStudents_EmptyRepository() {
        // Arrange
        when(studentRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Student> result = studentService.getAllStudents();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Helper method to create a Student object with the given studentId and role.
     *
     * @param studentId the student ID
     * @param role      the role of the student
     * @return the created Student object
     */
    private Student createStudentWithRole(String studentId, String role) {
        Student student = new Student();
        student.setStudentId(studentId);
        student.setRole(role);
        return student;
    }
}
