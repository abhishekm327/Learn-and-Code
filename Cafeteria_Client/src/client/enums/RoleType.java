package client.enums;

public enum RoleType {
	ADMIN, CHEF, EMPLOYEE;

	public static RoleType fromString(String role) {
		try {
			return RoleType.valueOf(role.toUpperCase());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
}
