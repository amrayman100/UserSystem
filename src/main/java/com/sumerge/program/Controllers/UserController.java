package com.sumerge.program.Controllers;
import Entities.AuditLog;
import com.sumerge.program.DAL.AuditLogRepo;
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
import org.apache.log4j.Logger;
import static java.util.logging.Level.SEVERE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import com.sumerge.program.DAL.UserRepo;
import com.sumerge.program.UserResources;

import javax.ws.rs.core.SecurityContext;

@RequestScoped
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Path("user")
public class UserController {
    static private com.sumerge.program.DAL.UserRepo repo2 = new com.sumerge.program.DAL.UserRepo();
    static private AuditLogRepo AuditLogger = new AuditLogRepo();
    private static final Logger LOGGER = Logger.getLogger(UserResources.class.getName());
    @Context
    private SecurityContext securityContext;
    private String loggeduser = securityContext.getUserPrincipal().toString();

    @GET
    public Response getAll() {
        LOGGER.debug("Entering create user REST method.");
        System.out.println("Name: "+securityContext.getUserPrincipal().toString());
        System.out.println("Auth Scheme : "+securityContext.isUserInRole("admin"));
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

        try {
            return Response.ok().
                    entity( repo2.findByUsername(username,securityContext.isUserInRole("admin"))).
                    build();

        } catch (Exception e) {
           // LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getClass() + ": " + e.getMessage()).
                    build();
        }
    }

    @POST
    public Response AddUser(Entities.User  u) {
        try {
            if (u.getId() == -1)
                throw new IllegalArgumentException("Can't edit user since it does not exist in the database");

            repo2.addUser(u,loggeduser);
            return Response.ok().
                    build();
        } catch (Exception e) {
            //LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getClass() + ": " + e.getMessage()).
                    build();
        }
    }

    @DELETE
    @Path("{username}")
    public Response deleteUser2(@PathParam("username") String username) {
        try {
            repo2.deleteUser(username);
            return Response.ok().
                    build();
        } catch (Exception e) {
           // LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getClass() + ": " + e.getMessage()).
                    build();
        }
    }

    @PUT
    @Path("{username}")
    public Response undoDelete(@PathParam("username") String username) {
        try {
            repo2.undoDeleteUser(username);
            return Response.ok().
                    build();
        } catch (Exception e) {
           // LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getClass() + ": " + e.getMessage()).
                    build();
        }
    }

    @PUT
    @Path("/username/{username}/")
    public Response updateName(@PathParam("username") String username ,String newusername ) {

        if(!securityContext.isUserInRole("admin") && !username.equalsIgnoreCase(securityContext.getUserPrincipal().toString())){
            return Response.status(Response.Status.fromStatusCode(401)).entity("You do not have permissions to do this action.").build();
        }

        try {

            repo2.updateUserName(username,newusername,loggeduser);
            return Response.ok().
                    build();
        } catch (Exception e) {
            //LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getClass() + ": " + e.getMessage()).
                    build();
        }
    }

    @PUT
    @Path("/email/{username}")
    public Response updateEmail(@PathParam("username") String username , String newemail ) {

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
                    entity(e.getClass() + ": " + e.getMessage()).
                    build();
        }
    }

    @PUT
    @Path("/password/{username}/")
    public Response updatePassword(@PathParam("username") String username , String password ) {

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
