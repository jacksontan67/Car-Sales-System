package acss;

import java.io.*;
import java.util.*;

public class DataIO {
    public static ArrayList<Manager> allManagers = new ArrayList<>();
    public static ArrayList<Salesman> allSalesmen = new ArrayList<>();
    public static ArrayList<Customer> allCustomers = new ArrayList<>();
    public static ArrayList<Car> allCars = new ArrayList<>();
    public static ArrayList<Sale> allSales = new ArrayList<>();
    public static ArrayList<Feedback> allFeedbacks = new ArrayList<>();

    public static void writeToFile() {
        try {
            // Write customers
            PrintWriter c = new PrintWriter("customers.txt");
            for (Customer cust : allCustomers) {
                c.println(cust.username);
                c.println(cust.password);
                c.println(cust.isApproved);
                c.println(); // blank line
            }
            c.close();

            // Write managers
            PrintWriter m = new PrintWriter("managers.txt");
            for (Manager man : allManagers) {
                m.println(man.username);
                m.println(man.password);
                m.println(); // blank line
            }
            m.close();

            // Write salesman 
            PrintWriter s = new PrintWriter("salesman.txt");
            for (Salesman sm : allSalesmen) {
                s.println(sm.username);
                s.println(sm.password);
                s.println(); // blank line
            }
            s.close();

            // Write cars
            PrintWriter carOut = new PrintWriter("cars.txt");
            for (Car cObj : allCars) {
                carOut.println(cObj.id);
                carOut.println(cObj.brand);
                carOut.println(cObj.model);
                carOut.println(cObj.price);
                carOut.println(cObj.status);
                carOut.println(); // blank line
            }
            carOut.close();

            // Write sales 
            PrintWriter salesOut = new PrintWriter("sales.txt");
            for (Sale sale : allSales) {
                salesOut.println(sale.car.id);
                salesOut.println(sale.buyer.username);
                salesOut.println(sale.seller.username);
                salesOut.println(sale.price);
                salesOut.println(sale.date);
                salesOut.println(sale.comment);
                salesOut.println(); // blank line
            }
            salesOut.close();

            // Write feedbacks
            PrintWriter feedOut = new PrintWriter("feedbacks.txt");
            for (Feedback feedback : allFeedbacks) {
                feedOut.println(feedback.customerUsername);
                feedOut.println(feedback.carId);
                feedOut.println(feedback.carBrand);
                feedOut.println(feedback.carModel);
                feedOut.println(feedback.rating);
                feedOut.println(feedback.comment);
                feedOut.println(feedback.date);
                feedOut.println(); // blank line
            }
            feedOut.close();

        } catch (Exception e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void readFromFile() {
        // Read customers
        try {
            Scanner c = new Scanner(new File("customers.txt"));
            while (c.hasNext()) {
                String name = c.nextLine();
                if (name.trim().isEmpty()) continue; // Skip empty lines
                String pass = c.nextLine();
                boolean approved = Boolean.parseBoolean(c.nextLine());
                allCustomers.add(new Customer(name, pass, approved));
                if (c.hasNext()) c.nextLine(); // blank line
            }
            c.close();
        } catch (Exception e) {
            System.out.println("No customers.txt found or error reading customers: " + e.getMessage());
        }

        // Read managers
        try {
            Scanner m = new Scanner(new File("managers.txt"));
            while (m.hasNext()) {
                String name = m.nextLine();
                if (name.trim().isEmpty()) continue; // Skip empty lines
                String pass = m.nextLine();
                allManagers.add(new Manager(name, pass));
                if (m.hasNext()) m.nextLine(); // blank line
            }
            m.close();
        } catch (Exception e) {
            System.out.println("No managers.txt found or error reading managers: " + e.getMessage());
        }

        // Read salesman 
        try {
            Scanner s = new Scanner(new File("salesman.txt"));
            while (s.hasNext()) {
                String name = s.nextLine();
                if (name.trim().isEmpty()) continue; // Skip empty lines
                String pass = s.nextLine();
                allSalesmen.add(new Salesman(name, pass));
                if (s.hasNext()) s.nextLine(); // blank line
            }
            s.close();
        } catch (Exception e) {
            System.out.println("No salesman.txt found or error reading salesman: " + e.getMessage());
        }

        // Read cars
        try {
            Scanner sc = new Scanner(new File("cars.txt"));
            while (sc.hasNext()) {
                String id = sc.nextLine();
                if (id.trim().isEmpty()) continue; // Skip empty lines
                String brand = sc.nextLine();
                String model = sc.nextLine();
                double price = Double.parseDouble(sc.nextLine());
                String status = sc.nextLine();
                allCars.add(new Car(id, brand, model, price, status));
                if (sc.hasNext()) sc.nextLine(); // blank line
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("No cars.txt found or error reading cars: " + e.getMessage());
        }

        // Read sales 
        try {
            Scanner salesIn = new Scanner(new File("sales.txt"));
            while (salesIn.hasNext()) {
                String carId = salesIn.nextLine();
                if (carId.trim().isEmpty()) continue; // Skip empty lines
                
                String buyerUsername = salesIn.nextLine();
                String sellerUsername = salesIn.nextLine();
                double price = Double.parseDouble(salesIn.nextLine());
                String date = salesIn.nextLine();
                
                String comment = "No comment"; // Default value for backward compatibility
                
                // Check if there's another line 
                if (salesIn.hasNext()) {
                    String nextLine = salesIn.nextLine();
                    if (!nextLine.trim().isEmpty()) {
                        // This line contains comment data
                        comment = nextLine;
                        // Consume the blank line if it exists
                        if (salesIn.hasNext()) {
                            salesIn.nextLine();
                        }
                    }
                    
                }
                
                // Find the actual objects
                Car car = null;
                for (Car c : allCars) {
                    if (c.id.equals(carId)) {
                        car = c;
                        break;
                    }
                }
                
                Customer buyer = searchCustomer(buyerUsername);
                Salesman seller = null;
                for (Salesman s : allSalesmen) {
                    if (s.username.equals(sellerUsername)) {
                        seller = s;
                        break;
                    }
                }
                
                if (car != null && buyer != null && seller != null) {
                    Sale sale = new Sale(car, buyer, seller, price, date, comment);
                    allSales.add(sale);
                    seller.sales.add(sale);
                    buyer.purchasedCars.add(car);
                }
            }
            salesIn.close();
        } catch (Exception e) {
            System.out.println("No sales.txt found or error reading sales: " + e.getMessage());
        }

        // Read feedbacks
        try {
            Scanner feedIn = new Scanner(new File("feedbacks.txt"));
            while (feedIn.hasNext()) {
                String customerUsername = feedIn.nextLine();
                if (customerUsername.trim().isEmpty()) continue; // Skip empty lines
                
                String carId = feedIn.nextLine();
                String carBrand = feedIn.nextLine();
                String carModel = feedIn.nextLine();
                int rating = Integer.parseInt(feedIn.nextLine());
                String comment = feedIn.nextLine();
                String date = feedIn.nextLine();
                
                allFeedbacks.add(new Feedback(customerUsername, carId, carBrand, carModel, rating, comment, date));
                if (feedIn.hasNext()) feedIn.nextLine(); // blank line
            }
            feedIn.close();
        } catch (Exception e) {
            System.out.println("No feedbacks.txt found or error reading feedbacks: " + e.getMessage());
        }
    }

    public static Customer searchCustomer(String username) {
        for (Customer c : allCustomers) {
            if (c.username.equals(username)) {
                return c;
            }
        }
        return null;
    }

    public static Manager searchManager(String username) {
        for (Manager m : allManagers) {
            if (m.username.equals(username)) {
                return m;
            }
        }
        return null;
    }

    public static Salesman searchSalesman(String username) {
        for (Salesman s : allSalesmen) {
            if (s.username.equals(username)) {
                return s;
            }
        }
        return null;
    }

    public static Car searchCar(String carId) {
        for (Car c : allCars) {
            if (c.id.equals(carId)) {
                return c;
            }
        }
        return null;
    }

    // Method to get sales by salesman
    public static ArrayList<Sale> getSalesBySalesman(String salesmanUsername) {
        ArrayList<Sale> salesmanSales = new ArrayList<>();
        for (Sale sale : allSales) {
            if (sale.seller.username.equals(salesmanUsername)) {
                salesmanSales.add(sale);
            }
        }
        return salesmanSales;
    }

    // Method to get sales by customer
    public static ArrayList<Sale> getSalesByCustomer(String customerUsername) {
        ArrayList<Sale> customerSales = new ArrayList<>();
        for (Sale sale : allSales) {
            if (sale.buyer.username.equals(customerUsername)) {
                customerSales.add(sale);
            }
        }
        return customerSales;
    }

    // Method to validate data integrity
    public static void validateDataIntegrity() {
        System.out.println("=== DATA INTEGRITY CHECK ===");
        System.out.println("Managers: " + allManagers.size());
        System.out.println("Salesmen: " + allSalesmen.size());
        System.out.println("Customers: " + allCustomers.size());
        System.out.println("Cars: " + allCars.size());
        System.out.println("Sales: " + allSales.size());
        System.out.println("Feedbacks: " + allFeedbacks.size());
        
        // Check for sales with missing car/customer/salesman
        int orphanedSales = 0;
        for (Sale sale : allSales) {
            if (searchCar(sale.car.id) == null || 
                searchCustomer(sale.buyer.username) == null || 
                searchSalesman(sale.seller.username) == null) {
                orphanedSales++;
            }
        }
        if (orphanedSales > 0) {
            System.out.println("WARNING: " + orphanedSales + " orphaned sales found!");
        }
        System.out.println("Data integrity check completed.");
    }
}

