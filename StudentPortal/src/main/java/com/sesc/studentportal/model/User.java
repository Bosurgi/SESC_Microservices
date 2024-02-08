package com.sesc.studentportal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String userName;
    @JsonIgnore // It ignores the password field when serializing to JSON
    private String password;
    private String email;
    private Role role;
}
