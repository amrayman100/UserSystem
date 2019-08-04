package test;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.sumerge.program.viewmodels.UserModel;
import entities.User;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.impl.ResponseImpl;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;
@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserIT {
    private static UserModel newUser;

    @BeforeClass
    public static void init() {
        newUser = new UserModel("jake","test"+System.currentTimeMillis(),"user","pass1","rash@email.com","1010");
    }

    @Test
    public void test01_GetUsers(){

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
    public void test02_GetUser(){
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
    public void test03_AddUser(){
         //newUser = new UserModel("jake","jake_10","user","pass1","rash@email.com","1010");
        Client client= ClientBuilder.newClient();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "admin");

        Response response  = client.target("http://localhost:8880/users/user/").register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(newUser,MediaType.APPLICATION_JSON));
        Assert.assertEquals(200, response.getStatus());

        client= ClientBuilder.newClient();
        feature = HttpAuthenticationFeature.basic("user", "user");

         response  = client.target("http://localhost:8880/users/user/").register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(newUser,MediaType.APPLICATION_JSON));
        Assert.assertEquals(401, response.getStatus());

    }

    @Test
    public void test04_DeleteUser(){
        System.out.println(newUser.getUsername());
        Client client= ClientBuilder.newClient();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "admin");
        //client.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        Response response  = client.target("http://localhost:8880/users/user/"+newUser.getUsername()).register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .delete();

        Response response2  = client.target("http://localhost:8880/users/user/"+newUser.getUsername()).register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .get();
        Assert.assertEquals(200, response.getStatus());
        User user = response2.readEntity(User.class);


        Assert.assertEquals(user.isDel(), true);


    }

    @Test
    public void test05_UndoDeleteUser(){
        System.out.println(newUser.getUsername());
        Client client= ClientBuilder.newClient();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "admin");
        client.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        Response response  = client.target("http://localhost:8880/users/user/"+newUser.getUsername()).register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .put(null);

        Response response2  = client.target("http://localhost:8880/users/user/"+newUser.getUsername()).register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .get();
        Assert.assertEquals(200, response.getStatus());
        User user = response2.readEntity(User.class);


        Assert.assertEquals(user.isDel(), false);


    }

    @Test
    public void test06_UpdateUserDetails(){
        String updatedUser = "test"+System.currentTimeMillis();
        Client client= ClientBuilder.newClient();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "admin");
        client.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        Response response  = client.target("http://localhost:8880/users/user/username/"+newUser.getUsername()).register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(updatedUser,MediaType.APPLICATION_JSON));
        Response response2  = client.target("http://localhost:8880/users/user/"+updatedUser).register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .get();
        Assert.assertEquals(200, response.getStatus());
        User user = response2.readEntity(User.class);
        Assert.assertEquals(user.getUsername(), updatedUser);
        newUser.setUsername(updatedUser);

         response  = client.target("http://localhost:8880/users/user/email/"+newUser.getUsername()).register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity("max@email.com",MediaType.APPLICATION_JSON));
          response2  = client.target("http://localhost:8880/users/user/"+updatedUser).register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .get();
        Assert.assertEquals(200, response.getStatus());
        user = response2.readEntity(User.class);
        Assert.assertEquals(user.getEmail(), "max@email.com");
        newUser.setUsername(updatedUser);

    }


    @Test
    public void test07_AddToGroup(){

        Client client= ClientBuilder.newClient();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "admin");

        Response response  = client.target("http://localhost:8880/users/user/group/"+newUser.getUsername()+"/1").register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .post(null);
        Assert.assertEquals(200, response.getStatus());

    }

    @Test
    public void test08_RemoveFromGroup(){

        Client client= ClientBuilder.newClient();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "admin");

        Response response  = client.target("http://localhost:8880/users/user/group/"+newUser.getUsername()+"/1").register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .delete();
        Assert.assertEquals(200, response.getStatus());

    }

    @Test
    public void test09_UpdatePassword(){

        String updatedPass = "pass";
        Client client= ClientBuilder.newClient();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "admin");
        client.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        Response response  = client.target("http://localhost:8880/users/user/password/"+newUser.getUsername()).register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(updatedPass,MediaType.APPLICATION_JSON));
        Response response2  = client.target("http://localhost:8880/users/user/"+newUser.getUsername()).register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .get();
        Assert.assertEquals(200, response.getStatus());


    }







}
