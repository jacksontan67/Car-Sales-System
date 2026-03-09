package acss;

public class Car implements Searchable {
    public String id;
    public String brand;
    public String model;
    public double price;
    public String status; // available, booked, sold

    // Main constructor with all parameters
    public Car(String id, String brand, String model, double price, String status) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.status = status;
    }

    // METHOD OVERLOADING - Constructor with default status
    public Car(String id, String brand, String model, double price) {
        this(id, brand, model, price, "available");
    }

    // METHOD OVERLOADING - Constructor for basic car info
    public Car(String id, String brand, String model) {
        this(id, brand, model, 0.0, "available");
    }

    // INTERFACE IMPLEMENTATION - Searchable interface method
    @Override
    public boolean matches(String criteria) {
        if (criteria == null || criteria.trim().isEmpty()) {
            return false;
        }
        String searchTerm = criteria.toLowerCase().trim();
        return id.toLowerCase().contains(searchTerm) ||
               brand.toLowerCase().contains(searchTerm) ||
               model.toLowerCase().contains(searchTerm) ||
               status.toLowerCase().contains(searchTerm);
    }

    @Override
    public String getSearchInfo() {
        return String.format("ID: %s | %s %s | RM%.2f | Status: %s", 
                           id, brand, model, price, status.toUpperCase());
    }
}