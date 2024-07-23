package server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	private static final String URL = System.getenv("CAFE_DB_URL");
	private static final String USER = System.getenv("CAFE_DB_USER");
	private static final String PASSWORD = System.getenv("CAFE_DB_PASSWORD");

	private static DatabaseConnection dbInstance;
	private Connection connection;

	private DatabaseConnection() throws SQLException {
		try {
			this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException exception) {
			throw new SQLException("Failed to create a database connection. Check the connection & Try Again");
		}
	}

	public static DatabaseConnection getInstance() throws SQLException {
		if (dbInstance == null) {
			dbInstance = new DatabaseConnection();
		} else if (dbInstance.getConnection().isClosed()) {
			dbInstance = new DatabaseConnection();
		}
		return dbInstance;
	}

	public Connection getConnection() {
		return connection;
	}

	public void closeConnection() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
		}
	}
}
