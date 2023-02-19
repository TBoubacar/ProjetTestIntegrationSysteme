package me.ktkim.blog;

import me.ktkim.blog.api.PostController;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.hamcrest.Matchers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.springframework.test.web.servlet.MockMvc;

import javax.inject.Inject;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(controllers = PostController.class)
public class BlogApplicationTests {

	
	
    @Test
    //@Disabled
    public void contextLoads() {
    	
    }

}
