package juja.sqlcmd;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DatabaseManagerTest {
    private static final String DB_CONNECTION_URL = "jdbc:postgresql://127.0.0.1:5432/";
    private static final String DB_NAME = "sqlcmd";
    private static final String DB_USER = "sqlcmd";
    private static final String DB_USER_PASSWORD = "sqlcmd";

    private static Connection connection;

    private DatabaseManager databaseManager;

    @BeforeClass
    public static void setConnection() throws SQLException {
        connection = DriverManager.getConnection(DB_CONNECTION_URL + DB_NAME, DB_USER, DB_USER_PASSWORD);
        dropAllTables();
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
        connection.close();
    }

    @Before
    public void init() throws SQLException {
        dropAllTables();
        databaseManager = new DatabaseManager();
        databaseManager.connect(DB_NAME, DB_USER, DB_USER_PASSWORD);
    }

    @Test
    public void connectWhenValidAllParametersReturnsTrue() {
        assertTrue(databaseManager.connect("sqlcmd", "sqlcmd", "sqlcmd"));
    }

    @Test
    public void connectWhenInvalidDatabaseNameReturnsFalse() {
        assertFalse(databaseManager.connect("Notsqlcmd", "sqlcmd", "sqlcmd"));
    }

    @Test
    public void connectWhenInvalidUserNameReturnsFalse() {
        assertFalse(databaseManager.connect("sqlcmd", "Notsqlcmd", "sqlcmd"));
    }

    @Test
    public void connectWhenInvalidUserPasswordReturnsFalse() {
        assertFalse(databaseManager.connect("sqlcmd", "sqlcmd", "Notsqlcmd"));
    }

    @Test
    public void getTableNamesWhenNoTablesInDbReturnsEmptyArray() throws SQLException {
        String[] expected = new String[]{};
        assertArrayEquals(expected, databaseManager.getTableNames());
    }

    @Test
    public void getTableNamesWhenTwoTablesInDbReturnsTableNamesArray() throws SQLException {
        executeSqlQuery("CREATE TABLE table1()");
        executeSqlQuery("CREATE TABLE table2()");
        String[] expected = new String[]{"table1", "table2"};
        String[] actual = databaseManager.getTableNames();
        Arrays.sort(actual);
        assertArrayEquals(expected, actual);
    }

    private static void executeSqlQuery(String sqlQuery) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlQuery);
        }
    }

    private static void dropAllTables() throws SQLException {
        executeSqlQuery("DROP SCHEMA public CASCADE");
        executeSqlQuery("CREATE SCHEMA public");
    }
}