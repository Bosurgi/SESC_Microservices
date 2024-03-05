package com.sesc.studentportal.endpoint;

import com.sesc.studentportal.model.Enrolments;
import com.sesc.studentportal.model.Module;
import com.sesc.studentportal.model.Student;
import com.sesc.studentportal.services.EnrolmentsService;
import dev.hilla.Endpoint;
import dev.hilla.exception.EndpointException;
import jakarta.annotation.security.PermitAll;

@Endpoint
@PermitAll
public class EnrolmentEndpoint {

    private final EnrolmentsService enrolmentService;

    public EnrolmentEndpoint(EnrolmentsService enrolmentService) {
        this.enrolmentService = enrolmentService;
    }

    public Enrolments createEnrolment(Student student, Module module) {
        try {
            return enrolmentService.createEnrolment(student, module);
        } catch (Exception e) {
            throw new EndpointException(e + "Student " + student.getStudentId() + " is already enrolled in module " + module.getTitle());
        }
    }
}
