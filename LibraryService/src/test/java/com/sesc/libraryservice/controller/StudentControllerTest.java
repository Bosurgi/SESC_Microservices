package com.sesc.libraryservice.controller;

import com.sesc.libraryservice.dto.StudentRequest;
import com.sesc.libraryservice.model.Student;
import com.sesc.libraryservice.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class StudentControllerTest {

    @MockBean
    private StudentService studentService;
    
    @Autowired
    private StudentController studentController;

    @BeforeEach
    void setUp() {
        studentService = mock(StudentService.class);
        studentController = new StudentController(studentService);
    }

    @Test
    void testGetAllStudents_Success() {
        // Mock
        List<Student> students = Collections.singletonList(new Student());
        when(studentService.getAllStudents()).thenReturn(students);

        // Execute
        String viewName = studentController.getAllStudents(mock(Model.class));

        // Verify
        assertEquals("admin/students", viewName);
    }

    @Test
    void testGetStudentById_Exists_Success() {
        // Mock
        String studentId = "123";
        Student student = new Student();
        when(studentService.getStudentById(studentId)).thenReturn(student);

        // Execute
        ResponseEntity<Student> response = studentController.getStudentById(studentId);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetStudentById_NotExists_NotFound() {
        // Mock
        String studentId = "456";
        when(studentService.getStudentById(studentId)).thenReturn(null);

        // Execute
        ResponseEntity<Student> response = studentController.getStudentById(studentId);

        // Verify
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateStudent_Success() {
        // Mock
        StudentRequest studentRequest = new StudentRequest("123");
        Student student = new Student();
        when(studentService.createStudent(studentRequest.getStudentId())).thenReturn(student);

        // Execute
        ResponseEntity<Student> response = studentController.createStudent(studentRequest);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testCreateStudent_DuplicateId_BadRequest() {
        // Mock
        StudentRequest studentRequest = new StudentRequest("456");
        when(studentService.createStudent(studentRequest.getStudentId())).thenThrow(new RuntimeException());

        // Execute
        ResponseEntity<Student> response = studentController.createStudent(studentRequest);

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}