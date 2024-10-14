package com.moviemanager.repository;

import com.moviemanager.entity.Actor;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository for managing Actor entities.
 */
@ApplicationScoped
public class ActorRepository implements PanacheRepository<Actor> {
}
