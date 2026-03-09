package acss;

import java.util.ArrayList;

public class Customer extends User {
    public boolean isApproved;
    public ArrayList<Car> purchasedCars = new ArrayList<>();

    // Main constructor with approval status
    public Customer(String username, String password, boolean isApproved) {
        super(username, password);
        this.isApproved = isApproved;
    }

    // METHOD OVERLOADING - Constructor with default approval (false)
    public Customer(String username, String password) {
        this(username, password, false);
    }

    // METHOD OVERLOADING - Constructor with validation
    public Customer(String username, String password, boolean isApproved, boolean validateInput) {
        super(username, password, validateInput);
        this.isApproved = isApproved;
    }

    @Override
    public void viewDashboard() {
        // Placeholder for GUI
    }
}