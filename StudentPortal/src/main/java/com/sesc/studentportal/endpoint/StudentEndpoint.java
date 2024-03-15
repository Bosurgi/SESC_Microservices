package com.sesc.studentportal.endpoint;

import com.sesc.studentportal.model.Student;
import com.sesc.studentportal.model.User;
import com.sesc.studentportal.services.StudentService;
import com.sesc.studentportal.services.UserService;
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
    private final UserService userService;

    public StudentEndpoint(StudentService studentService, UserService userService) {
        this.studentService = studentService;
        this.userService = userService;
    }

    public Student registerStudent(String user) {
        return studentService.registerStudent(user);
    }

    public Student getStudentByUser(User user) {
        return userService.findStudentFromUser(user);
    }

    public Student updateStudent(Student student) {
        return studentService.updateStudent(student);
    }

}
