package juja.sqlcmd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String PORT = "5432";
    private final static String SERVER = "localhost";
    private Connection connection;

    public boolean connect(String database, String user, String password) {
        String dbConnectionUrl = "jdbc:postgresql://" + SERVER + ":" + PORT + "/" + database;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(dbConnectionUrl, user, password);
            return true;
        } catch (ClassNotFoundException e) {
            System.err.println("Driver not found.");
            return false;
        } catch (SQLException e) {
            System.err.println("Connection not established.");
            return false;
        }
    }
}
