package com.sesc.libraryservice.controller;

import com.sesc.libraryservice.model.Student;
import com.sesc.libraryservice.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthControllerTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mock
    private Model model;

    @MockBean
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testChangePassword_Success() {
        // Mock data
        Student student = new Student();
        student.setStudentId("testUser");
        student.setPassword(passwordEncoder.encode("oldPassword"));
        student.setRole("STUDENT");

        Authentication auth = new UsernamePasswordAuthenticationToken("testUser", "oldPassword");

        when(studentService.getStudentById("testUser")).thenReturn(student);
        when(passwordEncoder.matches("oldPassword", student.getPassword())).thenReturn(true);
        when(passwordEncoder.matches("newPassword", student.getPassword())).thenReturn(false);

        // Execute
        String result = authController.changePassword("oldPassword", "newPassword", "newPassword", auth, model);

        // Verify
        assertEquals("redirect:/", result);
        assertFalse(student.isFirstLogin());
        assertEquals("REGISTERED", student.getRole());
        verify(studentService, times(1)).updateStudent(student);
    }

    @Test
    void testChangePassword_PasswordMismatch() {
        // Mock data
        Authentication auth = new UsernamePasswordAuthenticationToken("testUser", "oldPassword");

        // Execute
        String result = authController.changePassword("oldPassword", "newPassword", "confirmPassword", auth, model);

        // Verify
        assertEquals("changepassword", result);
        verify(model, times(1)).addAttribute(eq("passNotMatching"), eq("Passwords do not match"));
        verify(studentService, never()).updateStudent(any());
    }

    @Test
    void testChangePassword_IncorrectPassword() {
        // Mock data
        Student student = new Student();
        student.setStudentId("testUser");
        student.setPassword(passwordEncoder.encode("oldPassword"));
        Authentication auth = new UsernamePasswordAuthenticationToken("testUser", "oldPassword");

        when(studentService.getStudentById("testUser")).thenReturn(student);
        when(passwordEncoder.matches("oldPassword", student.getPassword())).thenReturn(false);

        // Execute
        String result = authController.changePassword("wrongPassword", "newPassword", "newPassword", auth, model);

        // Verify
        assertEquals("changepassword", result);
        verify(model, times(1)).addAttribute(eq("verifyPassword"), eq("Incorrect password"));
        verify(studentService, never()).updateStudent(any());
    }

    @Test
    void testChangePassword_PasswordReuse() {
        // Mock data
        Student student = new Student();
        student.setStudentId("testUser");
        student.setPassword(passwordEncoder.encode("oldPassword"));
        Authentication auth = new UsernamePasswordAuthenticationToken("testUser", "oldPassword");

        when(studentService.getStudentById("testUser")).thenReturn(student);
        when(passwordEncoder.matches("oldPassword", student.getPassword())).thenReturn(true);
        when(passwordEncoder.matches("newPassword", student.getPassword())).thenReturn(true);

        // Execute
        String result = authController.changePassword("oldPassword", "newPassword", "newPassword", auth, model);

        // Verify
        assertEquals("changepassword", result);
        verify(model, times(1)).addAttribute(eq("passwordReuse"), eq("Use a different password"));
        verify(studentService, never()).updateStudent(any());
    }
}
