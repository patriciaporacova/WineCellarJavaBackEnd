package model;

import java.util.ArrayList;
import java.util.List;

public class SensorDataList
{

    private List<SensorData> sensorData;

    public SensorDataList() {
        sensorData = new ArrayList<SensorData>();
    }

    public List<SensorData> getSensorData() {
        return sensorData;
    }
}
