package com.sesc.libraryservice.controller;

import com.sesc.libraryservice.dto.LoginDto;
import com.sesc.libraryservice.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(
            AuthenticationManager authenticationManager,
            StudentRepository studentRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Login endpoint to authenticate a user
     *
     * @param loginDto the POJO for the login request
     * @return Success message and HTTP Status code
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getStudentId(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return ResponseEntity.ok("Logged in");
    }

}
