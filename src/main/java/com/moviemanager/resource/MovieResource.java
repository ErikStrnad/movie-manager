package com.moviemanager.resource;

import com.moviemanager.dto.MovieCreateDTO;
import com.moviemanager.dto.MovieUpdateDTO;
import com.moviemanager.entity.Actor;
import com.moviemanager.entity.Movie;
import com.moviemanager.exception.ErrorResponse;
import com.moviemanager.repository.ActorRepository;
import com.moviemanager.repository.MovieRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RESTful resource for managing Movie entities.
 * Provides endpoints for CRUD operations, pagination, and search functionality.
 */
@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieResource {

    @Inject
    MovieRepository movieRepository;

    @Inject
    ActorRepository actorRepository;

    /**
     * GET endpoint for retrieving movies.
     * <p>
     * This endpoint supports both listing all movies and listing movies with pagination.
     * <p>
     * Example requests:
     * - Retrieve all movies (without pagination):
     * GET /movies
     * - Retrieve movies with pagination (second page, 20 movies per page):
     * GET /movies?page=2&size=20
     *
     * @param page Optional page number to display (starts from 1)
     * @param size Optional number of movies per page
     * @return HTTP response with the list of movies or an error
     */
    @GET
    @Operation(summary = "Retrieve movies with optional pagination")
    @APIResponse(responseCode = "200", description = "Successfully retrieved movies")
    @APIResponse(responseCode = "400", description = "Invalid pagination parameters")
    public Response getAllMovies(
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size) {

        final int MAX_SIZE = 100; // Maximum allowed page size

        List<Movie> movies;

        if (page != null && size != null) {
            // Validate pagination parameters
            if (page < 1 || size < 1) {
                return buildErrorResponse(Response.Status.BAD_REQUEST, "Page and size parameters must be positive integers.");
            }

            // Enforce maximum page size
            if (size > MAX_SIZE) {
                size = MAX_SIZE;
            }

            // Use Panache to perform pagination
            PanacheQuery<Movie> query = movieRepository.findAll();

            long totalMovies = query.count(); // Total number of movies
            long totalPages = (totalMovies + size - 1) / size; // Calculate total pages

            // Fetch paginated results
            movies = query.page(Page.of(page - 1, size)).list();

            // Create a response map with pagination metadata
            Map<String, Object> response = new HashMap<>();
            response.put("items", movies);
            response.put("currentPage", page);
            response.put("pageSize", size);
            response.put("totalItems", totalMovies);
            response.put("totalPages", totalPages);

            return Response.ok(response).build(); // Return the response with pagination metadata
        } else if (page == null && size == null) {
            // Retrieve all movies without pagination
            movies = movieRepository.listAll();

            return Response.ok(movies).build(); // Return the list of all movies
        } else {
            // If one of the pagination parameters is missing
            return buildErrorResponse(Response.Status.BAD_REQUEST, "Both page and size parameters must be provided together.");
        }
    }

    /**
     * GET endpoint for retrieving a movie by its IMDb ID.
     * <p>
     * Example request:
     * - Retrieve a movie with IMDb ID "tt0111161":
     * GET /movies/tt0111161
     *
     * @param imdbID IMDb ID of the movie to retrieve
     * @return HTTP response with movie details or an error
     */
    @GET
    @Path("/{imdbID}")
    @Operation(summary = "Retrieve a movie by IMDb ID")
    @APIResponse(responseCode = "200", description = "Movie found")
    @APIResponse(responseCode = "404", description = "Movie not found")
    public Response getMovieById(@PathParam("imdbID") String imdbID) {
        Movie movie = findMovieOrFail(imdbID);
        return Response.ok(movie).build(); // Return response with status 200 OK and movie details
    }

    /**
     * POST endpoint for creating a new movie.
     */
    @POST
    @Transactional
    @Operation(summary = "Create a new movie")
    @APIResponse(responseCode = "201", description = "Movie successfully created")
    @APIResponse(responseCode = "400", description = "Invalid input or actors not found")
    @APIResponse(responseCode = "409", description = "Movie with this IMDb ID already exists")
    public Response createMovie(@Valid MovieCreateDTO movieDTO) {
        if (movieRepository.findById(movieDTO.getImdbID()) != null) {
            return buildErrorResponse(Response.Status.CONFLICT, "Movie with this IMDb ID already exists.");
        }

        List<Actor> actors = validateAndFetchActors(movieDTO.getCast());
        if (actors == null) {
            return buildErrorResponse(Response.Status.BAD_REQUEST, "One or more actors not found.");
        }

        Movie movie = new Movie(
                movieDTO.getImdbID(),
                movieDTO.getTitle(),
                movieDTO.getReleaseYear(),
                movieDTO.getDescription(),
                movieDTO.getPictures(),
                actors
        );
        movieRepository.persist(movie);

        return Response.status(Response.Status.CREATED)
                .entity(movie)
                .build();
    }

    /**
     * PUT endpoint for updating an existing movie's details.
     */
    @PUT
    @Path("/{imdbID}")
    @Transactional
    @Operation(summary = "Update an existing movie")
    @APIResponse(responseCode = "200", description = "Movie successfully updated")
    @APIResponse(responseCode = "400", description = "Invalid input or actors not found")
    @APIResponse(responseCode = "404", description = "Movie not found")
    public Response updateMovie(@PathParam("imdbID") String imdbID, @Valid MovieUpdateDTO movieDTO) {
        // Find the existing movie by IMDb ID
        Movie existingMovie = findMovieOrFail(imdbID);

        // Validate and fetch actors by their IDs
        List<Actor> actors = validateAndFetchActors(movieDTO.getCast());
        if (actors == null) {
            return buildErrorResponse(Response.Status.BAD_REQUEST, "One or more actors not found.");
        }

        // Update movie details
        existingMovie.setTitle(movieDTO.getTitle());
        existingMovie.setReleaseYear(movieDTO.getReleaseYear());
        existingMovie.setDescription(movieDTO.getDescription());
        existingMovie.setPictures(movieDTO.getPictures());
        existingMovie.setCast(actors);

        // Persist the updated movie
        movieRepository.persist(existingMovie);

        return Response.ok(existingMovie).build();
    }

    /**
     * DELETE endpoint for removing a movie by its IMDb ID.
     * <p>
     * Example request:
     * DELETE /movies/tt1234567
     *
     * @param imdbID IMDb ID of the movie to delete
     * @return HTTP response with no content (204) or an error
     */
    @DELETE
    @Path("/{imdbID}")
    @Transactional // Manages the transaction for this method
    @Operation(summary = "Delete a movie by IMDb ID")
    @APIResponse(responseCode = "204", description = "Movie successfully deleted")
    @APIResponse(responseCode = "404", description = "Movie not found")
    public Response deleteMovie(@PathParam("imdbID") String imdbID) {
        // Find the movie by IMDb ID
        Movie movie = findMovieOrFail(imdbID);

        // Delete the movie
        movieRepository.delete(movie);

        return Response.noContent().build(); // Return response with status 204 No Content
    }

    /**
     * GET endpoint for searching movies by title or release year.
     * <p>
     * Example requests:
     * - Search for movies with the title "Inception":
     * GET /movies/search?title=Inception
     * - Search for movies released in 2020:
     * GET /movies/search?year=2020
     * - Search for movies with the title "Inception" and released in 2020:
     * GET /movies/search?title=Inception&year=2020
     * - Retrieve all movies without any search filters:
     * GET /movies/search
     *
     * @param title Title of the movie to search for (optional)
     * @param year  Release year of the movie to search for (optional)
     * @return HTTP response with the list of found movies
     */
    @GET
    @Path("/search")
    @Operation(summary = "Search movies by title or release year")
    @APIResponse(responseCode = "200", description = "Successfully retrieved search results")
    public Response searchMovies(@QueryParam("title") String title, @QueryParam("year") Integer year) {
        List<Movie> movies;
        // Search logic based on the presence of parameters
        if (title != null && year != null) {
            movies = movieRepository.find("title like ?1 and releaseYear = ?2", "%" + title + "%", year).list();
        } else if (title != null) {
            movies = movieRepository.find("title like ?1", "%" + title + "%").list();
        } else if (year != null) {
            movies = movieRepository.find("releaseYear", year).list();
        } else {
            movies = movieRepository.listAll(); // If no parameters, return all movies
        }
        return Response.ok(movies).build(); // Return response with status 200 OK and list of movies
    }

    // -------------------- Helper Methods --------------------

    /**
     * Helper method to retrieve a movie by IMDb ID or throw a 404 error if not found.
     *
     * @param imdbID IMDb ID of the movie
     * @return The found Movie object
     */
    private Movie findMovieOrFail(String imdbID) {
        Movie movie = movieRepository.findById(imdbID);
        if (movie == null) {
            // Throw a 404 Not Found error with an appropriate message
            throw new WebApplicationException(
                    Response.status(Response.Status.NOT_FOUND)
                            .entity(new ErrorResponse("Movie not found."))
                            .build()
            );
        }
        return movie;
    }

    /**
     * Helper method to validate and fetch actors by their IDs.
     *
     * @param actorIds List of actor IDs to validate and fetch
     * @return List of found Actor objects or null if any actor is not found
     */
    private List<Actor> validateAndFetchActors(List<Long> actorIds) {
        if (actorIds == null || actorIds.isEmpty()) {
            return List.of(); // Return an empty list if no actors are provided
        }
        // Fetch actors by their IDs
        List<Actor> actors = actorRepository.find("id in ?1", actorIds).list();
        if (actors.size() != actorIds.size()) {
            return null; // If the number of found actors does not match the number of IDs provided, return null
        }
        return actors; // Return the list of found actors
    }

    /**
     * Helper method to build a Response with an error message.
     *
     * @param status  HTTP status to return
     * @param message Error message
     * @return HTTP Response object containing the error
     */
    private Response buildErrorResponse(Response.Status status, String message) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", message);
        return Response.status(status)
                .entity(errorResponse)
                .build();
    }
}
