package juja.sqlcmd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Application {

    private final static String SERVER = "localhost";
    private final static String PORT = "5432";
    private final static String DB_NAME = "sqlcmd";
    private final static String USER_NAME = "sqlcmd";
    private final static String PASSWORD = "sqlcmd";
    private Connection connection;
    private String[] users = {"user1|password1", "user2|password2", "user3|password3"};

    public static void main(String[] args) {
        try {
            new Application().simpleSQL();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void simpleSQL() throws ClassNotFoundException, SQLException {
        connection = getConnection();
        deleteTable("user");
        System.out.println(getTablesName());
        createTable("user");
        System.out.println(getTablesName());
        addUsers(users);
        showTableData("user");
        changeUserName("user2", "userсhange1");
        showTableData("user");
        deleteUser("user3");
        showTableData("user");
        connection.close();
    }

    /**
     * Method deletes the user with name "userName" from the table "user".
     *
     * @param userName the username you want to delete.
     * @throws SQLException if a database access error occurs
     */
    private void deleteUser(String userName) throws SQLException {
        if (userName != null) {
            try (Statement statement = connection.createStatement()) {
                String query = String.format("DELETE FROM \"user\" WHERE name = '%s'", userName);
                statement.executeUpdate(query);
            }
        }
    }

    /**
     * Method changes the username in the "name" field in the "user" table.
     *
     * @param oldName old user name. It will be overwritten with the new value.
     * @param newName new user name value.
     * @throws SQLException if a database access error occurs
     */
    private void changeUserName(String oldName, String newName) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String query = String.format("UPDATE \"user\" SET name = '%s' WHERE name = '%s'", newName, oldName);
            statement.executeUpdate(query);
        }
    }

    /**
     * The method prints the table data to the console in the format Field1 | Field2 | ... | FieldN
     *
     * @param tableName the name of the table whose data should be output to the console.
     * @throws SQLException if a database access error occurs
     */
    private void showTableData(String tableName) throws SQLException {
        if (tableName != null) {
            String tableData = getTableData(tableName);
            System.out.println(tableData);
        } else {
            System.err.println("Can't display data. Table Name is Null");
        }
    }

    /**
     * The method returns all data from the table.
     *
     * @param tableName the name of the table whose data should be returned
     * @return all data from the table in the format Field1 | Field2 | ... | FieldN
     * @throws SQLException if a database access error occurs
     */
    private String getTableData(String tableName) throws SQLException {
        String query = "SELECT * FROM \"" + tableName + "\"";
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            int columnCount = resultSet.getMetaData().getColumnCount();
            StringBuilder result = new StringBuilder();
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    result.append(resultSet.getString(i)).append(" | ");
                }
                result.replace(result.length() - 3, result.length(), System.lineSeparator());
            }
            return result.toString();
        }
    }

    /**
     * Method adds users to the table "user".
     *
     * @param users array of users and their passwords.
     *              One element of array it is one user and his password.
     *              One element of array has format username|password.
     * @throws SQLException if a database access error occurs
     */
    private void addUsers(String[] users) throws SQLException {
        if (users == null) {
            System.err.println("List of users is null.");
        } else {
            for (String oneUser : users) {
                if (oneUser != null) {
                    addOneUser(oneUser.split("\\|"));
                } else {
                    System.err.println("Can't add some user. Name and password are Null");
                }
            }
        }

    }

    /**
     * Method adds one user in the table "user".
     *
     * @param oneUserNameAndPassword array with two element.
     *                               Element with index 0 it is the userneme.
     *                               Element with index 1 it is the password.
     * @throws SQLException if a database access error occurs
     */
    private void addOneUser(String[] oneUserNameAndPassword) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String query = String.format("INSERT INTO \"user\" (name, password) VALUES ('%s', '%s')",
                    oneUserNameAndPassword[0], oneUserNameAndPassword[1]);
            statement.executeUpdate(query);
        }
    }

    /**
     * Method creates the new table.
     *
     * @param tableName the name of the table that the method creates.
     * @throws SQLException if a database access error occurs
     */
    private void createTable(String tableName) throws SQLException {
        if (!isTableExist(tableName)) {
            String sql = "CREATE TABLE \"" + tableName + "\"" +
                    "(id SERIAL PRIMARY KEY," +
                    " name TEXT NOT NULL," +
                    " password TEXT NOT NULL)";
            try (Statement statement = connection.createStatement()) {
                statement.execute(sql);
            }
        } else {
            System.err.println("Table is already exist. ");
        }
    }

    /**
     * The method checks if the specified table exists in the database.
     *
     * @param tableName name of the table.
     * @return true if table exist, else false
     * @throws SQLException if a database access error occurs
     */
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
     * The method prints to the console the names of all tables created in the database.
     *
     * @return the list of all tables in the database. Each following table with a new row.
     * @throws SQLException if a database access error occurs
     */
    private String getTablesName() throws SQLException {
        String result = "";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT relname FROM pg_stat_user_tables")) {
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    result += resultSet.getString(1) + System.lineSeparator();
                }
            } else {
                result = "db is empty.";
            }
        }
        return result;
    }

    /**
     * Establishes a connection to the server
     *
     * @return a connection to the URL
     * @throws ClassNotFoundException if the class cannot be located
     * @throws SQLException           if a database access error occurs
     */
    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        String dbConnectionUrl = "jdbc:postgresql://" + SERVER + ":" + PORT + "/" + DB_NAME;
        return DriverManager.getConnection(dbConnectionUrl, USER_NAME, PASSWORD);
    }

    /**
     * Method removes the table from database.
     *
     * @param tableName name of the table
     * @throws SQLException if a database access error occurs
     */
    private void deleteTable(String tableName) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String query = "DROP TABLE IF EXISTS \"" + tableName + "\"";
            statement.executeUpdate(query);
        }
    }
}
