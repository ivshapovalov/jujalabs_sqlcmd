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

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class DatabaseManagerTest {
    private static final String DB_CONNECTION_URL = "jdbc:postgresql://127.0.0.1:5432/";
    private static final String DB_NAME = "sqlcmd";
    private static final String DB_USER = "sqlcmd";
    private static final String DB_USER_PASSWORD = "sqlcmd";
    private static final String TEST_DB_NAME = "testdatabase";

    private static Connection connection;

    private DatabaseManager databaseManager;

    @BeforeClass
    public static void setConnection() throws SQLException {
        connection = DriverManager.getConnection(DB_CONNECTION_URL + DB_NAME, DB_USER, DB_USER_PASSWORD);
        executeSqlQuery("DROP DATABASE IF EXISTS " + TEST_DB_NAME);
        executeSqlQuery("CREATE DATABASE " + TEST_DB_NAME);
        connection.close();
        connection = DriverManager.getConnection(DB_CONNECTION_URL + TEST_DB_NAME, DB_USER, DB_USER_PASSWORD);
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
        connection.close();
        connection = DriverManager.getConnection(DB_CONNECTION_URL + DB_NAME, DB_USER, DB_USER_PASSWORD);
        executeSqlQuery("DROP DATABASE IF EXISTS " + TEST_DB_NAME);
        connection.close();
    }

    @Before
    public void init() throws SQLException {
        dropAllTables();
        databaseManager = new DatabaseManager();
    }

    @After
    public void closeDbManagerConnection() throws SQLException {
        databaseManager.close();
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
        databaseManager.connect(TEST_DB_NAME, DB_USER, DB_USER_PASSWORD);
        String[] expected = new String[]{};
        assertArrayEquals(expected, databaseManager.getTableNames());
    }

    @Test
    public void getTableNamesWhenTwoTablesInDbReturnsTableNamesArray() throws SQLException {
        databaseManager.connect(TEST_DB_NAME, DB_USER, DB_USER_PASSWORD);
        executeSqlQuery("CREATE TABLE table1()");
        executeSqlQuery("CREATE TABLE table2()");
        String[] expected = new String[]{"table1", "table2"};
        assertArrayEquals(expected, databaseManager.getTableNames());
    }

    @Test
    public void getTableDataWhenEmptyTableReturnsEmptyArray() throws SQLException {
        databaseManager.connect(TEST_DB_NAME, DB_USER, DB_USER_PASSWORD);
        executeSqlQuery("CREATE TABLE test_table()");
        DataSet[] expected = new DataSet[]{};
        assertArrayEquals(expected, databaseManager.getTableData("test_table()"));
    }

    @Test
    public void getTableDataWhenWrongTableNameReturnsEmptyArray() throws SQLException {
        databaseManager.connect(TEST_DB_NAME, DB_USER, DB_USER_PASSWORD);
        DataSet[] expected = new DataSet[]{};
        assertArrayEquals(expected, databaseManager.getTableData("WrongTableName"));
    }

    @Test
    public void getTableDataWhenValidDataReturnsTableDataArray() throws SQLException {
        databaseManager.connect(TEST_DB_NAME, DB_USER, DB_USER_PASSWORD);
        executeSqlQuery("CREATE TABLE test_table(" +
                "id INTEGER," +
                "name VARCHAR(128)" +
                ")");
        executeSqlQuery("INSERT INTO test_table VALUES(1,'name1')");
        executeSqlQuery("INSERT INTO test_table VALUES(2,'name2')");
        DataSet row1 = new DataSet(2);
        row1.insertValue(0, "1");
        row1.insertValue(1, "name1");
        DataSet row2 = new DataSet(2);
        row2.insertValue(0, "2");
        row2.insertValue(1, "name2");
        DataSet[] expected = new DataSet[]{row1, row2};
        DataSet[] actual = databaseManager.getTableData("test_table");
        assertThat(actual, arrayContainingInAnyOrder(expected));
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