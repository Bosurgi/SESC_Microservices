package com.sesc.libraryservice.service;

import com.sesc.libraryservice.constants.LibraryConstants;
import com.sesc.libraryservice.model.Student;
import com.sesc.libraryservice.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TransactionService transactionService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        studentService = new StudentService(studentRepository, passwordEncoder, transactionService);
    }

    @Test
    void testCreateStudent_Successful() {
        // Arrange
        String studentId = "test123";
        String encodedPassword = "encodedPassword";

        // Mock behavior of studentRepository
        when(studentRepository.findStudentByStudentId(studentId)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(LibraryConstants.DEFAULT_PIN.getStringValue())).thenReturn(encodedPassword);
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Student createdStudent = studentService.createStudent(studentId);

        // Assert
        assertNotNull(createdStudent);
        assertEquals(studentId, createdStudent.getStudentId());
        assertEquals(LibraryConstants.STUDENT_ROLE.getStringValue(), createdStudent.getRole());
        assertTrue(createdStudent.isFirstLogin());
        assertEquals(encodedPassword, createdStudent.getPassword());

        // Verify that studentRepository.save() is called once
        verify(studentRepository, times(1)).save(any(Student.class));

        // Verify that studentRepository.save() is called once
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testCreateStudent_ExistingStudentId() {
        // Arrange
        String existingStudentId = "test123";
        Student existingStudent = new Student(existingStudentId, "password", "ROLE_STUDENT", true);

        // Mock behavior of studentRepository
        when(studentRepository.findStudentByStudentId(existingStudentId)).thenReturn(Optional.of(existingStudent));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> studentService.createStudent(existingStudentId));

        // Verify that studentRepository.findStudentByStudentId() is called once
        verify(studentRepository, times(1)).findStudentByStudentId(existingStudentId);
    }

    @Test
    void testUpdateStudent_Successful() {
        // Arrange
        String studentId = "test123";
        Student updatedStudent = new Student(studentId, "newPassword", "ROLE_ADMIN", false);
        Student existingStudent = new Student(studentId, "oldPassword", "ROLE_STUDENT", true);

        // Mock behavior of studentRepository
        when(studentRepository.findStudentByStudentId(studentId)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);

        // Act
        Student result = studentService.updateStudent(updatedStudent);

        // Assert
        assertNotNull(result);
        assertEquals(studentId, result.getStudentId());
        assertEquals("newPassword", result.getPassword());
        assertEquals("ROLE_ADMIN", result.getRole());
        assertFalse(result.isFirstLogin());

        // Verify that studentRepository.save() is called once
        verify(studentRepository, times(1)).save(any(Student.class));
    }
}
