package com.sesc.studentportal.services;

import com.sesc.studentportal.dto.Account;
import com.sesc.studentportal.dto.Invoice;
import com.sesc.studentportal.model.Enrolments;
import com.sesc.studentportal.model.Module;
import com.sesc.studentportal.model.Student;
import com.sesc.studentportal.repository.EnrolmentRepository;
import dev.hilla.BrowserCallable;
import jakarta.annotation.security.PermitAll;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@BrowserCallable
@PermitAll
public class EnrolmentsService {

    private final EnrolmentRepository enrolmentRepository;
    private final IntegrationService integrationService;

    public EnrolmentsService(EnrolmentRepository enrolmentRepository, IntegrationService integrationService) {
        this.enrolmentRepository = enrolmentRepository;
        this.integrationService = integrationService;
    }

    /**
     * It enrols a student to a module and creates the entry in the database.
     *
     * @param student the student to enrol
     * @param module  the module the student is enrolling to
     * @return the Enrolment
     * @throws Exception if the student is already enrolled in the module
     */
    public Enrolments createEnrolment(Student student, Module module) throws Exception {
        // Checking if the student is already enrolled in the module
        if (enrolmentRepository.findEnrolmentsByModuleAndStudent(module, student) != null) {
            // TODO: Create custom Exception
            throw new Exception("Student " + student.getStudentId() + " is already enrolled in module " + module.getModuleId());
        }
        return enrolmentRepository.save(new Enrolments(student, module));
    }

    /**
     * It gets all the enrolments belonging to a student
     *
     * @param student the student to look for
     * @return a list of enrolments
     */
    public List<Enrolments> getAllEnrolmentsFromStudent(Student student) {
        return enrolmentRepository.findEnrolmentsByStudent(student);
    }

    /**
     * It gets all the modules a student is enrolled in by their student number.
     *
     * @param studentNumber the student number
     * @return a list of modules
     */
    public List<Module> getModulesFromEnrolments(String studentNumber) {
        List<Enrolments> enrolments = enrolmentRepository.findEnrolmentsByStudent_StudentNumber(studentNumber);
        return enrolments.stream().map(Enrolments::getModule).toList();
    }

    /**
     * It creates an invoice for the tuition fees of a module and sends it to the finance service through
     * the integration Service
     *
     * @param student the Student to create the invoice for
     * @param module  the Module to create the invoice for
     * @return the created invoice
     */
    public Invoice createInvoice(Student student, Module module) {
        Account account = new Account();
        account.setStudentId(student.getStudentNumber());

        // Creating the invoice setting its Tuition Fees and setting the Due Date to 6 months
        Invoice invoice = new Invoice();
        invoice.setAccount(account);
        invoice.setAmount(module.getFee());
        invoice.setDueDate(LocalDate.now().plusMonths(6));
        invoice.setType(Invoice.Type.TUITION_FEES);
        return integrationService.createCourseFeeInvoice(invoice);
    }

}
