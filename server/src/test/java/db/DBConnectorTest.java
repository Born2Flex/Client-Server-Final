package db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.edu.ukma.db.DBConnector;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DBConnectorTest {
    private Properties properties;

    @BeforeEach
    void setup() {
        properties = mock(Properties.class);
        when(properties.getProperty("url")).thenReturn("jdbc:h2:mem:testdb;MODE=PostgreSQL");
        when(properties.getProperty("username")).thenReturn("sa");
        when(properties.getProperty("password")).thenReturn("");
    }

    @Test
    void connectionTest_ConnectionCreated() {
        DBConnector connector = new DBConnector(properties);
        assertDoesNotThrow(() -> {
            Connection connection = connector.getConnection();
            assertNotNull(connection);
        });
    }

    @Test
    void connectionTest_TestCreatedConnection() {
        DBConnector connector = new DBConnector(properties);
        assertDoesNotThrow(() -> {
            Connection connection = connector.getConnection();
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE test (id SERIAL PRIMARY KEY, num INT NOT NULL);");
            connection.createStatement().executeQuery("SELECT * FROM test");
        });
    }

    @Test
    void connectionTest_IncorrectUrl() {
        properties = mock(Properties.class);
        when(properties.getProperty("url")).thenReturn("jdbcabc:h2:mem:testdb");
        when(properties.getProperty("username")).thenReturn("sa");
        when(properties.getProperty("password")).thenReturn("");
        assertThrows(RuntimeException.class, () -> {
            DBConnector connector = new DBConnector(properties);
            connector.getConnection();
        });
    }
}