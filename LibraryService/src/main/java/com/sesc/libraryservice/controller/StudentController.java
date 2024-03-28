package com.sesc.libraryservice.controller;

import com.sesc.libraryservice.dto.StudentRequest;
import com.sesc.libraryservice.model.Student;
import com.sesc.libraryservice.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Get the student page with all the students and their overdue
     *
     * @param model the model to add the students to
     * @return the students page
     */
    @GetMapping()
    public String getAllStudents(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "admin/students";
    }

    /**
     * Get a student by the studentId
     *
     * @param studentId the studentId to search for
     * @return the student entity along with HTTP Status code.
     */
    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable String studentId) {
        Optional<Student> student = Optional.ofNullable(studentService.getStudentById(studentId));
        return student.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Post request endpoint to create a student
     *
     * @param studentRequest the POJO of the student to create
     * @return the created student entity and HTTP status code
     */
    @PostMapping("/register/")
    public ResponseEntity<Student> createStudent(@RequestBody StudentRequest studentRequest) {
        try {
            Student student = studentService.createStudent(studentRequest.getStudentId());
            return ResponseEntity.ok(student);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
