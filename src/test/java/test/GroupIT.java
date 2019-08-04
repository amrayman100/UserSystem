package test;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.sumerge.program.viewmodels.GroupModel;
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


    @BeforeClass
    public static void init() {
        newGroup1 = new GroupModel("test"+System.currentTimeMillis(),"desc"+System.currentTimeMillis());
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

        response  = client.target("http://localhost:8880/users/group/"+newGroup1.getName()).register(feature).register(JacksonJsonProvider.class)
                .request(MediaType.APPLICATION_JSON)
                .get();
        Assert.assertEquals(200, response.getStatus());

    }




}
