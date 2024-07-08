package miniProject.board.auth.constants;

public enum Role {
    ROLE_USER, ROLE_ADMIN;

    public static Role fromString(String role) {
        try {
            return Role.valueOf(role);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + role);
        }
    }
}
