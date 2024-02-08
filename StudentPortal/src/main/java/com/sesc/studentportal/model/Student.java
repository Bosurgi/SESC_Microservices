package com.sesc.studentportal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

/***
 * Entity for a student which extends the User class
 */
@Entity
//@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Getter
@Setter
@Data
@Table(name = "students")
public class Student {
    //        extends User {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studentId")
    private Long studentId;

    @NotBlank
    @Column(unique = true)
    private String studentNumber;

    private String surname;

    private String firstName;

    private Boolean isEnrolled;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Enrolments> enrolments = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;


    /***
     * Generate a student id if it is not already set
     */
    private void generateId() {
        if (this.studentNumber == null) {
            this.studentNumber = "c" + RandomStringUtils.random(1, '7', '3') +
                    RandomStringUtils.randomNumeric(6);
        }
    } // End of Method

    // CONSTRUCTORS //


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
