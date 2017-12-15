package juja.sqlcmd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    public String[] getTableNames() throws SQLException {
        String sqlQuery = "SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE); ResultSet resultSet = preparedStatement.executeQuery()) {
            int tableSize = countTable(resultSet);
            int index = 0;
            String[] tableNames = new String[tableSize];
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    tableNames[index++] = resultSet.getString(1);
                }
                return tableNames;
            } else return new String[0];
        }
    }

    private int countTable(ResultSet resultSet) throws SQLException {
        int count = 0;
        while (resultSet.next()) {
            count++;
        }
        resultSet.beforeFirst();
        return count;
    }
}
