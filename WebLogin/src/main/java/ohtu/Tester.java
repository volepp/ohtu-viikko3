package ohtu;

import java.util.Random;

import javax.management.RuntimeErrorException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Tester {

    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();

        driver.get("http://localhost:4567");
        
        sleep(2);

        testLogin(driver, "pekka", "akkep");

        sleep(3);
        
        WebElement element = driver.findElement(By.linkText("logout"));
        element.click();
        
        sleep(2);
        
        testLogin(driver, "pekka", "väärä");
        
        sleep(2);
        
        element = driver.findElement(By.linkText("back to home"));
        element.click();
        
        testLogin(driver, "sauli", "testi");
        
        sleep(2);
        
        element = driver.findElement(By.linkText("back to home"));
        element.click();
        
        sleep(2);
        
        testRegister(driver, "sauli", "test");
        
        sleep(2);
        
        element = driver.findElement(By.linkText("continue to application mainpage"));
        element.click();
        
        sleep(2);
        
        element = driver.findElement(By.linkText("logout"));
        element.click();
        
        sleep(3);
        
        driver.quit();
    }
    
    private static void testLogin(WebDriver driver, String username, String password) {
    	WebElement element = driver.findElement(By.linkText("login"));
        element.click();

        sleep(2);
    	
        element = driver.findElement(By.name("username"));
        element.sendKeys(username);
        element = driver.findElement(By.name("password"));
        element.sendKeys(password);
        element = driver.findElement(By.name("login"));
        
        sleep(2);
        element.submit();
    }
    
    private static void testRegister(WebDriver driver, String username, String password) {
    	WebElement element = driver.findElement(By.linkText("register new user"));
    	element.click();
    	
    	sleep(2);
    	
    	Random r = new Random();
    	
    	element = driver.findElement(By.name("username"));
        element.sendKeys(username + r.nextInt(1000));
        element = driver.findElement(By.name("password"));
        element.sendKeys(password);
        element = driver.findElement(By.name("passwordConfirmation"));
        element.sendKeys(password);
        element = driver.findElement(By.name("signup"));
    	
        sleep(2);
        element.submit();
    }
    
    private static void sleep(int n){
        try{
            Thread.sleep(n*1000);
        } catch(Exception e){}
    }
}
