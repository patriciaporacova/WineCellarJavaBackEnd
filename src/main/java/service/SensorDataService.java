package service;

import model.SensorData;
import model.SensorDataList;
import utils.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SensorDataService implements ISensorDataService
{
    private static String DB_NAME;
    private final SimpleDateFormat SDF;
    private Connection connection;

    public SensorDataService(Connection dbConnection) {
        this.SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.connection = dbConnection;
        this.DB_NAME = Database.getDbNameFromConnection(connection);
    }

    /**
     * Queries a database and returns an SensorDataList of all orders
     *
     * @return sensorData
     */
    @Override
    public SensorDataList getSensorData() throws SQLException
    {
        SensorDataList sensorData = new SensorDataList();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME + ".sensor_data");

        while (resultSet.next())
        {
            sensorData.getSensorData().add(populateSensorData(resultSet));
        }

        return sensorData;
    }

    /**
     * Initializes an SensorData object from the ResultSet and returns it
     *
     * @param resultSet         resultSet from database query
     * @return sensorData
     */
    private SensorData populateSensorData(ResultSet resultSet) throws SQLException
    {
        Date timeStamp = null;
        String temperature;
        String humidity;

        try {
            timeStamp = SDF.parse(resultSet.getString("time_stamp_column"));
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Couldn't parse SimpleDateFormat object in populateSensorData() method.");
        }

        SensorData sensorData = new SensorData(
                resultSet.getString("ID_data"),
                timeStamp,
                resultSet.getString("temperature"),
                resultSet.getString("humidity")
        );

        return sensorData;
    }

    /*INSERT INTO DATABASE EXAMPLE********************************************
        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO " + DB_NAME + ".tablename VALUES (NULL," + NULL);");
    */

   /*UPDATE DATABASE EXAMPLE**************************************************
        Statement statement = connection.createStatement();
        statement.executeUpdate("UPDATE " + DB_NAME + ".tablename SET pick_up_address= '"
                + "',pick_up_deadline= '" + VALUE+";");
    */

   /*DELETE FROM DATABASE EXAMPLE*********************************************
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM " + DB_NAME + ".tablename WHERE xxxx = '" + VALUE + "';");
   */

}
