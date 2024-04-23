package com.sesc.studentportal.services;

import com.sesc.studentportal.model.Student;
import com.sesc.studentportal.model.User;
import com.sesc.studentportal.repository.StudentRepository;
import com.sesc.studentportal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class StudentServiceTest {

    private StudentRepository studentRepository;

    private UserRepository userRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        studentRepository = mock(StudentRepository.class);
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testRegisterStudent_UserPresent_Success() {
        // Arrange
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        user.setFirstname("John");
        user.setSurname("Doe");

        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
        when(studentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Student registeredStudent = studentService.registerStudent(username);

        // Assert
        assertNotNull(registeredStudent);
        assertEquals(user.getSurname(), registeredStudent.getSurname());
        assertEquals(user.getFirstname(), registeredStudent.getFirstName());
        assertTrue(registeredStudent.getIsEnrolled());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testRegisterStudent_UserNotPresent_ReturnsNull() {
        // Arrange
        String username = "nonExistentUser";
        when(userRepository.findUserByUsername(username)).thenReturn(Optional.empty());

        // Act
        Student registeredStudent = studentService.registerStudent(username);

        // Assert
        assertNull(registeredStudent);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void testUpdateStudent() {
        // Arrange
        Student student = new Student();
        student.setStudentId(1L);

        when(studentRepository.save(student)).thenReturn(student);

        // Act
        Student updatedStudent = studentService.updateStudent(student);

        // Assert
        assertNotNull(updatedStudent);
        assertEquals(student.getStudentId(), updatedStudent.getStudentId());
        verify(studentRepository, times(1)).save(student);
    }
}