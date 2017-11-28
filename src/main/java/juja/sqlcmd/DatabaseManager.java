package juja.sqlcmd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private Connection connection;

    private static final String DB_CONNECTION_URL = "jdbc:postgresql://127.0.0.1:5432/";

    boolean connect(String database, String user, String password){
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_CONNECTION_URL+database,user,password);
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            return false;
        }
    }
}
