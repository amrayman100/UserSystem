package com.sumerge.program.Controllers;
import com.sumerge.program.DAL.JPAUtil;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;
import static java.util.logging.Level.SEVERE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import com.sumerge.program.DAL.UserRepo;
import com.sumerge.program.UserResources;

import javax.ws.rs.core.SecurityContext;

@RequestScoped
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Path("group")
public class GroupController {
    static private com.sumerge.program.DAL.GroupRepo repo2 = new com.sumerge.program.DAL.GroupRepo();
    private static final Logger LOGGER = Logger.getLogger(UserResources.class.getName());
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
    public Response AddGroup(Entities.Group  u) {
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
    public Response DeleteGroup(Entities.Group  u) {
        try {
            if (u.getId() == -1)
                throw new IllegalArgumentException("Can't edit user since it does not exist in the database");

            repo2.deleteGroup(u.getId());
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
