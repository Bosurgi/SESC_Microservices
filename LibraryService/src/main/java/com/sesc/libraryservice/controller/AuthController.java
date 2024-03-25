package com.sesc.libraryservice.controller;

import com.sesc.libraryservice.dto.LoginDto;
import com.sesc.libraryservice.model.Student;
import com.sesc.libraryservice.service.StudentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final StudentService studentService;

    private final PasswordEncoder passwordEncoder;

    public AuthController(StudentService studentService, PasswordEncoder passwordEncoder) {
        this.studentService = studentService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "login";
    }

    @GetMapping("/changepassword")
    public String changePassword() {
        return "changepassword";
    }

    /**
     * Handles the change password request from the user
     *
     * @param currentPassword the current password
     * @param newPassword     the new password
     * @param confirmPassword confirmation password
     * @param auth            the authentication object to fetch the current user
     * @return the redirect URL
     */
    @PostMapping("/changepassword")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Authentication auth) {

        // Validate if the new password and confirm password match
        if (!newPassword.equals(confirmPassword)) {
            // Handle password mismatch error
            // TODO: Password mismatch error handling below to implement below is an example
            return "redirect:/changepassword?error=passwordMismatch";
        }

        // Retrieve the student from the authentication object
        Student student = studentService.getStudentById(auth.getName());
        System.out.println("Authorities: " + auth.getAuthorities());

        // TODO: Implement the password validation logic below here

        newPassword = passwordEncoder.encode(newPassword);
        // Update the student's password, role, and first login status
        student.setPassword(newPassword);
        student.setFirstLogin(false);
        student.setRole("REGISTERED");

        // Save the updated student information
        studentService.updateStudent(student);

        // Redirect to the home page or any other appropriate page
        return "redirect:/";
    }

}
