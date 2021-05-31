package com.example.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Arrays;

public class GoogleChrome extends DriverManager {

    @Override
    public WebDriver createWebDriver(DesiredCapabilities capabilities) {
        return new ChromeDriver(
                DesiredCapabilities.chrome().merge(
                        addCustomCapabilities(capabilities)));
    }

    @Override
    protected DesiredCapabilities addCustomCapabilities(DesiredCapabilities capabilities) {
        super.addCustomCapabilities(capabilities);
        ChromeOptions options = new ChromeOptions();
        String args = Configuration.getStringProperty("BROWSER_ARGS");
        if (args != null) {
            Arrays.stream(args.split("\\s")).forEach(options::addArguments);
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        }
        windowWidthCorrection = -1;//TODO delete when solved chrome window width diff.
        return capabilities;
    }
}