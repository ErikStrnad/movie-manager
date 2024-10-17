package com.moviemanager.resource;

import com.moviemanager.entity.Actor;
import com.moviemanager.entity.Movie;
import com.moviemanager.repository.ActorRepository;
import com.moviemanager.repository.MovieRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for the MovieResource REST endpoints.
 */
@QuarkusTest
public class MovieResourceTest {

    @Inject
    ActorRepository actorRepository;

    @Inject
    MovieRepository movieRepository;

    // Actor IDs after persisting
    private Long actorId1;
    private Long actorId2;
    private Long actorId3;
    private Long actorId4;
    private Long actorId5;
    private Long actorId6;
    private Long actorId7;
    private Long actorId8;

    /**
     * Resets the database state before each test.
     * Ensures that tests are isolated and do not interfere with each other.
     */
    @BeforeEach
    @Transactional
    public void resetDatabase() {
        movieRepository.deleteAll();
        actorRepository.deleteAll();

        // Create and persist Actors
        Actor actor1 = new Actor("Tim Robbins", LocalDate.of(1958, 10, 16));
        Actor actor2 = new Actor("Morgan Freeman", LocalDate.of(1937, 6, 1));
        Actor actor3 = new Actor("Leonardo DiCaprio", LocalDate.of(1974, 11, 11));
        Actor actor4 = new Actor("Liam Neeson", LocalDate.of(1952, 6, 7));
        Actor actor5 = new Actor("Viggo Mortensen", LocalDate.of(1958, 10, 20));
        Actor actor6 = new Actor("Mark Hamill", LocalDate.of(1951, 9, 25));
        Actor actor7 = new Actor("Brad Pitt", LocalDate.of(1963, 12, 18));
        Actor actor8 = new Actor("Elijah Wood", LocalDate.of(1981, 1, 28));

        actorRepository.persistAndFlush(actor1);
        actorRepository.persistAndFlush(actor2);
        actorRepository.persistAndFlush(actor3);
        actorRepository.persistAndFlush(actor4);
        actorRepository.persistAndFlush(actor5);
        actorRepository.persistAndFlush(actor6);
        actorRepository.persistAndFlush(actor7);
        actorRepository.persistAndFlush(actor8);

        actorId1 = actor1.getId();
        actorId2 = actor2.getId();
        actorId3 = actor3.getId();
        actorId4 = actor4.getId();
        actorId5 = actor5.getId();
        actorId6 = actor6.getId();
        actorId7 = actor7.getId();
        actorId8 = actor8.getId();

        // Create and persist Movies with associated cast
        Movie movie1 = new Movie(
                "tt0111161",
                "The Shawshank Redemption",
                1994,
                "Two imprisoned men bond over a number of years...",
                Arrays.asList(
                        "http://example.com/shawshankredemption.jpg",
                        "http://example.com/shawshank_cast.jpg"),
                Arrays.asList(actor1, actor2)
        );

        Movie movie2 = new Movie(
                "tt0068646",
                "The Godfather",
                1972,
                "The aging patriarch of an organized crime dynasty...",
                Arrays.asList(
                        "http://example.com/thegodfather.jpg",
                        "http://example.com/godfather_cast.jpg"),
                Arrays.asList(actor3, actor4)
        );

        Movie movie3 = new Movie(
                "tt1375666",
                "Inception",
                2010,
                "A thief who steals corporate secrets through the use of dream-sharing technology...",
                Arrays.asList(
                        "http://example.com/inception.jpg",
                        "http://example.com/inception_cast.jpg"),
                List.of(actor3)
        );

        Movie movie4 = new Movie(
                "tt0108052",
                "Schindler's List",
                1993,
                "In German-occupied Poland during World War II...",
                Arrays.asList(
                        "http://example.com/schindlerlist.jpg",
                        "http://example.com/schindlerlist_cast.jpg"),
                List.of(actor4)
        );

        Movie movie5 = new Movie(
                "tt0167260",
                "The Lord of the Rings: The Return of the King",
                2003,
                "Gandalf and Aragorn lead the World of Men against Sauron's army...",
                Arrays.asList(
                        "http://example.com/lotr_returnking.jpg",
                        "http://example.com/lotr_returnking_cast.jpg"),
                List.of(actor5)
        );

        Movie movie6 = new Movie(
                "tt0080684",
                "Star Wars: Episode V - The Empire Strikes Back",
                1980,
                "After the Rebels are brutally overpowered by the Empire on the ice planet Hoth...",
                Arrays.asList(
                        "http://example.com/empirestrikesback.jpg",
                        "http://example.com/empirestrikesback_cast.jpg"),
                List.of(actor6)
        );

        Movie movie7 = new Movie(
                "tt0137523",
                "Fight Club",
                1999,
                "An insomniac office worker and a devil-may-care soap maker form an underground fight club...",
                Arrays.asList(
                        "http://example.com/fightclub.jpg",
                        "http://example.com/fightclub_cast.jpg"),
                List.of(actor7)
        );

        Movie movie8 = new Movie(
                "tt0120737",
                "The Lord of the Rings: The Fellowship of the Ring",
                2001,
                "A meek Hobbit from the Shire and eight companions set out on a journey to destroy the powerful One Ring...",
                Arrays.asList(
                        "http://example.com/lotr_fellowship.jpg",
                        "http://example.com/lotr_fellowship_cast.jpg"),
                List.of(actor8)
        );

        movieRepository.persistAndFlush(movie1);
        movieRepository.persistAndFlush(movie2);
        movieRepository.persistAndFlush(movie3);
        movieRepository.persistAndFlush(movie4);
        movieRepository.persistAndFlush(movie5);
        movieRepository.persistAndFlush(movie6);
        movieRepository.persistAndFlush(movie7);
        movieRepository.persistAndFlush(movie8);
    }

