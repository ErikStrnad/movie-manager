package com.moviemanager.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * Data Transfer Object for Movie.
 */
public class MovieDTO {

    @NotBlank(message = "IMDB ID cannot be blank")
    private String imdbID;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    private int releaseYear;
    private List<String> pictures;
    private List<Long> cast; // List of Actor IDs

    public MovieDTO() {
    }

    public MovieDTO(String imdbID, String title, int releaseYear, String description, List<String> pictures, List<Long> cast) {
        this.imdbID = imdbID;
        this.title = title;
        this.releaseYear = releaseYear;
        this.description = description;
        this.pictures = pictures;
        this.cast = cast;
    }

    public String getImdbID() {
        return imdbID;
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

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
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
