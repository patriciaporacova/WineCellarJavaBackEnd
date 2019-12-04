package service;

import model.SensorDataList;
import model.Temperature;
import model.TemperatureList;


import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

public interface ISensorDataService
{

    public TemperatureList getTemperatures(Date startDate, Date endDate) throws SQLException, ParseException;



}