    @Test
    public void testGetMovieById() {
        given()
                .pathParam("imdbID", "tt0111161")
                .when()
                .get("/movies/{imdbID}")
                .then()
                .statusCode(200)
                .body("imdbID", equalTo("tt0111161"))
                .body("title", equalTo("The Shawshank Redemption"))
                .body("releaseYear", equalTo(1994))
                .body("description", equalTo("Two imprisoned men bond over a number of years..."))
                .body("pictures", hasItems(
                        "http://example.com/shawshankredemption.jpg",
                        "http://example.com/shawshank_cast.jpg"))
                .body("cast", hasSize(2))
                .body("cast.name", hasItems("Tim Robbins", "Morgan Freeman"));
    }

    @Test
    public void testCreateMovie() {
        String movieJson = "{ " +
                "\"imdbID\": \"tt1234567\", " +
                "\"title\": \"Test Movie\", " +
                "\"releaseYear\": 2024, " +
                "\"description\": \"A test movie description.\", " +
                "\"pictures\": [\"http://example.com/testmovie.jpg\"], " +
                "\"cast\": [" + actorId1 + "] " +
                "}";

        given()
                .contentType("application/json")
                .body(movieJson)
                .when()
                .post("/movies")
                .then()
                .statusCode(201)
                .body("imdbID", equalTo("tt1234567"))
                .body("title", equalTo("Test Movie"))
                .body("releaseYear", equalTo(2024))
                .body("description", equalTo("A test movie description."))
                .body("pictures", hasSize(1))
                .body("pictures[0]", equalTo("http://example.com/testmovie.jpg"))
                .body("cast", hasSize(1))
                .body("cast.name", hasItem("Tim Robbins"));
    }

    @Test
    public void testUpdateMovie() {
        String updatedMovieJson = "{ " +
                "\"title\": \"The Shawshank Redemption - Updated\", " +
                "\"releaseYear\": 1995, " +
                "\"description\": \"Updated description.\", " +
                "\"pictures\": [\"http://example.com/updatedpicture.jpg\"], " +
                "\"cast\": [" + actorId1 + ", " + actorId2 + "] " +
                "}";

        given()
                .contentType("application/json")
                .body(updatedMovieJson)
                .pathParam("imdbID", "tt0111161")
                .when()
                .put("/movies/{imdbID}")
                .then()
                .statusCode(200)
                .body("imdbID", equalTo("tt0111161"))
                .body("title", equalTo("The Shawshank Redemption - Updated"))
                .body("releaseYear", equalTo(1995))
                .body("description", equalTo("Updated description."))
                .body("pictures", hasSize(1))
                .body("pictures[0]", equalTo("http://example.com/updatedpicture.jpg"))
                .body("cast", hasSize(2))
                .body("cast.name", hasItems("Tim Robbins", "Morgan Freeman"));
    }

