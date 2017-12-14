package juja.sqlcmd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Application {
    private static final String DB_DRIVER = "org.postgresql.Driver";
    private static final String DB_CONNECTION_URL = "jdbc:postgresql://localhost:5432/sqlcmd";
    private static final String DB_USER = "sqlcmd";
    private static final String DB_PASSWORD = "sqlcmd";

    private Connection connection;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        new Application().simpleSQL();
    }

    public void simpleSQL() throws SQLException, ClassNotFoundException {
        Class.forName(DB_DRIVER);
        connection = DriverManager
                .getConnection(DB_CONNECTION_URL, DB_USER, DB_PASSWORD);
        printTableNames();
        String sqlQuery = "CREATE TABLE \"user\"(id SERIAL PRIMARY KEY, name TEXT, password TEXT)";
        executeSqlQuery(sqlQuery);
        printTableNames();
        insertUser("user1", "password1");
        insertUser("user2", "password2");
        insertUser("user3", "password3");
        printTable("\"user\"");
        updateUser("name", "user2", "userсhange1");
        printTable("\"user\"");
        deleteUser("name", "user3");
        printTable("\"user\"");
        connection.close();
    }

    private void insertUser(String name, String password) throws SQLException {
        String sqlQuery = String.format("INSERT INTO \"user\"(name, password) VALUES('%s','%s') ", name, password);
        executeSqlQuery(sqlQuery);
    }

    private void updateUser(String colName, String oldValue, String newValue) throws SQLException {
        String sqlQuery = String.format("UPDATE \"user\" SET %1$s='%3$s' WHERE %1$s='%2$s'", colName, oldValue, newValue);
        executeSqlQuery(sqlQuery);
    }

    private void deleteUser(String colName, String value) throws SQLException {
        String sqlQuery = String.format("DELETE FROM \"user\" WHERE %s='%s'", colName, value);
        executeSqlQuery(sqlQuery);
    }

    private void executeSqlQuery(String sqlQuery) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlQuery);
        }
    }

    private void printTable(String tableName) throws SQLException {
        String sqlQuery = "SELECT * FROM " + tableName;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            int colCount = resultSet.getMetaData().getColumnCount();
            StringBuilder result = new StringBuilder();
            while (resultSet.next()) {
                for (int i = 1; i <= colCount; i++) {
                    result.append(" ").append(resultSet.getString(i)).append(" |");
                }
                result.replace(result.lastIndexOf(" |"), result.length(), System.lineSeparator());
            }
            System.out.println(result.toString());
        }
    }

    private void printTableNames() throws SQLException {
        StringBuilder result = new StringBuilder();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT relname FROM pg_stat_user_tables");
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    result.append(resultSet.getString(1)).append(System.lineSeparator());
                }
            }else{
                result.append("db is empty");
            }
        }
        System.out.println(result);
    }
}