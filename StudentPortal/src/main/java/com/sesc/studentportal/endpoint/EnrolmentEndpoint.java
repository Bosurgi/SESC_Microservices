package com.sesc.studentportal.endpoint;

import com.sesc.studentportal.dto.Invoice;
import com.sesc.studentportal.model.Enrolments;
import com.sesc.studentportal.model.Module;
import com.sesc.studentportal.model.Student;
import com.sesc.studentportal.services.EnrolmentsService;
import dev.hilla.Endpoint;
import dev.hilla.exception.EndpointException;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@Endpoint
@PermitAll
public class EnrolmentEndpoint {

    private final EnrolmentsService enrolmentService;

    public EnrolmentEndpoint(EnrolmentsService enrolmentService) {
        this.enrolmentService = enrolmentService;
    }

    /**
     * It enrols a student to a module and creates the entry in the database.
     *
     * @param student the student to enrol
     * @param module  the module the student is enrolling to
     * @return the Enrolment
     */
    public Enrolments createEnrolment(Student student, Module module) {
        try {
            return enrolmentService.createEnrolment(student, module);
        } catch (Exception e) {
            throw new EndpointException(e + "Student " + student.getStudentId() + " is already enrolled in module " + module.getTitle());
        }
    }

    /**
     * It gets all the enrolments belonging to a student
     *
     * @param student the student with the enrolments to look for
     * @return a list of enrolments
     */
    public List<Enrolments> getAllEnrolmentsFromStudent(Student student) {
        return enrolmentService.getAllEnrolmentsFromStudent(student);
    }

    /**
     * It gets all the modules a student is enrolled in by their student number.
     *
     * @param studentNumber the Student Unique Number
     * @return a list of Modules the student is enrolled to
     */
    public List<Module> getModulesFromEnrolments(String studentNumber) {
        return enrolmentService.getModulesFromEnrolments(studentNumber);
    }

    /**
     * Creating Student Invoice
     *
     * @param student the student to create the invoice for
     * @param module  the module the student is enrolling to
     * @return the Invoice
     */
    public Invoice createInvoice(Student student, Module module) {
        return enrolmentService.createInvoice(student, module);
    }
}
