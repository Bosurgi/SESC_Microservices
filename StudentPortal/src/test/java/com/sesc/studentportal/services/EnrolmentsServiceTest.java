package com.sesc.studentportal.services;

import com.sesc.studentportal.dto.Account;
import com.sesc.studentportal.dto.Invoice;
import com.sesc.studentportal.model.Enrolments;
import com.sesc.studentportal.model.Module;
import com.sesc.studentportal.model.Student;
import com.sesc.studentportal.repository.EnrolmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class EnrolmentsServiceTest {

    @Mock
    private EnrolmentRepository enrolmentRepository;

    @Mock
    private IntegrationService integrationService;

    @InjectMocks
    private EnrolmentsService enrolmentsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEnrolment() throws Exception {
        // Arrange
        Student student = new Student("Mario", "Rossi");
        Module module = new Module("Module 1", "Module 1 Description", 100.0);

        when(enrolmentRepository.findEnrolmentsByModuleAndStudent(module, student)).thenReturn(null);
        when(enrolmentRepository.save(any(Enrolments.class))).thenReturn(new Enrolments(student, module));

        // Act
        Enrolments result = enrolmentsService.createEnrolment(student, module);

        // Assert
        assertNotNull(result);
        assertEquals(student, result.getStudent());
        assertEquals(module, result.getModule());
    }

    @Test
    void testCreateEnrolmentWhenStudentAlreadyEnrolled() {
        // Arrange
        Student student = new Student("Mario", "Rossi");
        Module module = new Module("Module 1", "Module 1 Description", 100.0);

        when(enrolmentRepository.findEnrolmentsByModuleAndStudent(module, student)).thenReturn(new Enrolments(student, module));

        // Act & Assert
        assertThrows(Exception.class, () -> enrolmentsService.createEnrolment(student, module));
    }

    @Test
    void testGetAllEnrolmentsFromStudent() {
        // Arrange
        Student student = new Student("Mario", "Rossi");
        Module module = new Module("Module 1", "Module 1 Description", 100.0);
        Enrolments enrolments = new Enrolments(student, module);

        when(enrolmentRepository.findEnrolmentsByStudent(student)).thenReturn(Collections.singletonList(enrolments));

        // Act
        List<Enrolments> result = enrolmentsService.getAllEnrolmentsFromStudent(student);

        // Assert
        assertEquals(1, result.size());
        assertEquals(enrolments, result.get(0));
    }

    @Test
    void testGetModulesFromEnrolments() {
        // Arrange
        String studentNumber = "123456";
        Student student = new Student("Mario", "Rossi");
        Module module = new Module("Module 1", "Module 1 Description", 100.0);
        Enrolments enrolments = new Enrolments(student, module);

        when(enrolmentRepository.findEnrolmentsByStudent_StudentNumber(studentNumber)).thenReturn(Collections.singletonList(enrolments));

        // Act
        List<Module> result = enrolmentsService.getModulesFromEnrolments(studentNumber);

        // Assert
        assertEquals(1, result.size());
        assertEquals(module, result.get(0));
    }

    @Test
    void testCreateInvoice() {
        // Arrange
        Student student = new Student("Mario", "Rossi");
        Module module = new Module("Module 1", "Module 1 Description", 100.0);

        Account account = new Account();
        account.setStudentId(student.getStudentNumber());

        Invoice invoice = new Invoice();
        invoice.setAccount(account);
        invoice.setAmount(module.getFee());
        invoice.setDueDate(LocalDate.now().plusMonths(6));
        invoice.setType(Invoice.Type.TUITION_FEES);

        when(integrationService.createCourseFeeInvoice(any(Invoice.class))).thenReturn(invoice);

        // Act
        Invoice result = enrolmentsService.createInvoice(student, module);

        // Assert
        assertNotNull(result);
        assertEquals(account, result.getAccount());
        assertEquals(module.getFee(), result.getAmount());
        assertEquals(LocalDate.now().plusMonths(6), result.getDueDate());
        assertEquals(Invoice.Type.TUITION_FEES, result.getType());
    }
}