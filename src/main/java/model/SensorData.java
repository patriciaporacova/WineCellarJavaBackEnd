package model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class SensorData
{
    private String dataId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timeStamp;
    private String temperature;
    private String humidity;

    public SensorData() {}

    public SensorData(String dataId, Date timeStamp, String temperature, String humidity)
    {
        this.dataId = dataId;
        this.timeStamp = timeStamp;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public String getDataId()
    {
        return dataId;
    }

    public void setDataId(String dataId)
    {
        this.dataId = dataId;
    }

    public Date getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    public String getTemperature()
    {
        return temperature;
    }

    public void setTemperature(String temperature)
    {
        this.temperature = temperature;
    }

    public String getHumidity()
    {
        return humidity;
    }

    public void setHumidity(String humidity)
    {
        this.humidity = humidity;
    }
}