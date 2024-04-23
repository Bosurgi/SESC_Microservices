package com.sesc.studentportal.endpoint;

import com.sesc.studentportal.dto.Invoice;
import com.sesc.studentportal.model.Enrolments;
import com.sesc.studentportal.model.Module;
import com.sesc.studentportal.model.Student;
import com.sesc.studentportal.services.EnrolmentsService;
import dev.hilla.exception.EndpointException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class EnrolmentEndpointTest {

    @Mock
    private EnrolmentsService enrolmentService;

    @InjectMocks
    private EnrolmentEndpoint enrolmentEndpoint;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEnrolment() throws Exception {
        // Arrange
        Student student = new Student("John", "Doe");
        Module module = new Module("Module 1", "Module 1 Description", 100.0);
        Enrolments enrolment = new Enrolments(student, module);

        when(enrolmentService.createEnrolment(student, module)).thenReturn(enrolment);

        // Act
        Enrolments result = enrolmentEndpoint.createEnrolment(student, module);

        // Assert
        assertNotNull(result);
        assertEquals(enrolment, result);
    }

    @Test
    void testCreateEnrolmentWhenStudentAlreadyEnrolled() throws Exception {
        // Arrange
        Student student = new Student("Mario", "Rossi");
        Module module = new Module("Module 1", "Module 1 Description", 100.0);

        when(enrolmentService.createEnrolment(student, module)).thenThrow(new Exception());

        // Act & Assert
        assertThrows(EndpointException.class, () -> enrolmentEndpoint.createEnrolment(student, module));
    }

    @Test
    void testGetAllEnrolmentsFromStudent() {
        // Arrange
        Student student = new Student("Mario", "Rossi");
        Module module = new Module("Module 1", "Module 1 Description", 100.0);
        Enrolments enrolment = new Enrolments(student, module);

        when(enrolmentService.getAllEnrolmentsFromStudent(student)).thenReturn(Collections.singletonList(enrolment));

        // Act
        List<Enrolments> result = enrolmentEndpoint.getAllEnrolmentsFromStudent(student);

        // Assert
        assertEquals(1, result.size());
        assertEquals(enrolment, result.get(0));
    }

    @Test
    void testGetModulesFromEnrolments() {
        // Arrange
        String studentNumber = "123456";
        Module module = new Module("Module 1", "Module 1 Description", 100.0);

        when(enrolmentService.getModulesFromEnrolments(studentNumber)).thenReturn(Collections.singletonList(module));

        // Act
        List<Module> result = enrolmentEndpoint.getModulesFromEnrolments(studentNumber);

        // Assert
        assertEquals(1, result.size());
        assertEquals(module, result.get(0));
    }

    @Test
    void testCreateInvoice() {
        // Arrange
        Student student = new Student("Mario", "Rossi");
        Module module = new Module("Module 1", "Module 1 Description", 100.0);
        Invoice invoice = new Invoice();

        when(enrolmentService.createInvoice(student, module)).thenReturn(invoice);

        // Act
        Invoice result = enrolmentEndpoint.createInvoice(student, module);

        // Assert
        assertNotNull(result);
        assertEquals(invoice, result);
    }
}