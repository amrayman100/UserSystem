package com.sumerge.program.controllers;
import com.sumerge.program.dal.AuditLogRepo;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.sumerge.program.exceptions.NotFoundException;
import org.apache.log4j.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.core.SecurityContext;

@RequestScoped
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Path("user")
public class UserController {
    static private com.sumerge.program.dal.UserRepo repo2 = new com.sumerge.program.dal.UserRepo();
    static private AuditLogRepo AuditLogger = new AuditLogRepo();
    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());
    @Context
    private SecurityContext securityContext;
    private String loggeduser;

    @GET
    public Response getAll() {
        loggeduser = securityContext.getUserPrincipal().toString();
        LOGGER.debug("Entering create user REST method.");
        System.out.println("Name: " + securityContext.getUserPrincipal().toString());
        System.out.println("Auth Scheme : " + securityContext.isUserInRole("admin"));
        try {
            return Response.ok().
                    entity(repo2.getAll(securityContext.isUserInRole("admin"))).
                    build();

        } catch (Exception e) {
            //LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getClass() + ": " + e.getMessage()).
                    build();
        }
    }


    @GET
    @Path("{username}")
    public Response getUser(@PathParam("username") String username) {
        loggeduser = securityContext.getUserPrincipal().toString();
        try {
            Object u = repo2.findByUsername(username, securityContext.isUserInRole("admin"));
            if (u == null) {
                throw new NotFoundException("User with the specified username can not be found");
            }
            return Response.ok().
                    entity(u).
                    build();

        } catch (Exception e) {
            // LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getMessage()).
                    build();
        }
    }

    @POST
    public Response AddUser(entities.User u) {
        loggeduser = securityContext.getUserPrincipal().toString();
        try {
            if (u.getId() == -1)
                throw new IllegalArgumentException("Can't edit user since it does not exist in the database");

            repo2.addUser(u, loggeduser);
            return Response.ok().
                    build();

        }

        catch (Exception e) {
            System.out.println("EX   " +e.getClass());
            return Response.serverError().
                    entity(e.getMessage()).
                    build();
        }
    }

    @DELETE
    @Path("{username}")
    public Response deleteUser2(@PathParam("username") String username) {
        loggeduser = securityContext.getUserPrincipal().toString();
        try {
            repo2.deleteUser(username);
            return Response.ok().
                    build();
        } catch (Exception e) {
           // LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getMessage()).
                    build();
        }
    }

    @PUT
    @Path("{username}")
    public Response undoDelete(@PathParam("username") String username) {
        loggeduser = securityContext.getUserPrincipal().toString();
        try {
            repo2.undoDeleteUser(username);
            return Response.ok().
                    build();
        } catch (Exception e) {
           // LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getMessage()).
                    build();
        }
    }

    @PUT
    @Path("/username/{username}/")
    public Response updateName(@PathParam("username") String username ,String newusername ) {
        loggeduser = securityContext.getUserPrincipal().toString();
        if(!securityContext.isUserInRole("admin") && !username.equalsIgnoreCase(securityContext.getUserPrincipal().toString())){
            return Response.status(Response.Status.fromStatusCode(401)).build();
        }

        try {

            repo2.updateUserName(username,newusername,loggeduser);
            return Response.ok().
                    build();
        } catch (Exception e) {
            //LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getMessage()).
                    build();
        }
    }

    @PUT
    @Path("/email/{username}")
    public Response updateEmail(@PathParam("username") String username , String newemail ) {
        loggeduser = securityContext.getUserPrincipal().toString();

        if(!securityContext.isUserInRole("admin") && !username.equalsIgnoreCase(securityContext.getUserPrincipal().toString())){
            return Response.status(Response.Status.fromStatusCode(401)).build();
        }

        try {
            repo2.updateUserEmail(username,newemail,loggeduser);
            return Response.ok().
                    build();
        } catch (Exception e) {
           // LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getMessage()).
                    build();
        }
    }

    @PUT
    @Path("/password/{username}/")
    public Response updatePassword(@PathParam("username") String username , String password ) {
        loggeduser = securityContext.getUserPrincipal().toString();

        if(!securityContext.isUserInRole("admin") && !username.equalsIgnoreCase(securityContext.getUserPrincipal().toString())){
            return Response.status(Response.Status.fromStatusCode(401)).build();
        }

        try {
            repo2.updateUserPassword(username,password,loggeduser );
            return Response.ok().
                    build();
        } catch (Exception e) {
          //  LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getClass() + ": " + e.getMessage()).
                    build();
        }
    }



    @POST
    @Path("/group/{username}/{groupid}")
    public Response addtogroup(@PathParam("username") String username ,@PathParam("groupid") int groupid  ) {
        loggeduser = securityContext.getUserPrincipal().toString();
        if(!securityContext.isUserInRole("admin")){
            return Response.status(Response.Status.fromStatusCode(401)).build();
        }
        try {
            repo2.addtoGroup(username,groupid,loggeduser );
            return Response.ok().
                    build();
        } catch (Exception e) {
         //   LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getClass() + ": " + e.getMessage()).
                    build();
        }
    }

    @DELETE
    @Path("/group/{username}/{groupid}")
    public Response removefromGroup(@PathParam("username") String username ,@PathParam("groupid") int groupid  ) {
        loggeduser = securityContext.getUserPrincipal().toString();
        if(!securityContext.isUserInRole("admin")){
            return Response.status(Response.Status.fromStatusCode(401)).build();
        }
        try {
            repo2.removefromGroup(username,groupid,loggeduser );
            return Response.ok().
                    build();
        } catch (Exception e) {
          //  LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getClass() + ": " + e.getMessage()).
                    build();
        }
    }

    @PUT
    @Path("/group/{username}/{currgroupid}/{targetgroupid}")
    public Response movetoGroup(@PathParam("username") String username , @PathParam("currgroupid") int currgroupid ,@PathParam("targetgroupid") int targetgroupid ) {
        loggeduser = securityContext.getUserPrincipal().toString();
        if(!securityContext.isUserInRole("admin")){
            return Response.status(Response.Status.fromStatusCode(401)).build();
        }
        try {
            repo2.removefromGroup(username,currgroupid,loggeduser);
            repo2.addtoGroup(username,targetgroupid,loggeduser );
            return Response.ok().
                    build();
        } catch (Exception e) {
            //  LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getClass() + ": " + e.getMessage()).
                    build();
        }
    }



}
