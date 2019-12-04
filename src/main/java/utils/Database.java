package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Connection connection;

    // TODO: CHANGE DATABASE URLS USERNAMES AND PASSWORDS
    public static final String DB_NAME = "SmartCellarWarehouse_SEP4A19G2";
    private static String dbAddress = "10.200.131.2";
    private static int dbPort = 3306;
    private static String dbUsername = "SEP4A19G2";
    private static String dbPassword = "SEP4A19G2";
    private static String connectionUrl = "jdbc:sqlserver://10.200.131.2;database=TestDB_SEP4A19G2;user=SEP4A19G2;password=SEP4A19G2;";
    private static String mysqlUrl = "jdbc:sqlserver://" + dbAddress + ";"+ DB_NAME + ";user="+dbUsername+ ";password="+ dbPassword;
           // + "?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&autoReconnect=true";

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
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            connection = DriverManager.getConnection(connectionUrl);
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
