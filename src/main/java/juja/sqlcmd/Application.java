package juja.sqlcmd;

import java.sql.*;

public class Application {
    private Connection connection;

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

        try {
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/sqlcmd", "sqlcmd", "sqlcmd");
            printTableNames();
        } catch (SQLException e) {
            System.out.println("Something goes wrong. Connection failed! Reason: " + e.getMessage());
        } finally {
            closeConnection();
        }
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

    private void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
