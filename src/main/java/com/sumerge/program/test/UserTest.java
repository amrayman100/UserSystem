package com.sumerge.program.test;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.sumerge.program.viewmodels.UserModel;
import entities.User;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.impl.ResponseImpl;
import java.util.List;

@RunWith(JUnit4.class)
public class UserTest {

    @Test
    public void testGetUsers(){

        Client client= ClientBuilder.newClient();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "admin");

        Response response  = client.target("http://localhost:8880/users/user").register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .get();
        Assert.assertEquals(200, response.getStatus());
        List<User> users = response.readEntity(List.class);
        Assert.assertNotNull(users);
    }


    @Test
    public void testGetUser(){
        Client client= ClientBuilder.newClient();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "admin");

        Response response  = client.target("http://localhost:8880/users/user/amr_100").register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .get();
        Assert.assertEquals(200, response.getStatus());
        User user = response.readEntity(User.class);
        Assert.assertEquals("amr_100",user.getUsername());
    }

    @Test
    public void testAddUser(){
        UserModel newUser = new UserModel("Marucs","rashford_10","user","pass1","rash@email.com","1010");
        Client client= ClientBuilder.newClient();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "admin");

        /*Response response  = client.target("http://localhost:8880/users/user/amr_100").register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .post(entity(newUser));
        Assert.assertEquals(200, response.getStatus());*/


    }




}
