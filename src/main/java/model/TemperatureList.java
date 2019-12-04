package model;

import java.util.ArrayList;
import java.util.List;

public class TemperatureList {

    private List<Temperature> temperatures;

    public TemperatureList() {
        temperatures = new ArrayList<Temperature>();
    }

    public List<Temperature> getTemperatures() {
        return temperatures;
    }
}
