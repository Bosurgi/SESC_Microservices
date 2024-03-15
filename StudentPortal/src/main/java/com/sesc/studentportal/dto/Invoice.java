package com.sesc.studentportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    private Long id;
    private String reference;
    private Double amount;
    private LocalDate dueDate;
    private Type type;
    private Status status;
    private Account account;

    public enum Type {
        LIBRARY_FINE,
        TUITION_FEES
    }

    public enum Status {
        OUTSTANDING,
        PAID,
        CANCELLED
    }
}
