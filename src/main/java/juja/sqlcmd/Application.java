package juja.sqlcmd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Application {
    public static void main(String[] args) {
        new Application().simpleSQL();
    }

    void simpleSQL() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC driver not found!");
            return;
        }

        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sqlcmd", "sqlcmd", "sqlcmd");
            System.out.println("You've get a connection to sqlcmd");
        } catch (SQLException e) {
            System.out.println("Something goes wrong. Connection failed!");
        } finally {
            closeConnection(connection);
        }
    }

    private void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
