package juja.sqlcmd;

import java.sql.*;

public class Application {

    private static final String POSTGRESQL_DRIVER = "org.postgresql.Driver";
    private static final String DB_CONNECTION_PATH = "jdbc:postgresql://127.0.0.1:5432/sqlcmd";
    private static final String DB_USER = "sqlcmd";
    private static final String DB_PASSWORD = "sqlcmd";
    private static final String TABLE_NAME = "user";
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private Connection connection;


    public static void main(String[] arg) throws SQLException {
        new Application().simpleSQL();
    }

    public void simpleSQL() throws SQLException {
        checkJDBCDriver();
        createDbConnection();
        dropTableWithName(TABLE_NAME);
        showExistingTablesName();
        createTable(TABLE_NAME);
        for (int i = 1; i < 4; i++) {
            addUser("user" + i, "password" + i);
        }
        showExistingTablesName();
        showExistingRecordsOfTable(TABLE_NAME);
        updateDataByTitle("user2", "userchange1", TABLE_NAME, "name");
        showExistingRecordsOfTable(TABLE_NAME);
        deleteCortegeByTitle(TABLE_NAME, "name", "user3");
        showExistingRecordsOfTable(TABLE_NAME);
        connection.close();
    }

    private void deleteCortegeByTitle(String tableName, String columnName, String title) throws SQLException {
        String sqlQuery = String.format("DELETE FROM \"%1$s\" WHERE %2$s = ? ", tableName, columnName);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, title);
            preparedStatement.executeUpdate();
        }
    }

    private void updateDataByTitle(String oldData, String newData, String tableName, String columnName) throws SQLException {
        String sqlQuery = String.format("UPDATE \"%3$s\" SET %4$s = \'%2$s\' WHERE %4$s = \'%1s\';", oldData, newData, tableName, columnName);
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlQuery);
        }
    }

    private void showExistingRecordsOfTable(String tableName) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            if (tableName != null) {
                String sqlQuery = String.format("SELECT * FROM \"%s\"", tableName);
                ResultSet resultSet = statement.executeQuery(sqlQuery);
                int columnCount = resultSet.getMetaData().getColumnCount();

                StringBuilder tableData = new StringBuilder();
                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        tableData.append(resultSet.getString(i)).append(" | ");
                    }
                    tableData.replace(tableData.length() - 3, tableData.length(), LINE_SEPARATOR);
                }
                System.out.println(tableData.toString());
            }
        }

    }

    private void addUser(String userName, String userPassword) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            if (userName != null && userPassword != null) {
                // Why didn't suit here such expression "INSERT INTO public." + "\"" + TABLE_NAME + "\"" + "(password, name) "
                //                                      + "VALUES (" + userPassword + ", " + userName + ");";
                String sqlQuery = String.format("INSERT INTO \"" + TABLE_NAME + "\"(name, password) VALUES('%s','%s') ", userName, userPassword);
                statement.executeUpdate(sqlQuery);
            } else {
                System.out.println("Wrong input");
            }
        }
    }

    private void dropTableWithName(String user) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sqlQuery = "DROP TABLE IF EXISTS \"" + user + "\" CASCADE ;";
            statement.executeUpdate(sqlQuery);
        }
    }

    private void createTable(String tableName) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sqlQuery = "CREATE TABLE public.\"" + tableName + "\"" + LINE_SEPARATOR
                    + "(" + LINE_SEPARATOR
                    + "id SERIAL PRIMARY KEY ," + LINE_SEPARATOR
                    + "name TEXT  NOT NULL," + LINE_SEPARATOR
                    + "password TEXT  NOT NULL" + LINE_SEPARATOR
                    + ")";
            statement.executeUpdate(sqlQuery);
        }
    }

    private void showExistingTablesName() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sqlQuery = "SELECT" + LINE_SEPARATOR
                    + "*" + LINE_SEPARATOR
                    + "FROM" + LINE_SEPARATOR
                    + "pg_catalog.pg_tables" + LINE_SEPARATOR
                    + "WHERE" + LINE_SEPARATOR
                    + "schemaname != 'pg_catalog'" + LINE_SEPARATOR
                    + "AND schemaname != 'information_schema';";
            ResultSet rs = statement.executeQuery(sqlQuery);
            System.out.println("Existing Tables:");
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    System.out.println("-" + rs.getString("tablename") + LINE_SEPARATOR);
                }
            } else {
                System.out.println("db is empty" + LINE_SEPARATOR);
            }
        }
    }

    private void createDbConnection() {
        try {
            connection = DriverManager.getConnection(DB_CONNECTION_PATH, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        if (connection != null) {
            System.out.println("Connection created" + LINE_SEPARATOR);
        } else {
            System.out.println("Failed to make connection!");
        }
    }

    private void checkJDBCDriver() {
        System.out.println("-------- PostgreSQL "
                + "JDBC Connection  ------------");
        try {
            Class.forName(POSTGRESQL_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
        }
        System.out.println("PostgreSQL JDBC Driver Registered!");
    }

}
