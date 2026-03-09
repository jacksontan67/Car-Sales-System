package acss;

public class Manager extends User {
    
    // Main constructor
    public Manager(String username, String password) {
        super(username, password);
    }

    // METHOD OVERLOADING - Constructor with validation
    public Manager(String username, String password, boolean validateInput) {
        super(username, password, validateInput);
    }

    @Override
    public void viewDashboard() { 
    }
}




