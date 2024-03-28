package com.sesc.libraryservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id")
    private String studentId;

    private String password;

    private String role;

    private boolean isFirstLogin;

    @Transient
    private Long overdueCount;

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    @ToString.Exclude
    private List<Transaction> transactions;

    public Student(String studentId, String password, String role, boolean isFirstLogin) {
        this.studentId = studentId;
        this.password = password;
        this.role = role;
        this.isFirstLogin = isFirstLogin;
    }
}