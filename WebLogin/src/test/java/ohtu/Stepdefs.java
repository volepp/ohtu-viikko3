package ohtu;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.junit.Assert.*;

import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Stepdefs {
    WebDriver driver = new HtmlUnitDriver();
    String baseUrl = "http://localhost:4567";
    
    @Given("^login is selected$")
    public void login_selected() throws Throwable {
        driver.get(baseUrl);
        WebElement element = driver.findElement(By.linkText("login"));       
        element.click();          
    } 
    
    @Given("^command new user is selected$")
    public void command_new_user_is_selected() throws Throwable {
        driver.get(baseUrl);
        WebElement element = driver.findElement(By.linkText("register new user"));
        element.click();
    }

    @When("^username \"([^\"]*)\" and password \"([^\"]*)\" are given$")
    public void username_and_password_are_given(String username, String password) throws Throwable {
        WebElement element = driver.findElement(By.name("username"));
        element.sendKeys(username);
        element = driver.findElement(By.name("password"));
        element.sendKeys(password);
        element = driver.findElement(By.name("login"));
        element.submit();  
    }

    @Then("^system will respond \"([^\"]*)\"$")
    public void system_will_respond(String pageContent) throws Throwable {
        assertTrue(driver.getPageSource().contains(pageContent));
    }
    
    @When("^correct username \"([^\"]*)\" and password \"([^\"]*)\" are given$")
    public void username_correct_and_password_are_given(String username, String password) throws Throwable {
        logInWith(username, password);
    }

    @When("^correct username \"([^\"]*)\" and incorrect password \"([^\"]*)\" are given$")
    public void username_and_incorrect_password_are_given(String username, String password) throws Throwable {
        logInWith(username, password);
    }
    
    @When("^incorrect username \"([^\"]*)\" and incorrect password \"([^\"]*)\" are given$")
    public void incorrect_username_and_incorrect_password_are_given(String username, String password) throws Throwable {
        logInWith(username, password);
    }
    
    @Then("^user is logged in$")
    public void user_is_logged_in() throws Throwable {
        pageHasContent("Ohtu Application main page");
    }
    
    @Then("^user is not logged in and error message is given$")
    public void user_is_not_logged_in_and_error_message_is_given() throws Throwable {
        pageHasContent("invalid username or password");
        pageHasContent("Give your credentials to login");
    }     
    
    @When("^a valid username \"([^\"]*)\" and password \"([^\"]*)\" and matching password confirmation are entered$")
    public void a_valid_username_and_password_and_matching_password_confirmation_are_entered(String username, String password) throws Throwable {
    	createUserWith(username, password, password);
    }

    @Then("^a new user is created$")
    public void a_new_user_is_created() throws Throwable {
    	pageHasContent("Welcome to Ohtu Application!");
    }

    @When("^an invalid username \"([^\"]*)\" and valid password \"([^\"]*)\" and matching password confirmation are entered$")
    public void an_invalid_username_and_valid_password_and_matching_password_confirmation_are_entered(String username, String password) throws Throwable {
        createUserWith(username, password, password);
    }

    @Then("^user is not created and error \"([^\"]*)\" is reported$")
    public void user_is_not_created_and_error_is_reported(String error) throws Throwable {
        pageHasContent(error);
    }

    @When("^a valid username \"([^\"]*)\" and invalid password \"([^\"]*)\" and matching password confirmation are entered$")
    public void a_valid_username_and_invalid_password_and_matching_password_confirmation_are_entered(String username, String password) throws Throwable {
        createUserWith(username, password, password);
    }

    @When("^an already taken username \"([^\"]*)\" and a valid password \"([^\"]*)\" and matching password confirmation are entered$")
    public void an_already_taken_username_and_a_valid_password_and_matching_password_confirmation_are_entered(String username, String password) throws Throwable {
        createUserWith(username, password, password);
    }
    
    @When("^a valid username \"([^\"]*)\" and a valid password \"([^\"]*)\" are password confirmation \"([^\"]*)\" are entered$")
    public void a_valid_username_and_a_valid_password_are_password_confirmation_are_entered(String username, String password, String passwordConfirmation) throws Throwable {
        createUserWith(username, password, passwordConfirmation);
    }
    
    @Given("^user with username \"([^\"]*)\" with password \"([^\"]*)\" is successfully created$")
    public void user_with_username_with_password_is_successfully_created(String username, String password) throws Throwable {
    	driver.get(baseUrl);
    	WebElement element = driver.findElement(By.linkText("register new user"));
    	element.click();
        createUserWith(username, password, password);
        element = driver.findElement(By.linkText("continue to application mainpage"));
        element.click();
        element = driver.findElement(By.linkText("logout"));
        element.click();
    }

    @When("^correct username \"([^\"]*)\" and correct password \"([^\"]*)\" are entered$")
    public void correct_username_and_correct_password_are_entered(String username, String password) throws Throwable {
        logInWith(username, password);
    }

    @Given("^user with username \"([^\"]*)\" and password \"([^\"]*)\" is tried to be created$")
    public void user_with_username_and_password_is_tried_to_be_created(String username, String password) throws Throwable {
    	driver.get(baseUrl);
    	WebElement element = driver.findElement(By.linkText("register new user"));
    	element.click();
        createUserWith(username, password, password);
        element = driver.findElement(By.linkText("back to home"));
    }

    @When("^incorrect username \"([^\"]*)\" and correct password \"([^\"]*)\" are entered$")
    public void incorrect_username_and_correct_password_are_entered(String username, String password) throws Throwable {
        logInWith(username, password);
    }

    
    @After
    public void tearDown(){
        driver.quit();
    }
        
    /* helper methods */
 
    private void pageHasContent(String content) {
        assertTrue(driver.getPageSource().contains(content));
    }
        
    private void logInWith(String username, String password) {
        assertTrue(driver.getPageSource().contains("Give your credentials to login"));
        WebElement element = driver.findElement(By.name("username"));
        element.sendKeys(username);
        element = driver.findElement(By.name("password"));
        element.sendKeys(password);
        element = driver.findElement(By.name("login"));
        element.submit();  
    } 
    
    private void createUserWith(String username, String password, String passwordConfirmation) {
    	assertTrue(driver.getPageSource().contains("Create username and give password"));
    	WebElement element = driver.findElement(By.name("username"));
        element.sendKeys(username);
        element = driver.findElement(By.name("password"));
        element.sendKeys(password);
        element = driver.findElement(By.name("passwordConfirmation"));
        element.sendKeys(passwordConfirmation);
        element = driver.findElement(By.name("signup"));
        element.submit();  
    }
}
