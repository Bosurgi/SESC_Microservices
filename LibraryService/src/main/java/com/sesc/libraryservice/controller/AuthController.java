package com.sesc.libraryservice.controller;

import com.sesc.libraryservice.dto.LoginDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "login";
    }

//    @PostMapping("/login")
//    public String login(@RequestBody LoginDto loginDto) {
//        Authentication auth =
//                UsernamePasswordAuthenticationToken.unauthenticated(loginDto.getStudentId(), loginDto.getPassword());
//        Authentication authResponse = this.authenticationManager.authenticate(auth);
//        SecurityContextHolder.getContext().setAuthentication(authResponse);
//        return "changepassword";
//    }
}
