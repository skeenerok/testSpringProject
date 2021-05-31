package com.example.util;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Reporter;

import java.util.function.Supplier;
public enum DriverType {
    FIREFOX(FireFox.class, DesiredCapabilities::firefox, WebDriverManager::firefoxdriver),
    CHROME(GoogleChrome.class, DesiredCapabilities::chrome, WebDriverManager::chromedriver);


    private final Class<? extends DriverManager> type;
    private final Supplier<DesiredCapabilities> defaultCapabilities;
    private final Supplier<WebDriverManager> webDriverManager;

    <P extends DriverManager> DriverType(
            Class<P> browserClass,
            Supplier<DesiredCapabilities> capabilities,
            Supplier<WebDriverManager> browserManager) {
        type = browserClass;
        defaultCapabilities = capabilities;
        webDriverManager = browserManager;
    }

    /**
     * Calls the setup() method for selected browser and registers the path to it in the system
     */
    public void registerDriver() {
        synchronized (WebDriverManager.class) {
            Reporter.log("Registering a WebDriver -> " + defaultCapabilities.get(), true);
            setDriverManagerProperties();
            webDriverManager.get().setup();
        }
    }

    public Class<? extends DriverManager> getType() {
        return type;
    }

    public DesiredCapabilities getCapabilities() {
        return defaultCapabilities.get();
    }

    public static DriverType fromString(String browserName) {
        DriverType browser;
        if (browserName == null) {
            browserName = "";
        }

        switch (browserName.toLowerCase()) {
            case "chrome":
                browser = CHROME;
                break;
            case "firefox":
                browser = FIREFOX;
                break;
            default:
                browser = CHROME;
                System.out.printf("Unsupported browser name: '%s' in '%s'. '%s' will be loaded\n",
                        browserName, DriverType.class.getName(), browser.name());
        }
        return browser;
    }

    /**
     * Will take the version, specified in the config.properties, if set
     * Caches downloaded WebDriver by default.
     * Can Override this to always download the driver before tests run (FORCE_CACHE in config)
     */
    private void setDriverManagerProperties() {
        String chromeDriverVersion = Configuration.getStringProperty("CHROME_DRIVER_VERSION", "LATEST");
        System.setProperty("wdm.chromeDriverVersion", chromeDriverVersion);

        String geckoDriverVersion = Configuration.getStringProperty("GECKO_DRIVER_VERSION", "LATEST");
        System.setProperty("wdm.geckoDriverVersion", geckoDriverVersion);

        String forceCache = Configuration.getStringProperty("FORCE_CACHE", false);
        System.setProperty("wdm.forceCache", forceCache);
    }
}
