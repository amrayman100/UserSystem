package com.sumerge.program.controllers;

import com.sumerge.program.exceptions.NotFoundException;
import com.sumerge.program.exceptions.WrongFormatException;
import com.sumerge.program.viewmodels.GroupModel;

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
            repo2.entityManager.getTransaction().rollback();
            return Response.serverError().
                    entity(e.getMessage()).
                    build();
        }
    }

    @GET
    @Path("{username}")
    public Response getGroup(@PathParam("username") String group) throws NotFoundException {


            Object u = repo2.findByName(group,securityContext.isUserInRole("admin"));
            if(u==null) {
                throw new NotFoundException("Group with the specified name can not be found");
            }

            return Response.ok().
                    entity(u).
                    build();


    }

    @POST
    public Response AddGroup(GroupModel u) throws WrongFormatException {

            repo2.addGroup(u,securityContext.getUserPrincipal().toString());
            return Response.ok().
                    build();

    }

    @DELETE
    @Path("{id}")
    public Response DeleteGroup(@PathParam("id") int id) throws WrongFormatException {

        if(id == 1){
            throw new WrongFormatException("Can not delete default group");
        }
            repo2.deleteGroup(id,securityContext.getUserPrincipal().toString());
            return Response.ok().
                    build();

    }



}
