package com.moviemanager.resource;

import com.moviemanager.entity.Movie;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieResource {

    // GET all movies
    @GET
    public List<Movie> getAllMovies() {
        return Movie.listAll();
    }

    // GET a specific movie by imdbID
    @GET
    @Path("/{imdbID}")
    public Response getMovie(@PathParam("imdbID") String imdbID) {
        Movie movie = Movie.findById(imdbID);
        if (movie == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage("Movie not found"))
                    .build();
        }
        return Response.ok(movie).build();
    }

    // CREATE a new movie
    @POST
    @Transactional
    public Response createMovie(@Valid Movie movie) {
        if (Movie.findById(movie.getImdbID()) != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorMessage("Movie with this IMDB ID already exists"))
                    .build();
        }
        movie.persist();
        return Response.status(Response.Status.CREATED)
                .entity(movie)
                .build();
    }

    // UPDATE an existing movie
    @PUT
    @Path("/{imdbID}")
    @Transactional
    public Response updateMovie(@PathParam("imdbID") String imdbID, @Valid Movie updatedMovie) {
        Movie movie = Movie.findById(imdbID);
        if (movie == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage("Movie not found"))
                    .build();
        }
        movie.setTitle(updatedMovie.getTitle());
        movie.setReleaseYear(updatedMovie.getReleaseYear());
        movie.setDescription(updatedMovie.getDescription());
        movie.setPictures(updatedMovie.getPictures());
        movie.persist();
        return Response.ok(movie).build();
    }

    // DELETE a movie
    @DELETE
    @Path("/{imdbID}")
    @Transactional
    public Response deleteMovie(@PathParam("imdbID") String imdbID) {
        boolean deleted = Movie.deleteById(imdbID);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage("Movie not found"))
                    .build();
        }
        return Response.noContent().build();
    }

    // Inner class for error messages
    public static class ErrorMessage {
        private String message;

        public ErrorMessage() {
        }

        public ErrorMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}