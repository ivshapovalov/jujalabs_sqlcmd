package juja.sqlcmd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String PORT = "5432";
    private static final String SERVER = "localhost";
    private static final String TABLE_NAMES_QUERY = "SELECT relname FROM pg_stat_user_tables ORDER BY relname";
    private static final String TABLE_ROWS_COUNT_QUERY = "SELECT count(*) FROM \"?\"";
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
             ResultSet resultSet = statement.executeQuery(TABLE_ROWS_COUNT_QUERY.replace("?", tableName))) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    public DataSet[] getTableData(String tableName) {
        String query = String.format("SELECT * FROM \"%s\"", tableName);
        DataSet[] tableData = new DataSet[0];
        if (connection == null) {
            return tableData;
        }
        if (!isTableExist(tableName)) {
            return tableData;
        }
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (!resultSet.isBeforeFirst()) {
                return tableData;
            }
            int tableRowsCount = getTableRowsCount(tableName);
            tableData = new DataSet[tableRowsCount];
            int columnCount = resultSet.getMetaData().getColumnCount();
            for (int rowIndex = 0; resultSet.next(); rowIndex++) {
                tableData[rowIndex] = new DataSet(columnCount);
                for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                    tableData[rowIndex].add(columnIndex, resultSet.getString(columnIndex + 1));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tableData;
    }

    private boolean isTableExist(String tableName) {
        String query = String.format("SELECT to_regclass('%s')", tableName);
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            resultSet.next();
            return resultSet.getString(1) != null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
