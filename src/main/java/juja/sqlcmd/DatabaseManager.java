package juja.sqlcmd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DB_CONNECTION_URL = "jdbc:postgresql://127.0.0.1:5432/";
    private static final String POSTGRESQL_DRIVER = "org.postgresql.Driver";
    private Connection connection;

    public boolean connect(String databaseName, String user, String password) {
        try {
            Class.forName(POSTGRESQL_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Please add JDBC jar to your project");
            return false;
        }
        try {
            connection = DriverManager.getConnection(DB_CONNECTION_URL + databaseName, user, password);
        } catch (SQLException e) {
            System.out.println(String.format("Can not connect to %s with user = %s and password %s", databaseName, user, password));
            return false;
        }
        return true;
    }
}
