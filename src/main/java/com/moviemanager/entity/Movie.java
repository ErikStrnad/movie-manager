package com.moviemanager.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

/**
 * Entity representing a Movie.
 */
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
    @CollectionTable(name = "movie_pictures", joinColumns = @JoinColumn(name = "imdbID"))
    private List<String> pictures;

    @ManyToMany
    @JoinTable(
            name = "movie_cast",
            joinColumns = @JoinColumn(name = "movie_imdbID"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private List<Actor> cast;

    public Movie() {
    }

    public Movie(String imdbID, String title, int releaseYear, String description, List<String> pictures, List<Actor> cast) {
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

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
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

    public List<Actor> getCast() {
        return cast;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public void setCast(List<Actor> cast) {
        this.cast = cast;
    }
}
