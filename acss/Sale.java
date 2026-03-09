package acss;

public class Sale {
    public Car car;
    public Customer buyer;
    public Salesman seller;
    public double price;
    public String date;
    public String comment;

    // Main constructor with all parameters including comment
    public Sale(Car car, Customer buyer, Salesman seller, double price, String date, String comment) {
        this.car = car;
        this.buyer = buyer;
        this.seller = seller;
        this.price = price;
        this.date = date;
        this.comment = comment != null ? comment : "No comment";
    }
    public Sale(Car car, Customer buyer, Salesman seller, double price, String date) {
        this(car, buyer, seller, price, date, "No comment");
    }
    public Sale(Car car, Customer buyer, Salesman seller, double price) {
        this(car, buyer, seller, price, 
             java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
             "No comment");
    }
}