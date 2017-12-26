package juja.sqlcmd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DB_CONNECTION_URL = "jdbc:postgresql://127.0.0.1:5432/";
    private static final String POSTGRESQL_DRIVER = "org.postgresql.Driver";
    private Connection connection;

    public boolean connect(String databaseName, String user, String password) {
        try {
            Class.forName(POSTGRESQL_DRIVER);
            connection = DriverManager.getConnection(DB_CONNECTION_URL + databaseName, user, password);
            return true;
        } catch (ClassNotFoundException e) {
            System.out.println("Please add JDBC jar to your project");
            return false;
        } catch (SQLException e) {
            System.out.println(String.format("Can not connect to %s with user = %s and password %s", databaseName, user, password));
            return false;
        }
    }

    public String[] tableNames() throws SQLException {
        String sqlQuery = "SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery,
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            int arraySize = numberOfTables(resultSet);
            int index = 0;
            String[] tableNames = new String[arraySize];
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    tableNames[index++] = resultSet.getString(1);
                }
                return tableNames;
            } else {
                return new String[0];
            }
        }
    }

    public boolean close() {
        try {
            connection.close();
            return true;
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            return false;
        }

    }

    private int numberOfTables(ResultSet resultSet) throws SQLException {
        int tableCounter = 0;
        if (resultSet.last()) {
            tableCounter = resultSet.getRow();
            resultSet.beforeFirst();
        }
        return tableCounter;
    }
}
