package com.sesc.studentportal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/***
 * Entity for a module which is a course unit.
 */
@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
public class Module {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long moduleId;

    @Column(unique = true)
    @NotBlank
    private String title;
    private String description;
    private Double fee;

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

