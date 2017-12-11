package juja.sqlcmd;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class DatabaseManagerTest {
    private static final String DB_NAME = "sqlcmd";
    private static final String USER = "sqlcmd";
    private static final String PASSWORD = "sqlcmd";
    private static final String TEST_DB_CONNECTION_URL = "jdbc:postgresql://localhost:5432/";
    private DatabaseManager databaseManager;
    private String dbName;


    @Before
    public void setup() {
        databaseManager = new DatabaseManager();
    }

    @Test
    public void testConnectionWithCorrectParameters() {
        assertTrue(databaseManager.connect(DB_NAME, USER, PASSWORD));
    }

    @Test
    public void testConnectionIfDatabaseNotExists() {
        assertFalse(databaseManager.connect("noDatabase", USER, PASSWORD));
    }

    @Test
    public void testConnectionIfWrongUser() {
        assertFalse(databaseManager.connect(DB_NAME, "wrongUser", PASSWORD));
    }

    @Test
    public void testConnectionIfWrongPassword() {
        assertFalse(databaseManager.connect(DB_NAME, USER, "wrongPassword"));
    }

    @Test
    public void testGetTablesNameWhenDbHasTwoTables() throws SQLException, ClassNotFoundException {
        preparationEnvironment();
        createTwoTablesInDatabase(dbName);
        databaseManager.connect(dbName, USER, PASSWORD);
        assertArrayEquals(new String[]{"first_table", "second_table"}, databaseManager.getTableNames());
        databaseManager.closeConnection();
        dropEnvironment();
    }

    @Test
    public void testGetTablesNameWhenDbHasNotTables() throws SQLException, ClassNotFoundException {
        preparationEnvironment();
        databaseManager.connect(dbName, USER, PASSWORD);
        assertArrayEquals(new String[]{}, databaseManager.getTableNames());
        databaseManager.closeConnection();
        dropEnvironment();
    }

    @Test
    public void testGetTablesNameWhenNoConnection() throws SQLException, ClassNotFoundException {
        preparationEnvironment();
        assertArrayEquals(new String[]{}, databaseManager.getTableNames());
        dropEnvironment();
    }

    @After
    public void dropEnvironment() throws SQLException, ClassNotFoundException {
        Connection testConnection = getTestConnection();
        try (Statement statement = testConnection.createStatement()) {
            statement.execute(String.format("DROP DATABASE IF EXISTS \"%s\" ", this.dbName));
            testConnection.close();
        }
    }

    private void createTwoTablesInDatabase(String dbName) throws SQLException, ClassNotFoundException {
        Connection testConnection = getTestConnection(dbName);
        try (Statement statement = testConnection.createStatement()) {
            statement.execute("CREATE TABLE first_table (id SERIAL PRIMARY KEY)");
            statement.execute("CREATE TABLE second_table (id SERIAL PRIMARY KEY)");
            testConnection.close();
        }
    }

    private void preparationEnvironment() throws ClassNotFoundException, SQLException {
        this.dbName = new Date().toString();
        Connection testConnection = getTestConnection();
        try (Statement statement = testConnection.createStatement()) {
            statement.execute(String.format("CREATE DATABASE \"%s\" ", this.dbName));
            testConnection.close();
        }

    }

    private Connection getTestConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(TEST_DB_CONNECTION_URL, USER, PASSWORD);
    }

    private Connection getTestConnection(String dbName) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(TEST_DB_CONNECTION_URL + dbName, USER, PASSWORD);
    }

}
