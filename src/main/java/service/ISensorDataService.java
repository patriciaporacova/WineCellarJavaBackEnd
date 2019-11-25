package service;

import model.SensorDataList;

import java.sql.SQLException;

public interface ISensorDataService
{
    public SensorDataList getSensorData() throws SQLException;


}
