package com.sesc.studentportal.controller;

import com.sesc.studentportal.model.Student;
import com.sesc.studentportal.model.User;
import com.sesc.studentportal.services.StudentService;
import com.sesc.studentportal.services.UserService;
import org.springframework.web.bind.annotation.*;

/***
 * Controller for the Student entity.
 */
@RestController
@RequestMapping("/api/v1/student")
public class StudentController {

    private final StudentService studentService;
    private final UserService userService;

    public StudentController(StudentService studentService, UserService userService) {
        this.studentService = studentService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public Student registerStudent(String user) {
        return studentService.registerStudent(user);
    }

    @GetMapping("/student/{user}")
    public Student getStudentByUser(@PathVariable User user) {
        return userService.findStudentFromUser(user);
    }

}
