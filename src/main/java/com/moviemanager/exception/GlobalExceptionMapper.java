package com.moviemanager.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<WebApplicationException> {
    @Override
    public Response toResponse(WebApplicationException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
        return Response.status(exception.getResponse().getStatus())
                .entity(errorResponse)
                .build();
    }
}

