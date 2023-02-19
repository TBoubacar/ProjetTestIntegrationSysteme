package me.ktkim.blog.api;

import com.jayway.jsonpath.JsonPath;
import me.ktkim.blog.BlogApplication;
import me.ktkim.blog.service.PostService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes= BlogApplication.class)
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    PostService postService;

    @BeforeEach
    public void init() { postService = new PostService();    }

    /**
     * Question 2.1c
     * @throws Exception
     *
     */
    @Test
    public void verifyIsGoodIdTest() throws Exception {
        long id=1;
        String texte="<p>\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt " +
                "ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris " +
                "nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit " +
                "esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, " +
                "sunt in culpa qui officia deserunt mollit anim id est laborum.\"</p>";

        MvcResult mvcResult = mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("userId", is(1)))
                .andExpect(jsonPath("body", is(texte)))
                .andReturn();

        String body= JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.body");
        assertEquals("application/json", mvcResult.getResponse().getContentType());
        assertEquals(body, texte);
    }

    @Test
    public void verifyIsBadIdTest() throws Exception {
        mockMvc.perform(get("/api/posts/6"))
                .andExpect(status().is(404))
                .andExpect(jsonPath("errorMessage", is("Post does not exist")));
    }
}
