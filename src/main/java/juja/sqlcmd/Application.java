package juja.sqlcmd;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Application {
    private static final String DB_DRIVER = "org.postgresql.Driver";
    private static final String DB_CONNECTION = "jdbc:postgresql://localhost:5432/sqlcmd";
    private static final String DB_USER = "sqlcmd";
    private static final String DB_PASSWORD = "sqlcmd";

    private Connection connection;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        new Application().simpleSQL();
    }

    public void simpleSQL() throws SQLException, ClassNotFoundException {
        Class.forName(DB_DRIVER);
        connection = DriverManager
                .getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        String sqlQuery = "CREATE TABLE users(id SERIAL, name TEXT, password TEXT)";
        executeSqlQuery(sqlQuery);
        insertUser("user1","password1");
        insertUser("user2","password2");
        insertUser("user3","password3");
        printTableNames();
        connection.close();
    }

    private void insertUser(String name, String password) throws SQLException {
        String sqlQuery = String.format("INSERT INTO users(name, password) VALUES('%s','%s') ", name, password);
        executeSqlQuery(sqlQuery);
    }

    private void executeSqlQuery(String sqlQuery) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(sqlQuery);
    }

    private void printTableNames() throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        String[] types = {"TABLE"};
        ResultSet tables = metaData.getTables(null, null, "%", types);
        while (tables.next()) {
            int tableNameIndex = 3;
            System.out.println(tables.getString(tableNameIndex));
        }
    }
}