package properties;

import org.junit.jupiter.api.Test;
import ua.edu.ukma.properties.PropertiesLoader;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesLoaderTest {
    @Test
    void testLoadProperties() {
        PropertiesLoader propertiesLoader = new PropertiesLoader();
        Properties properties = propertiesLoader.loadProperties();

        assertNotNull(properties);
        assertEquals("value1", properties.getProperty("property1"));
        assertEquals("value2", properties.getProperty("property2"));
    }
}