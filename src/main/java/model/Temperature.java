package model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;
import java.sql.Time;

public class Temperature {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date date;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm:ss")
    private Time time;
    private double mesurement;

    public Temperature() {

    }

    public Temperature(Date date, Time time, double mesurement) {
        this.date = date;
        this.time = time;
        this.mesurement = mesurement;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public void setMesurement(double mesurement) {
        this.mesurement = mesurement;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public double getMesurement() {
        return mesurement;
    }

    @Override
    public String toString() {
        return "Temperature{" +
                "date=" + date +
                ", time=" + time +
                ", mesurement=" + mesurement +
                '}';
    }
}
