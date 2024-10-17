package com.moviemanager.resource;

import com.moviemanager.entity.Actor;
import com.moviemanager.repository.ActorRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for the ActorResource REST endpoints.
 */
@QuarkusTest
public class ActorResourceTest {

    @Inject
    ActorRepository actorRepository;

    @BeforeEach
    @Transactional
    public void setup() {
        actorRepository.deleteAll();
    }

    @Test
    public void testGetAllActors() {
        Long actorId = createTestActor();

        given()
                .when()
                .get("/actors")
                .then()
                .statusCode(200)
                .body("$", not(empty()))
                .body("id", hasItem(actorId.intValue()));
    }

    @Test
    public void testCreateActor() {
        String actorJson = "{ " +
                "\"name\": \"Test Actor\", " +
                "\"birthdate\": \"1980-01-01\" " +
                "}";

        given()
                .contentType("application/json")
                .body(actorJson)
                .when()
                .post("/actors")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("Test Actor"))
                .body("birthdate", equalTo("1980-01-01"));
    }

    @Test
    public void testGetActorById() {
        Long actorId = createTestActor();

        given()
                .pathParam("id", actorId)
                .when()
                .get("/actors/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(actorId.intValue()))
                .body("name", equalTo("Test Actor"))
                .body("birthdate", equalTo("1980-01-01"));
    }

    @Test
    public void testUpdateActor() {
        Long actorId = createTestActor();

        String updatedActorJson = "{ " +
                "\"name\": \"Updated Name\", " +
                "\"birthdate\": \"1990-01-01\" " +
                "}";

        given()
                .contentType("application/json")
                .body(updatedActorJson)
                .pathParam("id", actorId)
                .when()
                .put("/actors/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(actorId.intValue()))
                .body("name", equalTo("Updated Name"))
                .body("birthdate", equalTo("1990-01-01"));
    }

    @Test
    public void testDeleteActor() {
        Long actorId = createTestActor();

        given()
                .pathParam("id", actorId)
                .when()
                .delete("/actors/{id}")
                .then()
                .statusCode(204);

        // Verify actor is deleted
        given()
                .pathParam("id", actorId)
                .when()
                .get("/actors/{id}")
                .then()
                .statusCode(404)
                .body("message", equalTo("Actor not found."));
    }

    @Test
    public void testCreateActorValidationFailure() {
        // Missing name
        String actorJson = "{ " +
                "\"birthdate\": \"1980-01-01\" " +
                "}";

        given()
                .contentType("application/json")
                .body(actorJson)
                .when()
                .post("/actors")
                .then()
                .statusCode(400)
                .body("errors[0]", containsString("Name cannot be blank"));
    }

    @Transactional
    public Long createTestActor() {
        Actor actor = new Actor();
        actor.setName("Test Actor");
        actor.setBirthdate(LocalDate.of(1980, 1, 1));
        actorRepository.persistAndFlush(actor);
        return actor.getId();
    }
}
