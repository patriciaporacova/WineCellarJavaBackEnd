package service;

import model.SensorData;
import model.SensorDataList;
import model.Temperature;
import model.TemperatureList;
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

/*  try {
    timeStamp = SDF.parse(resultSet.getString("time_stamp_column"));
} catch (ParseException e) {
    e.printStackTrace();
    System.out.println("Couldn't parse SimpleDateFormat object in populateSensorData() method.");
}*/

    @Override
    public TemperatureList getTemperatures(Date startDate, Date endDate) throws SQLException, ParseException {
        TemperatureList temperatureList = new TemperatureList();
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("use SmartCellarWarehouse_SEP4A19G2 "+
                "SELECT"+
                " Dim_Date.MeasuringDate,"+
                " Dim_Time.MeasuringTime,"+
                " mesurement"+
                " FROM Fact_Temperature"+
                " JOIN Dim_Date ON Dim_Date.Date_ID = Fact_Temperature.Date_ID"+
                " JOIN Dim_Time ON Dim_Time.Time_ID = Fact_Temperature.Time_ID"+
                " Where MeasuringDate<='"+ endDate+"' and MeasuringDate>='"
                + startDate + "'"+
                " order by MeasuringDate, MeasuringTime; ");

        while (resultSet.next())
        {
            Temperature temp = new Temperature();
            temp.setDate(resultSet.getDate("MeasuringDate"));
            temp.setTime(resultSet.getTime("MeasuringTime"));
            temp.setMesurement(resultSet.getDouble("mesurement"));
            temperatureList.getTemperatures().add(temp);
        }

        return temperatureList;


    }





}
