package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import utils.ConsoleUtils;

public class CafeteriaClient {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 7777;

        try (Socket socket = new Socket(hostname, port);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {
        	
        	System.out.println("Welcome to ITT Cafe App");
            boolean exit = false;

            while (!exit) {
                System.out.println("1. Login");
                System.out.println("2. Exit");

                int choice = ConsoleUtils.getIntInput("Enter your choice: ");
                switch (choice) {
                    case 1:
                    	UserHandler.authenticateUser(scanner, writer, reader);
                        break;
                    case 2:
                        exit = true;
                        System.out.println("Thank you for using Our Cafe App");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter 1 or 2.");
                }
            }
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }   
}
