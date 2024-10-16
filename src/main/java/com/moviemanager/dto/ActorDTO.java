package com.moviemanager.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

/**
 * Data Transfer Object for Actor.
 */
public class ActorDTO {

    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    private LocalDate birthdate;
    
    public ActorDTO() {
    }

    public ActorDTO(Long id, String name, LocalDate birthdate) {
        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }
}
