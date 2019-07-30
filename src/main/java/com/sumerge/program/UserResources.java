package com.sumerge.program;

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
import javax.ws.rs.core.SecurityContext;

@RequestScoped
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Path("users")

public class UserResources {

    @Context
    private SecurityContext securityContext;

    private static final Logger LOGGER = Logger.getLogger(UserResources.class.getName());

    @EJB
    static private com.sumerge.program.UserRepo repo = new com.sumerge.program.UserRepo();

   static private com.sumerge.program.DAL.UserRepo repo2 = new com.sumerge.program.DAL.UserRepo();

    @Context
    HttpServletRequest request;

    @GET
    public Response getAllUsers() {
        try {
            return Response.ok().
                    entity(repo.getUserList()).
                    build();
        } catch (Exception e) {
            LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getClass() + ": " + e.getMessage()).
                    build();
        }
    }



    @GET
    @Path("test")
    public Response test() {
        /*EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        TypedQuery<Entities.User> query =
                entityManager.createNamedQuery("User.findAll", Entities.User.class);
        List<Entities.User> results = query.getResultList();

        entityManager.getTransaction().commit();
        entityManager.close();*/

        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        Entities.Group em = entityManager.find(Entities.Group.class, 1);
        System.out.println("emp id :: " + em.getName());
        System.out.println("emp id :: " + em.getDesc());
        System.out.println("ALIVE");

        entityManager.getTransaction().commit();
        entityManager.close();
        try {
            return Response.ok().
                    entity(em).
                    build();
        } catch (Exception e) {
            LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getClass() + ": " + e.getMessage()).
                    build();
        }
    }


    @GET
    @Path("test2")
    public Response test2() {
        //securityContext.getAuthenticationScheme();
        System.out.println("Name: "+securityContext.getUserPrincipal().toString());
        System.out.println("Auth Scheme : "+securityContext.isUserInRole("admin"));


        try {
            return Response.ok().
                    entity(repo2.getAll(securityContext.isUserInRole("admin"))).
                    build();

        } catch (Exception e) {
            LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getClass() + ": " + e.getMessage()).
                    build();
        }
    }

    @POST
    @Path("test2")
    public Response AddUser(Entities.User  u) {
        try {
            if (u.getId() == -1)
                throw new IllegalArgumentException("Can't edit student since it does not exist in the database");

            repo2.addUser(u);
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
    @Path("test2/{username}")
    public Response deleteUser2(@PathParam("username") String username) {
        try {
            repo2.deleteUser(username);
            return Response.ok().
                    build();
        } catch (Exception e) {
            LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getClass() + ": " + e.getMessage()).
                    build();
        }
    }

    @PUT
    @Path("test2/{username}")
    public Response undoDelete(@PathParam("username") String username) {
        try {
            repo2.undoDeleteUser(username);
            return Response.ok().
                    build();
        } catch (Exception e) {
            LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getClass() + ": " + e.getMessage()).
                    build();
        }
    }


    @PUT
    @Path("test3")
    public Response addtogroup() {
        try {
            repo2.removefromGroup("amr_100",1);
            return Response.ok().
                    build();
        } catch (Exception e) {
            LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getClass() + ": " + e.getMessage()).
                    build();
        }
    }






    @GET
    @Path("name")
    public Response getUserbyName(@QueryParam("name") String name) {
        try {
            return Response.ok().
                    entity(repo.getUserbyName(name)).
                    build();
        } catch (Exception e) {
            LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getClass() + ": " + e.getMessage()).
                    build();
        }
    }

    @GET
    @Path("address")
    public Response getUserbyAddress(@QueryParam("address") String address) {
        try {
            return Response.ok().
                    entity(repo.getUserbyAddress(address)).
                    build();
        } catch (Exception e) {
            LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getClass() + ": " + e.getMessage()).
                    build();
        }
    }

    @GET
    @Path("email")
    public Response getUserbyEmail(@QueryParam("email") String email) {
        try {
            return Response.ok().
                    entity(repo.getUserbyemail(email)).
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
    public Response deleteUser(@PathParam("id") int id) {
        try {
            repo.deleteUser(id);
            return Response.ok().
                    build();
        } catch (Exception e) {
            LOGGER.log(SEVERE, e.getMessage(), e);
            return Response.serverError().
                    entity(e.getClass() + ": " + e.getMessage()).
                    build();
        }
    }

    @PUT
    public Response editStudent(User  u)  {
        try {
            if (u.getId() == -1)
                throw new IllegalArgumentException("Can't edit student since it does not exist in the database");

            repo.Update(u,u.getId());
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
