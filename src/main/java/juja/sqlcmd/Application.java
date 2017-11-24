package juja.sqlcmd;

import java.sql.*;

public class Application {


    private final static String SERVER = "localhost";
    private final static String PORT = "5432";
    private final static String DB_NAME = "sqlcmd";
    private final static String USER_NAME = "sqlcmd";
    private final static String PASSWORD = "sqlcmd";
    private Connection connection;

    public static void main(String[] args) {
        try {
            new Application().simpleSQL();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void simpleSQL() throws ClassNotFoundException, SQLException {
        connection = getConnection();
        System.out.println(getTablesName());
        createTable("user");
        System.out.println(getTablesName());



    }

    private void createTable(String tableName) throws SQLException {
        if (isCorrectTableName(tableName)) {
            System.out.println("Wrong table name.");
            return;
        }
        if (isTableExist(tableName)) {
            System.out.println("Table is already exist.");
            return;
        }
        String sql = "CREATE TABLE \"" + tableName + "\"" +
                "(id SERIAL PRIMARY KEY," +
                " name TEXT NOT NULL," +
                " password TEXT NOT NULL)";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    private boolean isTableExist(String tableName) throws SQLException {
        String query = "SELECT EXISTS ( SELECT 1" +
                " FROM information_schema.tables WHERE  table_name = '" + tableName + "')";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            resultSet.next();
            return resultSet.getBoolean(1);
        }
    }


    /**
     * The method checks if the table name is entered correctly
     *
     * @param tableName Name of the table
     * @return true if first char of string is digital, else false
     */
    private boolean isCorrectTableName(String tableName) {
        return Character.isDigit(tableName.charAt(0));
    }

    /**
     * @return //todo write comment
     * @throws SQLException //todo write comment
     */

    private String getTablesName() throws SQLException {
        String result = "";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT relname FROM pg_stat_user_tables");
            if (!resultSet.isBeforeFirst()) {
                return "db is empty.";
            }
            while (resultSet.next()) {
                result += resultSet.getString(1) + "\n";
            }
        }
        return result;
    }

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        String dbConnectionLine = "jdbc:postgresql://" + SERVER + ":" + PORT + "/" + DB_NAME;
        return DriverManager.getConnection(dbConnectionLine, USER_NAME, PASSWORD);
    }
}
