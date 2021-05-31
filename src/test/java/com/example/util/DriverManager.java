package com.example.util;

import java.net.URL;
import java.util.logging.Level;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.*;
import org.testng.Reporter;

public abstract class DriverManager {
    private static Resolution currentResolution;

    abstract protected WebDriver createWebDriver(DesiredCapabilities capabilities);

    static int windowWidthCorrection = 0, windowHeightCorrection = 0;

    public static WebDriver initDriver() {
        return initDriver("");
    }

    public static WebDriver initDriver(String name) {
        return initDriver(name, "", "");
    }


    public static WebDriver initDriver(String name, String version, String platform) {
        DriverType driverType = DriverType.fromString(StringHelper.isNullOrBlank(name) ? "" : name);
        DesiredCapabilities capabilities = driverType.getCapabilities();

        if (!StringHelper.isNullOrBlank(version)) {
            capabilities.setVersion(version);
        }
        if (!StringHelper.isNullOrBlank(platform)) {
            capabilities.setPlatform(Platform.fromString(platform.toUpperCase()));
        }
        capabilities.setCapability("enableVNC", true);
        WebDriver driver = null;
        try {
            if (!Configuration.getBooleanProperty("SELENIUM_GRID", false)) {
                driverType.registerDriver();
                driver = driverType.getType().newInstance().createWebDriver(capabilities);
                //DriverManagerType chrome = DriverManagerType.CHROME;
                //WebDriverManager.getInstance(chrome).setup();
                //Class<?> chromeClass =  Class.forName(chrome.browserClass());
                //driver = (WebDriver) chromeClass.newInstance();
            } else {
                final RemoteWebDriver remoteWebDriver = new RemoteWebDriver(
                        new URL(Configuration.getStringProperty("HUB_URL")),
                        capabilities);

                remoteWebDriver.setFileDetector(new LocalFileDetector());
                driver = new Augmenter().augment(remoteWebDriver);
            }
            setUpPosition(driver);
            Reporter.log("WebDriver started: " + driver, true);
        } catch (Throwable e) {
            Reporter.log("ERROR: failed to load the WebDriver: " + driverType.getType(), true);
            e.printStackTrace();
        }
        return driver;
    }

    protected DesiredCapabilities addCustomCapabilities(DesiredCapabilities capabilities) {
        /*
         * Enable logging. By default, BROWSER log at SEVERE level enabled.
         */
        LoggingPreferences logs = new LoggingPreferences();
        Level logLevel = Level.parse(Configuration.getStringProperty("LOG_LEVEL", Level.SEVERE.toString()));
        String logType = Configuration.getStringProperty("LOG_TYPE", LogType.BROWSER);
        logs.enable(logType.trim().toLowerCase(), logLevel);
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, logs);
        return capabilities;
    }

    public static Resolution getCurrentResolution() {
        return currentResolution;
    }

    public static void setUpPosition(WebDriver driver) {
        boolean setPosition = Configuration.getBooleanProperty("SET_DRIVER_POSITION", true);
        String resolution = Configuration.getStringProperty("RESOLUTION", null);

        if (setPosition && resolution != null) {
            currentResolution = Resolution.valueOf(resolution);

            driver.manage().window().setPosition(new Point(
                    Configuration.getIntProperty("BROWSER_X_POS", 0),
                    Configuration.getIntProperty("BROWSER_Y_POS", 0)));

            driver.manage().window().setSize(
                    new Dimension(
                            currentResolution.getWidth() + windowWidthCorrection,
                            currentResolution.getHeight() + windowHeightCorrection));
        } else {
            Reporter.log("SET_DRIVER_POSITION is false, maximizing browser's window.");
            driver.manage().window().maximize();
        }
        int x = driver.manage().window().getPosition().getX();
        int y = driver.manage().window().getPosition().getY();
        int w = driver.manage().window().getSize().getWidth();
        int h = driver.manage().window().getSize().getHeight();
        Reporter.log(String.format("Browser window dimentions -> (x: %s, y: %s); %sx%s.", x, y, w, h), true);
    }
}
