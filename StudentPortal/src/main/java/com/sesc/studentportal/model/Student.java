package com.sesc.studentportal.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

@Entity
@Data
public class Student {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    private String studentId;

    private String surname;

    private String firstName;


    /***
     * Generate a student id if it is not already set
     */
    private void generateId() {
        if ( this.studentId == null ) {
            this.studentId = "c" + RandomStringUtils.random(1, '7', '3') +
                    RandomStringUtils.randomNumeric(6);
        }
    } // End of Method

    // CONSTRUCTORS //

    /***
     * Default constructor
     */
    public Student() {
    }

    /***
     * Constructor with surname and first name and generates a student ID
     * @param surname the surname of the student
     * @param firstName the first name of the student
     */
    public Student(String surname, String firstName) {
        this.surname = surname;
        this.firstName = firstName;
        // Generating a student ID
        generateId();
    }

} // End of Class
