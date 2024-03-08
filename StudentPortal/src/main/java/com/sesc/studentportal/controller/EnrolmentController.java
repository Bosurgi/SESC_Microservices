package com.sesc.studentportal.controller;

import com.sesc.studentportal.model.Enrolments;
import com.sesc.studentportal.model.Module;
import com.sesc.studentportal.model.Student;
import com.sesc.studentportal.services.EnrolmentsService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.exception.EndpointException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrolment")
@AnonymousAllowed
public class EnrolmentController {

    private final EnrolmentsService enrolmentService;

    public EnrolmentController(EnrolmentsService enrolmentService) {
        this.enrolmentService = enrolmentService;
    }

    @PostMapping("/enrol")
    public Enrolments createEnrolment(Student student, Module module) {
        try {
            return enrolmentService.createEnrolment(student, module);
        } catch (Exception e) {
            throw new EndpointException(e + "Student " + student.getStudentId() + " is already enrolled in module " + module.getTitle());
        }
    }

    @GetMapping("/enrolments/{student}")
    public List<Enrolments> getAllEnrolmentsFromStudent(@PathVariable Student student) {
        return enrolmentService.getAllEnrolmentsFromStudent(student);
    }

//    @GetMapping("/modules/{student}")
//    public List<Module> getModulesFromEnrolments(@PathVariable Student student) {
//        return enrolmentService.getModulesFromEnrolments(student);
//    }

    @GetMapping("/modules/{studentNumber}")
    public List<Module> getModulesFromEnrolments(@PathVariable String studentNumber) {
        return enrolmentService.getModulesFromEnrolments(studentNumber);
    }

}
