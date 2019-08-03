package com.sumerge.program.controllers;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;
import static java.util.logging.Level.SEVERE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.core.SecurityContext;

@RequestScoped
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Path("group")
public class GroupController {
    static private com.sumerge.program.dal.GroupRepo repo2 = new com.sumerge.program.dal.GroupRepo();
    private static final Logger LOGGER = Logger.getLogger(GroupController.class.getName());
    @Context
    private SecurityContext securityContext;

    @GET
    public Response getAll() {
        System.out.println("Name: "+securityContext.getUserPrincipal().toString());
        System.out.println("Auth Scheme : "+securityContext.isUserInRole("admin"));
        try {
            return Response.ok().
                    entity(repo2.getAll()).
                    build();

        } catch (Exception e) {
            LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getClass() + ": " + e.getMessage()).
                    build();
        }
    }

    @GET
    @Path("{username}")
    public Response getGroup(@PathParam("username") String group) {

        try {
            return Response.ok().
                    entity( repo2.findByName(group,securityContext.isUserInRole("admin"))).
                    build();

        } catch (Exception e) {
            LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getClass() + ": " + e.getMessage()).
                    build();
        }
    }

    @POST
    public Response AddGroup(entities.Group  u) {
        try {
            if (u.getId() == -1)
                throw new IllegalArgumentException("Can't edit user since it does not exist in the database");

            repo2.addGroup(u);
            return Response.ok().
                    build();
        } catch (Exception e) {
            LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getClass() + ": " + e.getMessage()).
                    build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response DeleteGroup(@PathParam("id") int id) {
        try {
            if (id == -1)
                throw new IllegalArgumentException("Can't edit user since it does not exist in the database");

            repo2.deleteGroup(id);
            return Response.ok().
                    build();
        } catch (Exception e) {
            LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getClass() + ": " + e.getMessage()).
                    build();
        }
    }



}
