package acss;

import java.util.ArrayList;

public class Salesman extends User {
    public ArrayList<Sale> sales = new ArrayList<>();

    // Main constructor
    public Salesman(String username, String password) {
        super(username, password);
    }

    // METHOD OVERLOADING - Constructor with validation
    public Salesman(String username, String password, boolean validateInput) {
        super(username, password, validateInput);
    }

    @Override
    public void viewDashboard() {
        // Placeholder for GUI
    }
}