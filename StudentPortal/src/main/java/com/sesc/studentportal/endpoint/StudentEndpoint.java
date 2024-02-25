package com.sesc.studentportal.endpoint;

import com.sesc.studentportal.model.Student;
import com.sesc.studentportal.services.StudentService;
import dev.hilla.Endpoint;
import jakarta.annotation.security.PermitAll;

@Endpoint
@PermitAll
/**
 * Endpoint for the Student entity where the Endpoints to access the Student entity are defined.
 * The Endpoints can be used internally on the Student Portal by Hilla.
 * The Controllers are needed to communicate with different services using REST API.
 */
public class StudentEndpoint {
    private final StudentService studentService;

    public StudentEndpoint(StudentService studentService) {
        this.studentService = studentService;
    }

    public Student registerStudent(String user) {
        return studentService.registerStudent(user);
    }

}
