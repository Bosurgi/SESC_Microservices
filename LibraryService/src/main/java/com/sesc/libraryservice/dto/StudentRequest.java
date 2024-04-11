package com.sesc.libraryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for the student request object used for registration
 */
@Data
@AllArgsConstructor
public class StudentRequest {
    private String studentId;
}
