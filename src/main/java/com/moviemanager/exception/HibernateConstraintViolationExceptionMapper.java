package com.moviemanager.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.hibernate.exception.ConstraintViolationException;

@Provider
public class HibernateConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        String message = "Database constraint violation: " + exception.getCause().getMessage();
        return Response.status(Response.Status.CONFLICT)
                .entity(new ErrorResponse(message))
                .build();
    }
}
