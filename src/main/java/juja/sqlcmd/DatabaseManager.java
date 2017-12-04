package juja.sqlcmd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_CONNECTION_URL = "jdbc:postgresql://127.0.0.1:5432/";

    private Connection connection;

    boolean connect(String database, String user, String password) {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_CONNECTION_URL + database, user, password);
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            return false;
        }
    }

    public String[] getTableNames() throws SQLException {
        String sqlQuery = "SELECT relname FROM pg_stat_user_tables";
        try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            int size = 0;
            if (resultSet.last()) {
                size = resultSet.getRow();
                resultSet.beforeFirst();
            }
            String[] tableNames = new String[size];
            int index = 0;
            while (resultSet.next()) {
                tableNames[index++] = resultSet.getString(1);
            }
            return tableNames;
        }
    }
}
