package com.moviemanager.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * Data Transfer Object for updating a Movie.
 */
public class MovieUpdateDTO {

    @NotBlank(message = "Title cannot be blank")
    private String title;

    private int releaseYear;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    private List<String> pictures;
    private List<Long> cast;
    
    public MovieUpdateDTO() {
    }

    public MovieUpdateDTO(String title, int releaseYear, String description, List<String> pictures, List<Long> cast) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.description = description;
        this.pictures = pictures;
        this.cast = cast;
    }

    public String getTitle() {
        return title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public List<Long> getCast() {
        return cast;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public void setCast(List<Long> cast) {
        this.cast = cast;
    }
}
