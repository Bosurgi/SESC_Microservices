package com.sesc.studentportal.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
    @Column(name = "username")
    private String username;
    private String password;
    private String firstname;
    private String surname;
    private String email;
    private String roles;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId")
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
