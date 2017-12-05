package juja.sqlcmd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String PORT = "5432";
    private static final String SERVER = "localhost";
    private Connection connection;

    public boolean connect(String database, String user, String password) {
        String dbConnectionUrl = "jdbc:postgresql://" + SERVER + ":" + PORT + "/" + database;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(dbConnectionUrl, user, password);
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            return false;
        }
    }
}
