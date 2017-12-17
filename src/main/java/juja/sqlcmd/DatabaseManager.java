package juja.sqlcmd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DatabaseManager {
    private static final String PORT = "5432";
    private static final String SERVER = "localhost";
    private static final String TABLE_NAMES_QUERY = "SELECT relname FROM pg_stat_user_tables ORDER BY relname";
    private static final String TABLE_ROWS_COUNT_QUERY = "SELECT count(*) FROM ?";
    private static final int VALID_TIMEOUT = 15;

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

    public String[] getTableNames() {
        String[] tableNames = new String[0];
        try {
            if (connection != null && connection.isValid(VALID_TIMEOUT)) {
                try (Statement statement = connection.createStatement();
                     ResultSet resultSet = statement.executeQuery(TABLE_NAMES_QUERY)) {
                    if (resultSet.isBeforeFirst()) {
                        int arraySize = getTableRowsCount("pg_stat_user_tables");
                        tableNames = new String[arraySize];
                        for (int index = 0; resultSet.next(); index++) {
                            tableNames[index] = resultSet.getString(1);
                        }
                    }
                }
            } else {
                throw new SQLException("The connection to the database is not established");
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return tableNames;
    }

    private int getTableRowsCount(String tableName) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(TABLE_ROWS_COUNT_QUERY.replace("?",tableName))) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
