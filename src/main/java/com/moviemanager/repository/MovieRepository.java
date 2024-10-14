package com.moviemanager.repository;

import com.moviemanager.entity.Movie;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository for managing Movie entities.
 */
@ApplicationScoped
public class MovieRepository implements PanacheRepositoryBase<Movie, String> {
}
