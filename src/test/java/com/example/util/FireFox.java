package com.example.util;

import com.google.common.base.Strings;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Reporter;

public class FireFox extends DriverManager {

    @Override
    public WebDriver createWebDriver(DesiredCapabilities capabilities) {
        return new FirefoxDriver(
                DesiredCapabilities.firefox().merge(
                        addCustomCapabilities(capabilities)));
    }

    @Override
    protected DesiredCapabilities addCustomCapabilities(DesiredCapabilities capabilities) {
        super.addCustomCapabilities(capabilities);

        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("network.automatic-ntlm-auth.trusted-uris", "http://,https://");
        profile.setPreference("network.automatic-ntlm-auth.allow-non-fqdn", true);
        profile.setPreference("network.negotiate-auth.delegation-uris", "http://,https://");
        profile.setPreference("network.negotiate-auth.trusted-uris", "http://,https://");
        profile.setPreference("network.http.phishy-userpass-length", 255);
        profile.setPreference("security.csp.enable", false);
        profile.setPreference("browser.download.dir", Configuration.getStringProperty("DOWNLOAD_FOLDER"));
        profile.setPreference("intl.accept_languages", Configuration.getStringProperty("BROWSER_LANGUAGE", "en-us"));
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
                "application/csv, text/csv, text/comma-separated-values");

        capabilities.setCapability(FirefoxDriver.PROFILE, profile);

        /*
         * Selenium uses W3C Webdriver protocol to send requests to Geckodriver,
         * Which translates them and uses Marionette protocol to send them to Firefox
         * Selenium<--(W3C Webdriver)-->Geckodriver<---(Marionette)--->Firefox
         *
         * Turn on the Marionette and disable it's debug log.
         */
        capabilities.setCapability(FirefoxDriver.MARIONETTE, true);
        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");

        boolean customLocation = Configuration.getBooleanProperty("CUSTOM_BROWSER_LOCATION", false);
        if (customLocation) {
            String binary = Configuration.getStringProperty("BROWSER_BINARY", "");
            if (!Strings.isNullOrEmpty(binary)) {
                capabilities.setCapability(FirefoxDriver.BINARY, binary);
            } else {
                Reporter.log("WARNING: CUSTOM_BROWSER_LOCATION set to true but the path to binary was not found", true);
            }
        }
        return capabilities;
    }
}