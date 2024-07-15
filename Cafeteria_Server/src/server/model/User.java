package server.model;

public class User {
	private String employeeId;
	private String name;
	private String role;
	private String password;

	public User(String employeeId, String name, String role, String password) {
		this.employeeId = employeeId;
		this.name = name;
		this.role = role;
		this.password = password;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public String getName() {
		return name;
	}

	public String getRole() {
		return role;
	}

	public String getPassword() {
		return password;
	}
}
