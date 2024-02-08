package com.sesc.studentportal.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/***
 * Entity for a student's enrolment to a module
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="enrolments")
public class Enrolments {
    // Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrolmentId;
    // Foreign Key to Student
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;
    // Foreign Key to Module
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    private Module module;

    // CONSTRUCTORS //

    /***
     * Constructor for an enrolment with a student and module
     * @param student the student
     * @param module the module
     */
    public Enrolments(Student student, Module module) {
        this.student = student;
        this.module = module;
    }
}

