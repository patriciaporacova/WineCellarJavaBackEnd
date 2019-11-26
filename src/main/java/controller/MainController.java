package controller;

import service.SocketServer;

import java.io.IOException;

public class MainController
{
    
    public static void main(String[] args) {
        try {
            new SocketServer().startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
