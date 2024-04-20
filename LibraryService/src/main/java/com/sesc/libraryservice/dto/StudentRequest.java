package com.sesc.libraryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for the student request object used for registration
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequest {
    private String studentId;
}
