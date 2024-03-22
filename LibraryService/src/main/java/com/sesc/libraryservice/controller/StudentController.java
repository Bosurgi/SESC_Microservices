package com.sesc.libraryservice.controller;

import com.sesc.libraryservice.dto.StudentRequest;
import com.sesc.libraryservice.model.Student;
import com.sesc.libraryservice.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    /**
     * Get a student by the studentId
     *
     * @param studentId the studentId to search for
     * @return the student entity along with HTTP Status code.
     */
    @GetMapping("/students/{studentId}")
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
    @PostMapping("/students/register/")
    public ResponseEntity<Student> createStudent(@RequestBody StudentRequest studentRequest) {
        try {
            Student student = studentService.createStudent(studentRequest.getStudentId());
            return ResponseEntity.ok(student);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
