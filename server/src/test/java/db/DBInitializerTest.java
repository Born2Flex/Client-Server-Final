package db;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.edu.ukma.db.DBInitializer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class DBInitializerTest {
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
    }

    @Test
    void testInitialize() throws Exception {
        DBInitializer dbInitializer = new DBInitializer(connection);
        dbInitializer.initialize();

        var statement = connection.createStatement();
        var resultSet = statement.executeQuery("SHOW TABLES");
        boolean tableExists = false;
        while (resultSet.next()) {
            if ("test".equalsIgnoreCase(resultSet.getString(1))) {
                tableExists = true;
                break;
            }
        }
        assertTrue(tableExists);
    }

    @Test
    void testInitializeScriptRunnerException() {
        ScriptRunner scriptRunner = mock(ScriptRunner.class);
        doThrow(new RuntimeException("Script runner error")).when(scriptRunner).runScript(any(BufferedReader.class));

        DBInitializer dbInitializer = new DBInitializer(connection) {
            @Override
            public void initialize() {
                ClassLoader classLoader = DBInitializer.class.getClassLoader();
                InputStream input = classLoader.getResourceAsStream("ddl.sql");
                scriptRunner.setLogWriter(null);
                scriptRunner.setSendFullScript(true);
                scriptRunner.runScript(new BufferedReader(new InputStreamReader(input)));
            }
        };

        assertThrows(RuntimeException.class, dbInitializer::initialize, "Script runner error");
    }
}
