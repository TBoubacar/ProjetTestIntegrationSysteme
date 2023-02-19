package me.ktkim.blog.front;

import io.github.bonigarcia.wdm.WebDriverManager;
import me.ktkim.blog.model.domain.Post;
import me.ktkim.blog.service.PostService;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class TestsFront {
    @Autowired
    ApplicationContext applicationContext;
    @LocalServerPort
    private Integer port;
    private WebDriver webDriver =  null;;
    private String baseUrl;

    @BeforeAll
    public static void setUpFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
    }

    @BeforeEach
    public void setUpWebDriver() {
        webDriver = new FirefoxDriver();
        baseUrl = "http://localhost:3000" +"/api/posts";
    }

    @AfterEach
    public void quitWebDriver() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    /**
     * Question 5.1
     *
     */
    @Test
    public void verifyFirstPostHasTitleStandardLoremIpsumTest(){
        webDriver.get(baseUrl);
        final WebElement element = webDriver.findElement(By.xpath("//h2//a[@href='/posts/1']"));
        final String elementTitle = element.getText();
        assertThat(elementTitle).isEqualTo("The standard Lorem Ipsum passage");
    }

    /**
     * Question 5.2
     *
     */
    @Test
    public void verifyFirstPostCommentAfterClickTest() {
        PostService postService = (PostService) applicationContext.getBean("postService");
        long id = 1;
        Optional<Post> opt=postService.findForId(id);
        Post post = opt.get();
        webDriver.get(baseUrl);

        final WebElement readMoreElement =  webDriver.findElement(By.xpath("//div[@class='post']" + "/a[@href='/posts/1']"));
        readMoreElement.click();
        final WebElement element = webDriver.findElement(By.xpath("//div[@class='post-body']" + "/p"));
        final String elementBody = element.getText();

        Assert.assertEquals(post.getBody(), "<p>"+elementBody+"</p>");
    }

    /**
     * Question 5.3
     * With User admin
     */
    @Test
    public void goodAuthenticationTest(){
        webDriver.get(baseUrl);
        final WebElement accountElement =   webDriver.findElement(By.id("account-menu"));
        accountElement.click();
        final WebElement loginElement =  webDriver.findElement(By.id("login-item"));
        loginElement.click();
        final WebElement userName =   webDriver.findElement(By.id("username"));
        final WebElement passWord= webDriver.findElement(By.id("password"));
        userName.sendKeys("admin@mail.com");
        passWord.sendKeys("admin");
        final WebElement submitElement = webDriver.findElement(By.xpath("//button[@type='submit']"));
        submitElement.click();

        final WebElement newPostElement = webDriver.findElement(By.xpath("//a[@href='/editor']"));
        String postName = newPostElement.getText();
        assertEquals(postName, "NEW POST");
    }

    @Test
    public void badAuthenticationTest(){
        webDriver.get(baseUrl);
        final WebElement accountElement =   webDriver.findElement(By.id("account-menu"));
        accountElement.click();
        final WebElement loginElement =  webDriver.findElement(By.id("login-item"));
        loginElement.click();
        final WebElement userName =   webDriver.findElement(By.id("username"));
        final WebElement passWord= webDriver.findElement(By.id("password"));
        userName.sendKeys("not-admin@mail.com");
        passWord.sendKeys("xxxx");
        final WebElement submitElement = webDriver.findElement(By.xpath("//button[@type='submit']"));
        submitElement.click();

        assertThrows(org.openqa.selenium.TimeoutException.class,()->{
            new WebDriverWait(webDriver,3).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@href='/editor']")));
        });
    }

    /**
     * Question 5.4
     *
     */
    @Test
    public void verifyIfThereAreAGoodTitleTest(){
        String url ="http://localhost:3000/login";
        webDriver.get(url);

        final WebElement userName =   webDriver.findElement(By.id("username"));
        final WebElement passWord= webDriver.findElement(By.id("password"));
        userName.sendKeys("admin@mail.com");
        passWord.sendKeys("admin");

        final WebElement submitElement = webDriver.findElement(By.xpath("//button[@type='submit']"));
        submitElement.click();
        final WebElement newPostElement = webDriver.findElement(By.xpath("//a[@href='/editor']"));
        newPostElement.click();

        final WebElement newPostTile = webDriver.findElement(By.xpath("//input[@placeholder='Enter title here']"));
        newPostTile.sendKeys("Je suis le titre du post");
        final WebElement saveButton = webDriver.findElement(By.xpath("//div[@class='editor-button']/button"));
        saveButton.click();

        final WebElement newPostCreated = webDriver.findElement(By.xpath("//h2/a"));
        String text = newPostCreated.getText();
        assertEquals(text, "Je suis le titre du post");
    }
}
