package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Connection connection;

    // TODO: CHANGE DATABASE URLS USERNAMES AND PASSWORDS
    public static final String DB_NAME = "sql7288442";
    private static String dbAddress = "sql7.freemysqlhosting.net";
    private static int dbPort = 3306;
    private static String dbUsername = "sql7288442";
    private static String dbPassword = "U6KmY4WiER";
    private static String mysqlUrl = "jdbc:mysql://" + dbAddress + ":" + dbPort + "/"+ DB_NAME
            + "?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&autoReconnect=true";

    /**
     * Lazy implementation of the database connection
     *
     * @return connection
     */
    public static Connection getConnection() {
        if (connection != null)
            return connection;
        return getNewConnection();
    }

    /**
     * Creates a new database connection through JDBC driver
     *
     * @return connection
     */
    private static Connection getNewConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(mysqlUrl, dbUsername, dbPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }

    /**
     * This is a helper method used for getting a database name based on the connection url
     *
     * Checks if the connection url is equal to the production or test url and returns name of the database
     *
     * IMPORTANT: This is a 'dirty' version, should be rewrote later
     *
     * @return DB_NAME
     */
    public static String getDbNameFromConnection(Connection connection) {
        String url = "sql7.freemysqlhosting.net";
        try {
            url = connection.getMetaData().getURL();
            System.out.println(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return url.equals(mysqlUrl) ? "sql7288442" : "wine_cellar_schema";
    }
}
