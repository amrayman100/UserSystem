package com.sumerge.program.dal;
import javax.ws.rs.GET;
import javax.ws.rs.core.Response;

public interface CRUD {
        @GET
        Response getAll();
}
