package com.sesc.libraryservice.controller;

import com.sesc.libraryservice.dto.LoginDto;
import com.sesc.libraryservice.model.Student;
import com.sesc.libraryservice.service.StudentService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

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
                                 Authentication auth,
                                 Model model) {
        // Validate if the new password and confirm password match
        if (!newPassword.equals(confirmPassword)) {
            // Handle password mismatch error
            model.addAttribute("passNotMatching", "Passwords do not match");
            return "changepassword";
        }

        // Retrieve the student from the authentication object
        Student student = studentService.getStudentById(auth.getName());

        // Check if the password is correct
        if (!passwordEncoder.matches(currentPassword, student.getPassword())) {
            // Handle incorrect password error
            model.addAttribute("verifyPassword", "Incorrect password");
            return "changepassword";
        }

        // Check if the new password is the same as the current password
        if (passwordEncoder.matches(newPassword, student.getPassword())) {
            // Handle password reuse error
            model.addAttribute("passwordReuse", "Use a different password");
            return "changepassword";
        }

        newPassword = passwordEncoder.encode(newPassword);
        // Update the student's password, role, and first login status
        student.setPassword(newPassword);
        student.setFirstLogin(false);
        student.setRole("REGISTERED");

        // Save the updated student information
        studentService.updateStudent(student);

        // Update the authentication object with the new role to give user access to the website
        updateAuthenticationWithRole(auth, student.getRole());

        // Redirect to the home page or any other appropriate page
        return "redirect:/";
    }

    /**
     * Helper method to update the authentication object with the new role
     *
     * @param auth the authentication object
     * @param role the new role to add
     */
    private void updateAuthenticationWithRole(Authentication auth, String role) {
        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
        updatedAuthorities.add(new SimpleGrantedAuthority(role));

        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
