package controller;

import model.SensorData;
import model.SensorDataList;
import org.json.JSONObject;
import service.ISensorDataService;
import service.SensorDataService;
import utils.Database;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/api")
@Produces("application/json")
@Consumes("application/json")
public class SensorDataController
{
    private ISensorDataService iSensorDataService;

    public SensorDataController() {
        iSensorDataService = new SensorDataService(Database.getConnection());
    }

    /**
     * Method triggered by GET request on the endpoint "/sensordata"
     *
     * Requests a list of all sensor data which was passed in the url parameter
     * and returns it as a JSON in an HTTP Response.
     *
     * @return dataList
     */
    @GET
    @Path("/sensordata")
    public Response getData() {
        SensorDataList dataList = null;
        try {
            dataList = iSensorDataService.getSensorData();
            return Response.status(200).entity(dataList).build();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(500).entity(e).build();
        }
    }

    /*
    @POST
    @Path("/sensorData")
    public Response addData(SensorData sensorData) {
        try {
            iSensorDataService.addOrder(sensorData);
            return Response.status(200).build();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(500).entity(e).build();
        }
    }*/
    
    /*
    @DELETE
    @Path("/sensorData/{id}")
    public Response delete(@PathParam("id") String value) {
        try {
            iSensorDataService.method(value);
            return Response.status(200).build();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(500).entity(e).build();
        }
    }*/
}
