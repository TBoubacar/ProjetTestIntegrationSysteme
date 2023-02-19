package me.ktkim.blog.api;

import com.jayway.jsonpath.JsonPath;
import me.ktkim.blog.BlogApplication;
import me.ktkim.blog.model.domain.Post;
import me.ktkim.blog.service.PostService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.hamcrest.Matchers;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes= BlogApplication.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ApplicationContext applicationContext;
    private String authentificationToken = "";

    public AuthControllerTest() {
    }

    /**
     * Question 2.2
     * @throws Exception
     *
     */
    @Test
    public void goodAuthenticationInformationTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/auth/authenticate")
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .accept(MediaType.APPLICATION_JSON)
                                                    .content("{\"email\":\"admin@mail.com\"," +
                                                            "\"password\" :\"admin\"}"))
                                                    .andExpect(status().isOk())
                                                    .andReturn();
        Assert.assertEquals("application/json", mvcResult.getResponse().getContentType());
        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    public void badAuthenticationInformationTest() throws Exception {
        MvcResult mvcResult =  mockMvc.perform(post("/auth/authenticate")
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .accept(MediaType.APPLICATION_JSON)
                                                    .content("{\"email\":\"not-admin@mail.com\"," +
                                                            "\"password\" :\"xxxx\"}"))
                                                    .andExpect(status().is(401))
                                                    .andReturn();

        Assert.assertEquals("application/json", mvcResult.getResponse().getContentType());
        Assert.assertEquals(401, mvcResult.getResponse().getStatus());
    }


    /**
     * Question 2.3
     * Test With le token
     */
    @Test
    public void isAuthenticatedWithTokenTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/auth/authenticate")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .accept(MediaType.APPLICATION_JSON)
                                                .content("{\"email\":\"admin@mail.com\"," +
                                                        "\"password\" :\"admin\"}"))
                                    .andExpect(status().isOk())
                                    .andReturn();
        Assert.assertEquals("application/json", mvcResult.getResponse().getContentType());
        authentificationToken = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.token");

        mvcResult = mockMvc.perform(post("/auth/user")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .header("Authorization", authentificationToken)
                                    )
                            .andExpect(status().isOk())
                            .andReturn();


        String jsonExpected="{\"id\":3,\"email\":\"admin@mail.com\",\"userName\":\"admin\",\"provider\":\"local\"}";
        Assert.assertEquals(jsonExpected, mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void isAuthenticatedWithBadTokenTest() throws Exception {
        MvcResult  mvcResult =  mockMvc.perform(post("/auth/user")
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .accept(MediaType.APPLICATION_JSON)
                                                    .header("Authorization", authentificationToken))
                                        .andExpect(status().is(401))
                                        .andReturn();
        Assert.assertEquals(401, mvcResult.getResponse().getStatus());
    }

    /**
     * Question 2.4
     * UserDetails Version
     */
    @Test
    @WithUserDetails(value="admin@mail.com", userDetailsServiceBeanName = "customUserDetailsService")
    public void createPageWithPostRequest() throws Exception {
        String newPost = "{\"title\":\"Test with Post Request\",\"body\":\"<p>This page is created by post request !</p>\"}";

        MvcResult mvcResult = mockMvc.perform(post("/api/posts")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .accept(MediaType.APPLICATION_JSON)
                                                .content(newPost))
                                    .andExpect(status().is(201))
                                    .andExpect(jsonPath("title", is("Test with Post Request")))
                                    .andExpect(jsonPath("body", is("<p>This page is created by post request !</p>")))
                                    .andReturn();
        String title = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.title");
        String body = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.body");

        Assert.assertEquals(201, mvcResult.getResponse().getStatus());
        Assert.assertEquals(title, "Test with Post Request");
        Assert.assertEquals(body, "<p>This page is created by post request !</p>" );

    }

    /**
     * Question 2.5
     * Test with context
     */
    @Test
    @WithUserDetails(value="admin@mail.com",userDetailsServiceBeanName = "customUserDetailsService")
    public void createPageWithPostRequestWhenAuthenticatedAndVerified() throws Exception {
        PostService postService = (PostService) applicationContext.getBean("postService");
        String newPost = "{\"title\":\"Test with Post Request\",\"body\":\"<p>This page is created by post request !</p>\"}";

        MvcResult mvcResult = mockMvc.perform(post("/api/posts")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .accept(MediaType.APPLICATION_JSON)
                                            .content(newPost))
                                    .andExpect(status().is(201))
                                    .andExpect(jsonPath("title", is("Test with Post Request")))
                                    .andExpect(jsonPath("body", is("<p>This page is created by post request !</p>")))
                                    .andReturn();
        int idInt = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

        long id = Long.valueOf(idInt);
        Optional<Post> optional = postService.findForId(id);
        Post post = optional.get();
        Assert.assertEquals("Test with Post Request", post.getTitle());

    }
}
