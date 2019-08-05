package com.sumerge.program.controllers;
import com.sumerge.program.dal.AuditLogRepo;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.sumerge.program.exceptions.NotFoundException;
import com.sumerge.program.exceptions.NotFoundMapper;
import com.sumerge.program.exceptions.WrongFormatException;
import com.sun.tools.corba.se.idl.constExpr.Not;
import org.apache.log4j.Logger;
import com.sumerge.program.viewmodels.UserModel;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.core.SecurityContext;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

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
    public Response getAll() throws NotFoundException , WrongFormatException {
        loggeduser = securityContext.getUserPrincipal().toString();
        LOGGER.debug("Entering create user REST method.");
        System.out.println("Name: " + securityContext.getUserPrincipal().toString());
        System.out.println("Auth Scheme : " + securityContext.isUserInRole("admin"));

            return Response.ok().
                    entity(repo2.getAll(securityContext.isUserInRole("admin"))).
                    build();


    }


    @GET
    @Path("{username}")
    public Response getUser(@PathParam("username") String username) throws NotFoundException , WrongFormatException {
        loggeduser = securityContext.getUserPrincipal().toString();

            Object u = repo2.findByUsername(username, securityContext.isUserInRole("admin"));
            if(u==null){
                throw new NotFoundException("User with the specified username can not be found");
            }

            return Response.ok().
                    entity(u).
                    build();


    }

    @POST
    public Response AddUser(UserModel u) throws NotFoundException, WrongFormatException, UnsupportedEncodingException, NoSuchAlgorithmException {
        loggeduser = securityContext.getUserPrincipal().toString();

            repo2.addUser(u, loggeduser);
            return Response.ok().
                    build();




    }

    @DELETE
    @Path("{username}")
    public Response deleteUser2(@PathParam("username") String username) throws NotFoundException , WrongFormatException{
        if(username.equalsIgnoreCase("admin")){
            return Response.status(Response.Status.fromStatusCode(401)).build();
        }
        loggeduser = securityContext.getUserPrincipal().toString();

            repo2.deleteUser(username);
            return Response.ok().
                    build();

    }

    @PUT
    @Path("{username}")
    public Response undoDelete(@PathParam("username") String username) throws NotFoundException , WrongFormatException{
        loggeduser = securityContext.getUserPrincipal().toString();

            repo2.undoDeleteUser(username);
            return Response.ok().
                    build();

    }

    @PUT
    @Path("/username/{username}/")
    public Response updateUserName(@PathParam("username") String username ,String newusername ) throws NotFoundException , WrongFormatException{
        loggeduser = securityContext.getUserPrincipal().toString();
        if(!securityContext.isUserInRole("admin") && !username.equalsIgnoreCase(securityContext.getUserPrincipal().toString())){
            return Response.status(Response.Status.fromStatusCode(401)).build();
        }

            repo2.updateUserName(username,newusername,loggeduser);
            return Response.ok().
                    build();

    }

    @PUT
    @Path("/email/{username}")
    public Response updateEmail(@PathParam("username") String username , String newemail ) throws NotFoundException , WrongFormatException {
        loggeduser = securityContext.getUserPrincipal().toString();

        if(!securityContext.isUserInRole("admin") && !username.equalsIgnoreCase(securityContext.getUserPrincipal().toString())){
            return Response.status(Response.Status.fromStatusCode(401)).build();
        }


            repo2.updateUserEmail(username,newemail,loggeduser);
            return Response.ok().
                    build();

    }

    @PUT
    @Path("/phone/{username}")
    public Response updatePhone(@PathParam("username") String username , String phone ) throws NotFoundException , WrongFormatException {
        loggeduser = securityContext.getUserPrincipal().toString();

        if(!securityContext.isUserInRole("admin") && !username.equalsIgnoreCase(securityContext.getUserPrincipal().toString())){
            return Response.status(Response.Status.fromStatusCode(401)).build();
        }


        repo2.updateUserPhone(username,phone,loggeduser);
        return Response.ok().
                build();

    }


    @PUT
    @Path("/password/{username}/")
    public Response updatePassword(@PathParam("username") String username , String password ) throws NotFoundException, WrongFormatException, UnsupportedEncodingException, NoSuchAlgorithmException {
        loggeduser = securityContext.getUserPrincipal().toString();

        if(!securityContext.isUserInRole("admin") && !username.equalsIgnoreCase(securityContext.getUserPrincipal().toString())){
            return Response.status(Response.Status.fromStatusCode(401)).build();
        }


            repo2.updateUserPassword(username,password,loggeduser );
            return Response.ok().
                    build();


    }



    @POST
    @Path("/group/{username}/{groupid}")
    public Response addtogroup(@PathParam("username") String username ,@PathParam("groupid") int groupid  ) throws NotFoundException , WrongFormatException {
        loggeduser = securityContext.getUserPrincipal().toString();
        if(!securityContext.isUserInRole("admin")){
            return Response.status(Response.Status.fromStatusCode(401)).build();
        }

            /*repo2.addtoGroup(username,groupid,loggeduser );
            return Response.ok().
                    build();*/

         //   LOGGER.log(SEVERE, e.getMessage(), e);

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
    public Response removefromGroup(@PathParam("username") String username ,@PathParam("groupid") int groupid  ) throws NotFoundException , WrongFormatException{
        loggeduser = securityContext.getUserPrincipal().toString();
        if(!securityContext.isUserInRole("admin")){
            return Response.status(Response.Status.fromStatusCode(401)).build();
        }

            repo2.removefromGroup(username,groupid,loggeduser );
            return Response.ok().
                    build();

          //  LOGGER.log(SEVERE, e.getMessage(), e);


    }

    @PUT
    @Path("/group/{username}/{currgroupid}/{targetgroupid}")
    public Response movetoGroup(@PathParam("username") String username , @PathParam("currgroupid") int currgroupid ,@PathParam("targetgroupid") int targetgroupid ) throws NotFoundException , WrongFormatException {
        loggeduser = securityContext.getUserPrincipal().toString();
        if(!securityContext.isUserInRole("admin")){
            return Response.status(Response.Status.fromStatusCode(401)).build();
        }

            repo2.removefromGroup(username,currgroupid,loggeduser);
            repo2.addtoGroup(username,targetgroupid,loggeduser );
            return Response.ok().
                    build();

    }



}
