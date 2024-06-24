package ua.edu.ukma.db;

import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;

public class DBInitializer {
    private static final String DDL_FILENAME = "ddl.sql";
    private final Connection connection;

    public DBInitializer(Connection connection) {
        this.connection = connection;
    }

    public void initialize() {
        ClassLoader classLoader = DBInitializer.class.getClassLoader();
        InputStream input = classLoader.getResourceAsStream(DDL_FILENAME);
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        scriptRunner.setLogWriter(null);
        scriptRunner.setSendFullScript(true);
        scriptRunner.runScript(new BufferedReader(new InputStreamReader(input)));
    }
}