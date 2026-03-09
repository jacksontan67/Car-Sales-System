package acss;

public class Feedback {
    public String customerUsername;
    public String carId;
    public String carBrand;
    public String carModel;
    public int rating; // 1-5 stars
    public String comment;
    public String date;
    
    // Main constructor with all parameters
    public Feedback(String customerUsername, String carId, String carBrand, String carModel, int rating, String comment, String date) {
        this.customerUsername = customerUsername;
        this.carId = carId;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
    }

    // METHOD OVERLOADING - Constructor without date 
    public Feedback(String customerUsername, String carId, String carBrand, String carModel, int rating, String comment) {
        this(customerUsername, carId, carBrand, carModel, rating, comment, 
             java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    // METHOD OVERLOADING - Constructor without comment
    public Feedback(String customerUsername, String carId, String carBrand, String carModel, int rating) {
        this(customerUsername, carId, carBrand, carModel, rating, "No comment provided");
    }
}