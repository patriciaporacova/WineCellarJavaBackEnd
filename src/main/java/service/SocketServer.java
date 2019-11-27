package service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    private final int PORT = 4400;
    private ServerSocket serverSocket;

    public SocketServer() throws IOException{
        serverSocket = new ServerSocket(PORT);
    }

    /**
     * Starts a socket server and wait for clients to connect.
     * When a new client connects, starts a new thread to handle the communication
     */
    public void startServer() throws IOException {
        System.out.println("Administrator connection listening on port " + PORT);

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Client accepted ");

            new Thread(new SocketCommunicationHandler(socket)).start();
        }

    }
}
