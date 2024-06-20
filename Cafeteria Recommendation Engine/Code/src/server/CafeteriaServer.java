package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class CafeteriaServer {
    public static void main(String[] args) {
    	int port = 7777;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Cafeteria Server is listening on port 7777");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                new CafeteriaServerThread(socket).start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
