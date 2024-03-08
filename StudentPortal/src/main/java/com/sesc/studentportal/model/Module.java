package com.sesc.studentportal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/***
 * Entity for a module which is a course unit.
 */
@Entity
@Data
@NoArgsConstructor
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moduleId;

    @Column(unique = true)
    @NotBlank
    private String title;
    private String description;
    private Double fee;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "module")
    @JsonIgnore // It ignores the enrolments field when serializing to JSON
    @ToString.Exclude
    private List<Enrolments> enrolments = new ArrayList<>();

    /***
     * Constructor for a module with title, description and fee
     * @param title the title of the module
     * @param description the description of the module
     * @param fee the cost for the module
     */
    public Module(String title, String description, Double fee) {
        this.title = title;
        this.description = description;
        this.fee = fee;
    }
}

