package acss;

public abstract class User {
    public String username;
    public String password;

    // Main constructor
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // METHOD OVERLOADING - Constructor with validation
    public User(String username, String password, boolean validateInput) {
        if (validateInput && (username == null || username.trim().isEmpty())) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (validateInput && (password == null || password.trim().isEmpty())) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.username = username;
        this.password = password;
    }

    public abstract void viewDashboard();
}