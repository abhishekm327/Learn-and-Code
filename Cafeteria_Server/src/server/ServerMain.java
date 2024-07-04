package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
    public static void main(String[] args) {
    	int port = Integer.parseInt(System.getenv("CAFE_PORT"));
    	
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Cafeteria Server is listening on port " + port);

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
