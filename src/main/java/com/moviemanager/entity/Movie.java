package com.moviemanager.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

@Entity
public class Movie extends PanacheEntityBase {

    @Id
    @NotBlank(message = "IMDB ID cannot be blank")
    private String imdbID;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @Column(nullable = false)
    private int releaseYear;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @ElementCollection
    private List<String> pictures;

    public Movie() {
    }

    public Movie(String imdbID, String title, int releaseYear, String description, List<String> pictures) {
        this.imdbID = imdbID;
        this.title = title;
        this.releaseYear = releaseYear;
        this.description = description;
        this.pictures = pictures;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }
}
