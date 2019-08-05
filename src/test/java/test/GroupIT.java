package test;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.sumerge.program.viewmodels.GroupModel;
import com.sumerge.program.viewmodels.UserModel;
import entities.Group;
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
public class GroupIT {
    private static GroupModel newGroup1;
    private static GroupModel newGroup2;
    private static UserModel newUser;


    @BeforeClass
    public static void init() {
        newGroup1 = new GroupModel("test"+System.currentTimeMillis(),"desc"+System.currentTimeMillis());
        newGroup2 = new GroupModel("test2"+System.currentTimeMillis(),"desc"+System.currentTimeMillis());
        newUser = new UserModel("Steve","test2"+System.currentTimeMillis(),"user","pass1","rash@email.com","1010");
        Client client= ClientBuilder.newClient();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "admin");

        Response response  = client.target("http://localhost:8880/users/user/").register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(newUser,MediaType.APPLICATION_JSON));
        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void test01_GetGroups(){

        Client client= ClientBuilder.newClient();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "admin");

        Response response  = client.target("http://localhost:8880/users/group").register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .get();
        Assert.assertEquals(200, response.getStatus());
        List<Group> groups = response.readEntity(List.class);
        Assert.assertNotNull(groups);
    }


    @Test
    public void test02_AddGroup(){
        Client client= ClientBuilder.newClient();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "admin");

        Response response  = client.target("http://localhost:8880/users/group/").register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(newGroup1,MediaType.APPLICATION_JSON));
        Assert.assertEquals(200, response.getStatus());

        response  = client.target("http://localhost:8880/users/group/").register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(newGroup2,MediaType.APPLICATION_JSON));
        Assert.assertEquals(200, response.getStatus());

        response  = client.target("http://localhost:8880/users/group/"+newGroup1.getName()).register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .get();
        Assert.assertEquals(200, response.getStatus());

    }
    @Test
    public void test03_MoveFromGroup(){
        Client client= ClientBuilder.newClient();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "admin");
        Response response  = client.target("http://localhost:8880/users/group/"+newGroup1.getName()).register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .get();
        Assert.assertEquals(200, response.getStatus());
        Group group1 = response.readEntity(Group.class);
        response  = client.target("http://localhost:8880/users/group/"+newGroup2.getName()).register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .get();
        Assert.assertEquals(200, response.getStatus());
        Group group2 = response.readEntity(Group.class);
        response  = client.target("http://localhost:8880/users/user/"+newGroup2.getName()).register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .get();
        Assert.assertEquals(200, response.getStatus());
        User user = response.readEntity(User.class);

        response  = client.target("http://localhost:8880/users/user/group/"+newUser.getUsername()+"/"+group1.getId()).register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .post(null);
        Assert.assertEquals(200, response.getStatus());


        client.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
         response  = client.target("http://localhost:8880/users/user/group/"+newUser.getUsername()+"/"+group1.getId()+"/"+group2.getId()).register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .put(null);

        Assert.assertEquals(200, response.getStatus());

    }
    @Test
    public void test04_UpdateGroupDetails(){
        String gName = "testUpdate"+System.currentTimeMillis();
        Client client= ClientBuilder.newClient();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "admin");

        Response response  = client.target("http://localhost:8880/users/group/"+newGroup1.getName()).register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .get();
        Assert.assertEquals(200, response.getStatus());
        Group group1 = response.readEntity(Group.class);

        client.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        response  = client.target("http://localhost:8880/users/group/name/"+group1.getId()).register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(gName,MediaType.APPLICATION_JSON));
        Response response2  = client.target("http://localhost:8880/users/group/"+gName).register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .get();
        Assert.assertEquals(200, response.getStatus());
        Group updatedGroup = response2.readEntity(Group.class);

        Assert.assertEquals(updatedGroup.getName(), gName);


    }


    @Test
    public void test05_DeleteGroup(){

        Client client= ClientBuilder.newClient();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "admin");
        client.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        Response response  = client.target("http://localhost:8880/users/group/"+newGroup2.getName()).register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .get();
        Assert.assertEquals(200, response.getStatus());
        Group group2 = response.readEntity(Group.class);

        feature = HttpAuthenticationFeature.basic("amr_100", "user");
        client.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        response  = client.target("http://localhost:8880/users/group/"+group2.getId()).register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .delete();
        Assert.assertEquals(403, response.getStatus());


    }




}
