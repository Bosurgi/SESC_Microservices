package com.sesc.libraryservice.dto;

import lombok.Data;

/**
 * DTO for the login request object
 */
@Data
public class LoginDto {
    private String studentId;
    private String password;
}
