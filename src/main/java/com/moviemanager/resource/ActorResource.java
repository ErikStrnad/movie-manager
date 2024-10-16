package com.moviemanager.resource;

import com.moviemanager.dto.ActorDTO;
import com.moviemanager.entity.Actor;
import com.moviemanager.repository.ActorRepository;
import com.moviemanager.exception.ErrorResponse;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.util.List;

/**
 * RESTful resource for managing Actor entities.
 */
@Path("/actors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ActorResource {

    @Inject
    ActorRepository actorRepository;

    @GET
    @Operation(summary = "Retrieve all actors")
    @APIResponse(responseCode = "200", description = "Successfully retrieved actors")
    public Response getAllActors() {
        List<Actor> actors = actorRepository.listAll();
        return Response.ok(actors).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Retrieve an actor by ID")
    @APIResponse(responseCode = "200", description = "Actor found")
    @APIResponse(responseCode = "404", description = "Actor not found")
    public Response getActorById(@PathParam("id") Long id) {
        Actor actor = actorRepository.findById(id);
        if (actor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Actor not found."))
                    .build();
        }
        return Response.ok(actor).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Create a new actor")
    @APIResponse(responseCode = "201", description = "Actor successfully created")
    @APIResponse(responseCode = "400", description = "Invalid input")
    public Response createActor(@Valid ActorDTO actorDTO) {
        Actor actor = new Actor();
        actor.setName(actorDTO.getName());
        actor.setBirthdate(actorDTO.getBirthdate());
        actorRepository.persist(actor);
        return Response.status(Response.Status.CREATED)
                .entity(actor)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Update an existing actor")
    @APIResponse(responseCode = "200", description = "Actor successfully updated")
    @APIResponse(responseCode = "404", description = "Actor not found")
    public Response updateActor(@PathParam("id") Long id, @Valid ActorDTO actorDTO) {
        Actor actor = actorRepository.findById(id);
        if (actor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Actor not found."))
                    .build();
        }
        actor.setName(actorDTO.getName());
        actor.setBirthdate(actorDTO.getBirthdate());
        actorRepository.persist(actor);
        return Response.ok(actor).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Delete an actor by ID")
    @APIResponse(responseCode = "204", description = "Actor successfully deleted")
    @APIResponse(responseCode = "404", description = "Actor not found")
    @APIResponse(responseCode = "409", description = "Cannot delete actor associated with movies")
    public Response deleteActor(@PathParam("id") Long id) {
        Actor actor = actorRepository.findById(id);
        if (actor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Actor not found."))
                    .build();
        }
        if (actor.getMovies() != null && !actor.getMovies().isEmpty()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorResponse("Cannot delete actor associated with movies."))
                    .build();
        }
        actorRepository.delete(actor);
        return Response.noContent().build();
    }
}
