package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.*;
import org.json.JSONObject;
import utils.Database;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class SocketCommunicationHandler implements Runnable {

    private Socket socket;
    private ISensorDataService iSensorDataService;
    private InputStream inputStream;
    private OutputStream outputStream;
    private SocketRequest request;
    private final String SUCCESS = "success";

    public SocketCommunicationHandler(Socket socket) {
        this.socket = socket;
        this.iSensorDataService = new SensorDataService(Database.getConnection());
    }

    /**
     * Initializes input and output stream for the socket communication and waits for the request.
     * When request arrives, identifies an action and perform necessary steps to create and send a response.
     *
     * Action is defined in the ACTION field contained in the request. Each action has it's own separate
     * set of commands that will be executed in order to send a response back.
     */
    @Override
    public void run() {
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            String json = read();
            JSONObject jsonObject = new JSONObject(json);
            System.out.println(json);
            request = new SocketRequest(
                    jsonObject.getEnum(SocketRequest.ACTION.class, "action"),
                    jsonObject.get("obj").equals(null)  ? null : jsonObject.getJSONObject("obj")
            );
            System.out.println(request.getObj());
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (request.getAction()) {
            case GET_SENSOR_DATA:
                try {
                    SensorDataList sensorDataList = iSensorDataService.getSensorData();
//                  JSONObject response = new JSONObject(orders);
                    String response = new ObjectMapper().writeValueAsString(sensorDataList);
                    send(response);
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
                break;
                
        }
    }

    /**
     * Reads the request from the sockets and transforms bytes into json
     */
    private String read() throws IOException {
        //translating input
        byte[] lenBytes = new byte[4];
        inputStream.read(lenBytes, 0, 4);
        int len = (((lenBytes[3] & 0xff) << 24) | ((lenBytes[2] & 0xff) << 16) |
                ((lenBytes[1] & 0xff) << 8) | (lenBytes[0] & 0xff));
        byte[] receivedBytes = new byte[len];
        inputStream.read(receivedBytes, 0, len);

        String json = new String(receivedBytes, 0, len);

        return json;
    }

    /**
     * Writes json response into bytes and sends it back to the receiver
     */
    private void send(String toSend) throws IOException {
        System.out.println(toSend);
        byte[] toSendBytes = toSend.getBytes();
        int toSendLen = toSendBytes.length;
        byte[] toSendLenBytes = new byte[4];
        toSendLenBytes[0] = (byte) (toSendLen & 0xff);
        toSendLenBytes[1] = (byte) ((toSendLen >> 8) & 0xff);
        toSendLenBytes[2] = (byte) ((toSendLen >> 16) & 0xff);
        toSendLenBytes[3] = (byte) ((toSendLen >> 24) & 0xff);
        outputStream.write(toSendLenBytes);
        outputStream.write(toSendBytes);
    }
}
