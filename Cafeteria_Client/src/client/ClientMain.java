package client;

import java.io.*;
import java.net.Socket;
import client.controller.ClientUserController;
import client.utils.ConsoleUtils;

public class ClientMain {
	public static void main(String[] args) throws IOException {
		String hostname = System.getenv("CAFE_HOSTNAME");
		int port = Integer.parseInt(System.getenv("CAFE_PORT"));

		try (Socket socket = new Socket(hostname, port);
				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {

			System.out.println("Welcome to ITT Cafe App");
			boolean exit = false;

			while (!exit) {
				System.out.println("1. Login");
				System.out.println("2. Exit");

				int choice = ConsoleUtils.getIntInput("Enter your choice: ");
				switch (choice) {
				case 1:
					ClientUserController.authenticateUser(writer, reader);
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
			System.out.println(
					"An unexpected error occurred while trying to communicate with the server. Please try again later.");
		}
	}
}
