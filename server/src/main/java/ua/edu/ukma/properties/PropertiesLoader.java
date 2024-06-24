package ua.edu.ukma.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    private static final String CONFIG_FILENAME = "application.properties";

    public Properties loadProperties() {
        Properties properties = new Properties();
        try {
            ClassLoader classLoader = PropertiesLoader.class.getClassLoader();
            InputStream input = classLoader.getResourceAsStream(CONFIG_FILENAME);

            if (input != null) {
                properties.load(input);
            } else {
                System.out.printf("File not found in classpath: %s %n", CONFIG_FILENAME);
                System.exit(1);
            }
        } catch (IOException e) {
            System.out.printf("Error occurred while loading properties %s %n", e);
        }
        return properties;
    }
}