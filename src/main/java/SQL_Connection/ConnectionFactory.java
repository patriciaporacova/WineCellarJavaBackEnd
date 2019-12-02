package SQL_Connection;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

   private Connection connection = null;

    public ConnectionFactory() {

    }

    public Connection connect() {
        System.out.println("--------  JDBC Test Connection ------------");

        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("JDBC Driver Registered!");

        } catch (ClassNotFoundException e) {

            System.out.println("JDBC Driver not found");
            e.printStackTrace();

        }


        try {

            String connectionUrl = "jdbc:sqlserver://10.200.131.2;database=TestDB_SEP4A19G2;user=SEP4A19G2;password=SEP4A19G2;";
            connection = DriverManager.getConnection(connectionUrl);


        } catch (SQLException e) {

            System.out.println("Connection Failed!");
            e.printStackTrace();

        }

        if (connection != null) {
            System.out.println("Database connected");

        } else {
            System.out.println("Failed to make connection!");
        }
        return connection;

    }

}
