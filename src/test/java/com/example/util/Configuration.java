package com.example.util;

import org.testng.Reporter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
public class Configuration {
    private static final String NO_PROPERTY_WARNING = "No property found in the configuration file";
    private final Map<String, String> props = new HashMap<>();

    private Configuration() {
        try {
            Properties config = new Properties();
            config.load(new FileInputStream("config.properties"));
            config.stringPropertyNames().forEach(propName -> props.put(propName, config.getProperty(propName)));

            /*
             * Add the system's environment variables
             * Will override local config's values if environment variable set (i.e. Jenkins build variable)
             */
            Map<String, String> systemEnv = System.getenv();
            systemEnv.keySet().forEach(key -> props.put(key, systemEnv.get(key)));
        } catch (IOException ex) {
            Reporter.log("Error loading 'config.properties' file",true);
            ex.printStackTrace();
        }
    }

    public static String getConfigProperty(String key) {
        return getInstance().props.get(key);
    }

    public static String getStringProperty(String key) {
        return getStringProperty(key, NO_PROPERTY_WARNING);
    }

    public static String getStringProperty(String key, Object defaultValue) {
        String property = getConfigProperty(key);
        return property == null || property.trim().length() == 0 ? String.valueOf(defaultValue) : property;
    }

    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        return Boolean.parseBoolean(getStringProperty(key, defaultValue));
    }

    public static int getIntProperty(String key, int defaultValue) {
        return Integer.parseInt(getStringProperty(key, defaultValue));
    }

    public static double getDoubleProperty(String key, double defaultValue) {
        return Double.parseDouble(getStringProperty(key, defaultValue));
    }

    private static volatile Configuration INSTANCE;

    private static Configuration getInstance() {
        Configuration localInstance = INSTANCE;
        if (localInstance == null) {
            synchronized (Configuration.class) {
                localInstance = INSTANCE;
                if (localInstance == null) {
                    INSTANCE = localInstance = new Configuration();
                }
            }
        }
        return localInstance;
    }
}
