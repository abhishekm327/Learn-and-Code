package server;

import java.io.*;
import java.net.Socket;
import org.json.JSONObject;
import server.controller.ServerUserController;

public class CafeteriaServerThread extends Thread {
	private final Socket socket;
	private final ServerUserController serverUserController = new ServerUserController();

	public CafeteriaServerThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

			String request;
			while ((request = reader.readLine()) != null) {
				JSONObject jsonRequest = new JSONObject(request);
				JSONObject jsonResponse = serverUserController.handleRequest(jsonRequest);
				writer.println(jsonResponse.toString());
			}
		} catch (IOException ex) {
			System.out.println("An error occurred processing client request");
		}
	}
}
