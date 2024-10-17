# Movie Manager

Welcome to the **Movie Manager** project! This application is a RESTful API for managing movies and actors, built with
Quarkus, the Supersonic Subatomic Java Framework.

## Table of Contents

- [Overview](#overview)
- [Running the Application](#running-the-application)
    - [Dev Mode](#dev-mode)
    - [Packaging and Running the Application](#packaging-and-running-the-application)
    - [Creating a Native Executable](#creating-a-native-executable)
- [API Documentation](#api-documentation)
    - [Actors API](#actors-api)
        - [Retrieve All Actors](#1-retrieve-all-actors)
        - [Retrieve an Actor by ID](#2-retrieve-an-actor-by-id)
        - [Create a New Actor](#3-create-a-new-actor)
        - [Update an Existing Actor](#4-update-an-existing-actor)
        - [Delete an Actor](#5-delete-an-actor)
    - [Movies API](#movies-api)
        - [Retrieve Movies](#1-retrieve-movies)
        - [Retrieve a Movie by IMDb ID](#2-retrieve-a-movie-by-imdb-id)
        - [Create a New Movie](#3-create-a-new-movie)
        - [Update an Existing Movie](#4-update-an-existing-movie)
        - [Delete a Movie](#5-delete-a-movie)
        - [Search Movies](#6-search-movies)
    - [Error Handling](#error-handling)
- [Database Initialization](#database-initialization)

## Overview

The **Movie Manager** application provides a RESTful API for managing movies and actors. It allows clients to perform
CRUD operations on movies and actors, supports pagination and search functionality for movies, and ensures data
persistence using an H2 in-memory database.

## Running the Application

### Dev Mode

You can run the application in development mode, which enables live coding and hot reload:

```bash
./mvnw compile quarkus:dev
```

The application will be available at `http://localhost:8080/`.

**Note:** Quarkus provides a Dev UI in dev mode at `http://localhost:8080/q/dev/`.

### Packaging and Running the Application

To package the application:

```bash
./mvnw package
```

This command produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory. You can run the application
using:

```bash
java -jar target/quarkus-app/quarkus-run.jar
```

If you prefer to build an über-jar (fat JAR), use:

```bash
./mvnw package -Dquarkus.package.type=uber-jar
```

Run the über-jar using:

```bash
java -jar target/*-runner.jar
```

### Creating a Native Executable

To create a native executable:

```bash
./mvnw package -Pnative
```

If you don't have GraalVM installed, you can build the native executable in a container:

```bash
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

Run the native executable:

```bash
./target/movie-manager-1.0.0-SNAPSHOT-runner
```

**Note:** Building a native executable can take some time.

## API Documentation

### Actors API

#### 1. Retrieve All Actors

**Endpoint:**

```
GET /actors
```

**Description:**

Retrieve a list of all actors.

**Responses:**

- `200 OK`: Successfully retrieved the list of actors.

**Example:**

```bash
curl -X GET "http://localhost:8080/actors" -H "Accept: application/json"
```

#### 2. Retrieve an Actor by ID

**Endpoint:**

```
GET /actors/{id}
```

**Path Parameters:**

- `id` (integer): The ID of the actor.

**Responses:**

- `200 OK`: Actor found.
- `404 Not Found`: Actor not found.

**Example:**

```bash
curl -X GET "http://localhost:8080/actors/9" -H "Accept: application/json"
```

#### 3. Create a New Actor

**Endpoint:**

```
POST /actors
```

**Request Body:**

Provide a JSON object with the following fields:

- `name` (string, required): The name of the actor.
- `birthdate` (string, optional): The birthdate of the actor in `YYYY-MM-DD` format.

**Responses:**

- `201 Created`: Actor successfully created.
- `400 Bad Request`: Validation failed.

**Example:**

```bash
curl -X POST "http://localhost:8080/actors"   -H "Content-Type: application/json"   -d '{
        "name": "New Actor",
        "birthdate": "1990-01-01"
      }'
```

#### 4. Update an Existing Actor

**Endpoint:**

```
PUT /actors/{id}
```

**Path Parameters:**

- `id` (integer): The ID of the actor to update.

**Request Body:**

Provide a JSON object with the updated actor details:

- `name` (string, required): The new name of the actor.
- `birthdate` (string, optional): The updated birthdate.

**Responses:**

- `200 OK`: Actor successfully updated.
- `400 Bad Request`: Validation failed.
- `404 Not Found`: Actor not found.

**Example:**

```bash
curl -X PUT "http://localhost:8080/actors/1"   -H "Content-Type: application/json"   -d '{
        "name": "Updated Actor Name",
        "birthdate": "1985-05-15"
      }'
```

#### 5. Delete an Actor

**Endpoint:**

```
DELETE /actors/{id}
```

**Path Parameters:**

- `id` (integer): The ID of the actor to delete.

**Responses:**

- `204 No Content`: Actor successfully deleted.
- `404 Not Found`: Actor not found.

**Example:**

```bash
curl -X DELETE "http://localhost:8080/actors/1"
```

### Movies API

#### 1. Retrieve Movies

**Endpoint:**

```
GET /movies
```

**Description:**

Retrieve all movies or a paginated list of movies.

**Query Parameters:**

- `page` (integer, optional): Page number to retrieve (starting from 1).
- `size` (integer, optional): Number of movies per page (maximum 100).

**Responses:**

- `200 OK`: Successfully retrieved movies.
- `400 Bad Request`: Invalid pagination parameters.

**Examples:**

- Retrieve all movies:

  ```bash
  curl -X GET "http://localhost:8080/movies" -H "Accept: application/json"
  ```

- Retrieve movies with pagination:

  ```bash
  curl -X GET "http://localhost:8080/movies?page=1&size=5" -H "Accept: application/json"
  ```

#### 2. Retrieve a Movie by IMDb ID

**Endpoint:**

```
GET /movies/{imdbID}
```

**Path Parameters:**

- `imdbID` (string): The IMDb ID of the movie.

**Responses:**

- `200 OK`: Movie found.
- `404 Not Found`: Movie not found.

**Example:**

```bash
curl -X GET "http://localhost:8080/movies/tt0111161" -H "Accept: application/json"
```

#### 3. Create a New Movie

**Endpoint:**

```
POST /movies
```

**Request Body:**

Provide a JSON object with the following fields:

- `imdbID` (string, required): The IMDb ID of the movie.
- `title` (string, required): The title of the movie.
- `releaseYear` (integer, optional): The release year of the movie.
- `description` (string, optional): A description of the movie.
- `pictures` (array of strings, optional): URLs to pictures related to the movie.
- `cast` (array of integers, optional): IDs of actors in the movie.

**Responses:**

- `201 Created`: Movie successfully created.
- `400 Bad Request`: Validation failed or actors not found.
- `409 Conflict`: Movie with this IMDb ID already exists.

**Example:**

```bash
curl -X POST "http://localhost:8080/movies"   -H "Content-Type: application/json"   -d '{
        "imdbID": "tt1234567",
        "title": "New Movie Title",
        "releaseYear": 2024,
        "description": "A description of the new movie.",
        "pictures": ["http://example.com/image1.jpg", "http://example.com/image2.jpg"],
        "cast": [1, 2, 3]
      }'
```

#### 4. Update an Existing Movie

**Endpoint:**

```
PUT /movies/{imdbID}
```

**Path Parameters:**

- `imdbID` (string): The IMDb ID of the movie to update.

**Request Body:**

Provide a JSON object with the updated movie details:

- `title` (string, required): The new title of the movie.
- `releaseYear` (integer, optional): The new release year.
- `description` (string, optional): The updated description.
- `pictures` (array of strings, optional): Updated list of picture URLs.
- `cast` (array of integers, optional): Updated list of actor IDs.

**Responses:**

- `200 OK`: Movie successfully updated.
- `400 Bad Request`: Validation failed or actors not found.
- `404 Not Found`: Movie not found.

**Example:**

```bash
curl -X PUT "http://localhost:8080/movies/tt1234567"   -H "Content-Type: application/json"   -d '{
        "title": "Updated Movie Title",
        "releaseYear": 2025,
        "description": "Updated description of the movie.",
        "pictures": ["http://example.com/newimage1.jpg"],
        "cast": [2, 3, 4]
      }'
```

#### 5. Delete a Movie

**Endpoint:**

```
DELETE /movies/{imdbID}
```

**Path Parameters:**

- `imdbID` (string): The IMDb ID of the movie to delete.

**Responses:**

- `204 No Content`: Movie successfully deleted.
- `404 Not Found`: Movie not found.

**Example:**

```bash
curl -X DELETE "http://localhost:8080/movies/tt1234567"
```

#### 6. Search Movies

**Endpoint:**

```
GET /movies/search
```

**Query Parameters:**

- `title` (string, optional): Title to search for (partial matches allowed).
- `year` (integer, optional): Release year to search for.

**Responses:**

- `200 OK`: Successfully retrieved search results.

**Examples:**

- Search by title:

  ```bash
  curl -X GET "http://localhost:8080/movies/search?title=Inception" -H "Accept: application/json"
  ```

- Search by release year:

  ```bash
  curl -X GET "http://localhost:8080/movies/search?year=1994" -H "Accept: application/json"
  ```

- Search by title and release year:

  ```bash
  curl -X GET "http://localhost:8080/movies/search?title=Inception&year=2010" -H "Accept: application/json"
  ```

- Retrieve all movies without filters:

  ```bash
  curl -X GET "http://localhost:8080/movies/search" -H "Accept: application/json"
  ```

## Error Handling

The API returns error responses with a JSON body containing a `message` field describing the error. For validation
errors, additional details are provided in the `errors` field.

### Example Error Response

```json
{
  "message": "Validation failed",
  "errors": [
    "IMDB ID cannot be blank",
    "Title cannot be blank"
  ]
}
```

## Database Initialization

The database is automatically populated with sample data upon application startup using the
`/src/main/resources/import.sql` file.

You can modify the database generation behavior and logging settings in the `/src/main/resources/application.properties`
file.