    @Test
    public void testDeleteMovie() {
        given()
                .pathParam("imdbID", "tt0111161")
                .when()
                .delete("/movies/{imdbID}")
                .then()
                .statusCode(204);

        // Verify the movie is deleted
        given()
                .pathParam("imdbID", "tt0111161")
                .when()
                .get("/movies/{imdbID}")
                .then()
                .statusCode(404)
                .body("message", equalTo("Movie not found."));
    }

    @Test
    public void testSearchMoviesByTitle() {
        given()
                .queryParam("title", "Godfather")
                .when()
                .get("/movies/search")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].imdbID", equalTo("tt0068646"))
                .body("[0].title", equalTo("The Godfather"));
    }

    @Test
    public void testGetAllMoviesWithoutPagination() {
        given()
                .when()
                .get("/movies")
                .then()
                .statusCode(200)
                .body("$", hasSize(8))
                .body("imdbID", hasItems(
                        "tt0111161", "tt0068646", "tt1375666",
                        "tt0108052", "tt0167260", "tt0080684",
                        "tt0137523", "tt0120737"));
    }

    @Test
    public void testGetAllMoviesPaginationFirstPage() {
        given()
                .when()
                .get("/movies?page=1&size=5")
                .then()
                .statusCode(200)
                .body("items", hasSize(5))
                .body("items.imdbID", hasItems(
                        "tt0111161", "tt0068646", "tt1375666",
                        "tt0108052", "tt0167260"))
                .body("currentPage", equalTo(1))
                .body("pageSize", equalTo(5))
                .body("totalItems", equalTo(8))
                .body("totalPages", equalTo(2));
    }

    @Test
    public void testGetAllMoviesPaginationSecondPage() {
        given()
                .when()
                .get("/movies?page=2&size=5")
                .then()
                .statusCode(200)
                .body("items", hasSize(3))
                .body("items.imdbID", hasItems(
                        "tt0080684", "tt0137523", "tt0120737"))
                .body("currentPage", equalTo(2))
                .body("pageSize", equalTo(5))
                .body("totalItems", equalTo(8))
                .body("totalPages", equalTo(2));
    }

    @Test
    public void testGetAllMoviesPaginationExceedingMaxSize() {
        given()
                .when()
                .get("/movies?page=1&size=200")
                .then()
                .statusCode(200)
                .body("items", hasSize(8))
                .body("currentPage", equalTo(1))
                .body("pageSize", equalTo(100))
                .body("totalItems", equalTo(8))
                .body("totalPages", equalTo(1));
    }

    @Test
    public void testGetAllMoviesPaginationInvalidParameters() {
        given()
                .when()
                .get("/movies?page=0&size=0")
                .then()
                .statusCode(400)
                .body("message", equalTo("Page and size parameters must be positive integers."));
    }

    @Test
    public void testSearchMoviesByYear() {
        given()
                .queryParam("year", 1994)
                .when()
                .get("/movies/search")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].imdbID", equalTo("tt0111161"))
                .body("[0].title", equalTo("The Shawshank Redemption"));
    }

    @Test
    public void testSearchMoviesByTitleAndYear() {
        given()
                .queryParam("title", "The Lord of the Rings")
                .queryParam("year", 2003)
                .when()
                .get("/movies/search")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].imdbID", equalTo("tt0167260"))
                .body("[0].title", equalTo("The Lord of the Rings: The Return of the King"));
    }

    @Test
    public void testSearchMoviesWithoutParameters() {
        given()
                .when()
                .get("/movies/search")
                .then()
                .statusCode(200)
                .body("$", hasSize(8))
                .body("imdbID", hasItems(
                        "tt0111161", "tt0068646", "tt1375666",
                        "tt0108052", "tt0167260", "tt0080684",
                        "tt0137523", "tt0120737"));
    }

    @Test
    public void testCreateMovieWithMissingFields() {
        String movieJson = "{ " +
                "\"imdbID\": \"\", " +
                "\"title\": \"\", " +
                "\"releaseYear\": 2024, " +
                "\"description\": \"\", " +
                "\"pictures\": [\"http://example.com/testmovie.jpg\"], " +
                "\"cast\": [" + actorId1 + "] " +
                "}";

        given()
                .contentType("application/json")
                .body(movieJson)
                .when()
                .post("/movies")
                .then()
                .statusCode(400)
                .body("errors", hasItems(
                        "IMDB ID cannot be blank",
                        "Title cannot be blank",
                        "Description cannot be blank"
                ));
    }

    @Test
    public void testGetNonExistentMovie() {
        given()
                .pathParam("imdbID", "nonexistent")
                .when()
                .get("/movies/{imdbID}")
                .then()
                .statusCode(404)
                .body("message", equalTo("Movie not found."));
    }

    @Test
    public void testCreateMovieWithNonExistentActors() {
        String movieJson = "{ " +
                "\"imdbID\": \"tt9999999\", " +
                "\"title\": \"Movie with Non-Existent Actors\", " +
                "\"releaseYear\": 2024, " +
                "\"description\": \"A movie that references actors that do not exist.\", " +
                "\"pictures\": [\"http://example.com/testmovie.jpg\"], " +
                "\"cast\": [9999, 8888] " +
                "}";

        given()
                .contentType("application/json")
                .body(movieJson)
                .when()
                .post("/movies")
                .then()
                .statusCode(400)
                .body("message", equalTo("One or more actors not found."));
    }

    @Test
    public void testUpdateMovieWithNonExistentActors() {
        String movieJson = "{ " +
                "\"title\": \"Updated Movie\", " +
                "\"releaseYear\": 1995, " +
                "\"description\": \"Updated description.\", " +
                "\"pictures\": [\"http://example.com/updatedpicture.jpg\"], " +
                "\"cast\": [9999, 8888] " +
                "}";

        given()
                .contentType("application/json")
                .body(movieJson)
                .pathParam("imdbID", "tt0111161")
                .when()
                .put("/movies/{imdbID}")
                .then()
                .statusCode(400)
                .body("message", equalTo("One or more actors not found."));
    }

    @Test
    public void testGetAllMoviesPaginationInvalidParameters2() {
        given()
                .when()
                .get("/movies?page=-1&size=-5")
                .then()
                .statusCode(400)
                .body("message", equalTo("Page and size parameters must be positive integers."));

        given()
                .when()
                .get("/movies?page=0&size=0")
                .then()
                .statusCode(400)
                .body("message", equalTo("Page and size parameters must be positive integers."));
    }

    @Test
    public void testCreateDuplicateMovie() {
        // Attempt to create a movie with an existing imdbID
        String movieJson = "{ " +
                "\"imdbID\": \"tt0111161\", " +
                "\"title\": \"Duplicate Movie\", " +
                "\"releaseYear\": 1994, " +
                "\"description\": \"This is a duplicate movie.\", " +
                "\"pictures\": [\"http://example.com/duplicate.jpg\"], " +
                "\"cast\": [" + actorId1 + "] " +
                "}";

        given()
                .contentType("application/json")
                .body(movieJson)
                .when()
                .post("/movies")
                .then()
                .statusCode(409)
                .body("message", equalTo("Movie with this IMDb ID already exists."));
    }

    @Test
    public void testUpdateNonExistentMovie() {
        String movieJson = "{ " +
                "\"title\": \"Non-existent Movie\", " +
                "\"releaseYear\": 2020, " +
                "\"description\": \"Trying to update a movie that doesn't exist.\", " +
                "\"pictures\": [], " +
                "\"cast\": [] " +
                "}";

        given()
                .contentType("application/json")
                .body(movieJson)
                .pathParam("imdbID", "tt0000000")
                .when()
                .put("/movies/{imdbID}")
                .then()
                .statusCode(404)
                .body("message", equalTo("Movie not found."));
    }
}
