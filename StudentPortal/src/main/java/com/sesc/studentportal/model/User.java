package com.sesc.studentportal.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Data
@Table(name = "users")
//@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Column(name = "userId")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(name = "username", unique = true)
    private String username;
    private String password;
    private String firstname;
    private String surname;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "roles", updatable = true, insertable = true)
    private String roles;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "user_student",
            joinColumns = {@JoinColumn(name = "userId", referencedColumnName = "userId")},
            inverseJoinColumns = {@JoinColumn(name = "studentId", referencedColumnName = "studentId")})
    @ToString.Exclude
    private Student student;

    public User(String userName, String password, String roles) {
        this.username = userName;
        this.password = password;
        this.roles = roles;
    }

    public User(String userName, String password, String roles, String firstname, String surname, String email) {
        this.username = userName;
        this.password = password;
        this.roles = roles;
        this.firstname = firstname;
        this.surname = surname;
        this.email = email;
    }


}
