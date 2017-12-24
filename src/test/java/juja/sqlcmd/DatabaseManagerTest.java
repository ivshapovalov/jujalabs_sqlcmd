package juja.sqlcmd;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DatabaseManagerTest {

    private static final String DB_NAME = "sqlcmd";
    private static final String TEST_DB_NAME = "test";
    private static final String DB_USER_NAME = "sqlcmd";
    private static final String DB_USER_PASSWORD = "sqlcmd";
    private static final String JDBC_URL = "jdbc:postgresql://127.0.0.1:5432/";
    private static Connection connection;

    private DatabaseManager databaseManager;

    @BeforeClass
    public static void setTestingEnvironment() throws SQLException {
        connection = DriverManager.getConnection(
                JDBC_URL, DB_USER_NAME, DB_USER_PASSWORD);
        executeSqlQuery("CREATE DATABASE " + TEST_DB_NAME);
        connection.close();
        connection = DriverManager.getConnection(
                JDBC_URL + TEST_DB_NAME, DB_USER_NAME, DB_USER_PASSWORD);
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
        connection.close();
        connection = DriverManager.getConnection(JDBC_URL + DB_NAME, DB_USER_NAME, DB_USER_PASSWORD);
        executeSqlQuery("DROP DATABASE IF EXISTS " + TEST_DB_NAME);
        connection.close();
    }

    @Before
    public void setUp() throws SQLException {
        databaseManager = new DatabaseManager();
        dropAllTables();
    }

    @After
    public void closeTestConnection() {
        databaseManager.close();
    }

    private static void executeSqlQuery(String sqlQuery) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlQuery);
        }
    }

    private static void dropAllTables() throws SQLException {
        String sqlQuery = "DROP SCHEMA public CASCADE; CREATE SCHEMA public;";
        executeSqlQuery(sqlQuery);
    }

    @Test
    public void connectWithValidParametersShouldReturnTrue() {
        assertTrue(databaseManager.connect(TEST_DB_NAME, DB_USER_NAME, DB_USER_PASSWORD));
    }

    @Test
    public void connectWithWrongDatabaseNameShouldReturnFalse() {
        assertFalse(databaseManager.connect("wrongName", DB_USER_NAME, DB_USER_PASSWORD));
    }

    @Test
    public void connectWithWrongUserNameShouldReturnFalse() {
        assertFalse(databaseManager.connect(TEST_DB_NAME, "wrongUserName", DB_USER_PASSWORD));
    }

    @Test
    public void connectWithWrongPasswordShouldReturnFalse() {
        assertFalse(databaseManager.connect(TEST_DB_NAME, DB_USER_NAME, "wrongPassword"));
    }

    @Test
    public void getTableNamesWhenNoTableInDatabaseShouldReturnEmptyArray() throws SQLException {
        String[] expected = new String[0];
        databaseManager.connect(TEST_DB_NAME, DB_USER_NAME, DB_USER_PASSWORD);
        assertArrayEquals("We Expected:", expected, databaseManager.TableNames());
    }

    @Test
    public void getTableNamesWhenTwoTablesInDatabaseShouldReturnTableNamesArray() throws SQLException {
        executeSqlQuery("CREATE TABLE public.test1()");
        executeSqlQuery("CREATE TABLE public.test2()");
        databaseManager.connect(TEST_DB_NAME, DB_USER_NAME, DB_USER_PASSWORD);
        String[] expected = new String[]{"test1", "test2"};
        assertArrayEquals(expected, databaseManager.TableNames());
    }
}