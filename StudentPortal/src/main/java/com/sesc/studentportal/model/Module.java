package com.sesc.studentportal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/***
 * Entity for a module which is a course unit.
 */
@Entity
@Data
@Getter
@Setter
public class Module {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Double fee;

    /***
     * Default constructor for a module
     */
    public Module() {
        // Empty Constructor
    }

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
