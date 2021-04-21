package com.example.e2e.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class SeleniumDemoTest {
    private WebDriver driver;

    @BeforeClass
    public void setUp()
    {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.navigate().to("https://the-internet.herokuapp.com/login");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(120, TimeUnit.MILLISECONDS);
    }

    @Test
    public void userLogin()
    {
        driver.navigate().to("https://the-internet.herokuapp.com/login");
        WebElement usernameTxt = driver.findElement(By.id("username"));
        usernameTxt.sendKeys("tomsmith");
        WebElement passwordTxt = driver.findElement(By.id("password"));
        passwordTxt.sendKeys("SuperSecretPassword!");
        WebElement submitBtn = driver.findElement(By.className("radius"));
        submitBtn.click();
        System.out.println("Current URL is:" + driver.getCurrentUrl());
        Assert.assertTrue(driver.getCurrentUrl().contains("secure"));
    }

    @Test
    public void userLoginLogin()
    {
        driver.navigate().to("http://localhost:8080/login");
        WebElement usernameTxt = driver.findElement(By.cssSelector("[name=\"username\"]"));
        usernameTxt.sendKeys("admin");
        WebElement passwordTxt = driver.findElement(By.cssSelector("[name=\"password\"]"));
        passwordTxt.sendKeys("123");
        WebElement submitBtn = driver.findElement(By.cssSelector("[type=\"submit\"]"));
        submitBtn.click();
    }

    @AfterClass
    public void tearDown(){
        if (driver != null) {
            driver.quit();
        }
    }
}
