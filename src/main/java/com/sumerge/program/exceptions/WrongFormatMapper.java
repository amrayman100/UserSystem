package com.sumerge.program.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WrongFormatMapper implements ExceptionMapper<WrongFormatException> {

    @Override
    public Response toResponse(WrongFormatException exception)
    {
        return Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).build();
    }
}
