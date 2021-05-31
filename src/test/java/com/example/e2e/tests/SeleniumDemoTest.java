package com.example.e2e.tests;

import com.automation.remarks.testng.UniversalVideoListener;
import com.automation.remarks.video.annotations.Video;
import com.automation.remarks.video.enums.RecorderType;
import com.automation.remarks.video.enums.RecordingMode;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.example.util.Configuration;
import com.example.util.IDriverSupplier;
import com.example.util.ITestArtifactProvider;
import com.example.util.TestListener;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import io.qameta.allure.selenide.LogType;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@Listeners({UniversalVideoListener.class, TestListener.class})
public class SeleniumDemoTest implements IDriverSupplier, ITestArtifactProvider {
    private WebDriver driver;
    @Override
    public List<Map<String, BufferedImage>> getArtifacts() {
        return new ArrayList<>();
    }
    @Override
    public WebDriver getDriver() {
        return driver;
    }
    @BeforeClass
    public void setUp()
    {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        //options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.navigate().to("https://the-internet.herokuapp.com/login");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(120, TimeUnit.MILLISECONDS);
        WebDriverRunner.setWebDriver(driver);
        com.codeborne.selenide.Configuration.timeout = Configuration.getIntProperty("SELENIDE.TIMEOUT", 4000);
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(false));
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().enableLogs(LogType.BROWSER, Level.ALL));
    }
    @BeforeMethod
    public void before(){
        System.setProperty("video.mode", RecordingMode.ALL.toString());
        System.setProperty("recorder.type", RecorderType.FFMPEG.toString());
    }
    @Severity(SeverityLevel.CRITICAL)
    @Story("Demo login test")
    @Description("Use correct login")
    @Video
    @Test
    public void userLogin()
    {
        open("https://the-internet.herokuapp.com/login");
        setName("tomsmith");
        setPassword("SuperSecretPassword");
        submit();
    }
    @Severity(SeverityLevel.CRITICAL)
    @Story("Demo login test")
    @Description("Use incorrect login")
    @Test
    @Video
    public void userLoginIncorrect()
    {
        open("https://the-internet.herokuapp.com/login");
        setName("tomsmith");
        setPassword("SuperSecretPassword");
        submit();
        Assert.assertTrue(driver.getCurrentUrl().contains("secure"));
    }

    @Story("newFeature1")
    @Test(enabled = false)
    public void newFeature1()
    {
        driver.navigate().to("http://localhost:8080/login");
        WebElement usernameTxt = driver.findElement(By.cssSelector("[name=\"username\"]"));
        usernameTxt.sendKeys("admin");
        WebElement passwordTxt = driver.findElement(By.cssSelector("[name=\"password\"]"));
        passwordTxt.sendKeys("123");
        new WebDriverWait(driver, 40)
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector("form[action=\"/login\"] [type=\"submit\"]")))
                .click();
    }
    @Step
    public void setName(String name){
        $("#username").should(Condition.exist).setValue(name);
        addScreenshot("setName", driver);
    }
    @Step
    public void setPassword(String password){
        $("#password").should(Condition.exist).setValue(password);
        addScreenshot("setPassword", driver);
    }
    @Step
    public void submit(){
        $(".radius").should(Condition.exist).click();
    }
    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        long time = result.getEndMillis() - result.getStartMillis();
        String formattedTime = String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(time),
                TimeUnit.MILLISECONDS.toSeconds(time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
        String testResult = "STARTED";
        int testStatus = result.getStatus();
        if (ITestResult.SUCCESS == testStatus) {
            testResult = "SUCCESSFUL";
        } else if (ITestResult.FAILURE == testStatus) {
            testResult = "FAILED";
        } else if (ITestResult.SKIP == testStatus) {
            testResult = "SKIPPED";
        }
        processScreenshot(result);
        Reporter.log(
                "[AFTER METHOD]: " + result.getMethod().getMethodName() +
                        " | Result: " + testResult +
                        " | Time: " + formattedTime,
                true);
    }
    @AfterClass
    public void tearDown(){
        if (driver != null) {
            driver.quit();
        }
    }
    public void processScreenshot(ITestResult result){
        try {
            if (driver != null) {
                addScreenshot("screenshot_" + result.getMethod().getMethodName(), driver);
                Reporter.log("screenshot at: " + driver.getCurrentUrl(), true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Attachment(value = "{0}", type = "image/png")
    public static byte[] addScreenshot(String name, WebDriver driver) {
        if (driver == null) {
            Reporter.log("WARNING: Unable to get WebDriver to make a screenshot", true);
            return new byte[0];
        }
        byte[] screenShot;
        try {
            screenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            Reporter.log("Can't take a screenshot: " + e.getMessage(), true);
            return new byte[0];
        }
        return screenShot;
    }
}
