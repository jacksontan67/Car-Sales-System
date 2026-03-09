package acss;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Page4CustomerViewCars implements ActionListener {
    JFrame x;
    Button viewAllAvailable, searchByBrand, searchByPriceRange, viewCarDetails, back;

    public Page4CustomerViewCars() {
        x = new JFrame("Available Cars");
        x.setSize(400, 180);
        x.setLocation(600, 300);
        x.setLayout(new FlowLayout());

        viewAllAvailable = new Button("View All Available");
        searchByBrand = new Button("Search by Brand");
        searchByPriceRange = new Button("Search by Price");
        viewCarDetails = new Button("View Car Details");
        back = new Button("Back");

        viewAllAvailable.addActionListener(this);
        searchByBrand.addActionListener(this);
        searchByPriceRange.addActionListener(this);
        viewCarDetails.addActionListener(this);
        back.addActionListener(this);

        x.add(viewAllAvailable);
        x.add(searchByBrand);
        x.add(searchByPriceRange);
        x.add(viewCarDetails);
        x.add(back);
        x.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Customer currentCustomer = (Customer) ACSS.currentUser;
            
            // Check if customer is approved
            if (!currentCustomer.isApproved) {
                JOptionPane.showMessageDialog(null, "Your account is not approved yet. Please wait for manager approval to browse cars.");
                return;
            }

            if (e.getSource() == viewAllAvailable) {
                // Show only available cars
                StringBuilder availableCars = new StringBuilder("=== AVAILABLE CARS ===\n");
                boolean foundAvailable = false;
                
                for (Car c : DataIO.allCars) {
                    if (c.status.equalsIgnoreCase("available")) {
                        availableCars.append("ID: ").append(c.id)
                                    .append(" | Brand: ").append(c.brand)
                                    .append(" | Model: ").append(c.model)
                                    .append(" | Price: RM").append(c.price).append("\n");
                        foundAvailable = true;
                    }
                }
                
                if (!foundAvailable) {
                    availableCars.append("No cars are currently available for purchase.");
                } else {
                    availableCars.append("\nNote: Use 'View Car Details' for more information about specific cars.");
                }
                
                JTextArea textArea = new JTextArea(availableCars.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 300));
                JOptionPane.showMessageDialog(null, scrollPane, "Available Cars", JOptionPane.INFORMATION_MESSAGE);

            } else if (e.getSource() == searchByBrand) {
                String brandToSearch = JOptionPane.showInputDialog("Enter brand name to search:");
                if (brandToSearch == null || brandToSearch.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Brand name cannot be empty!");
                    return;
                }
                
                StringBuilder brandResults = new StringBuilder("=== AVAILABLE " + brandToSearch.toUpperCase() + " CARS ===\n");
                boolean found = false;
                
                for (Car c : DataIO.allCars) {
                    if (c.brand.equalsIgnoreCase(brandToSearch.trim()) && c.status.equalsIgnoreCase("available")) {
                        brandResults.append("ID: ").append(c.id)
                                   .append(" | Model: ").append(c.model)
                                   .append(" | Price: RM").append(c.price).append("\n");
                        found = true;
                    }
                }
                
                if (!found) {
                    brandResults.append("No available cars found for brand: ").append(brandToSearch);
                } else {
                    brandResults.append("\nAll cars shown are available for purchase.");
                }
                
                JTextArea textArea = new JTextArea(brandResults.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(450, 250));
                JOptionPane.showMessageDialog(null, scrollPane, "Brand Search Results", JOptionPane.INFORMATION_MESSAGE);

            } else if (e.getSource() == searchByPriceRange) {
                String minPriceStr = JOptionPane.showInputDialog("Enter minimum price (RM):");
                if (minPriceStr == null || minPriceStr.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Minimum price cannot be empty!");
                    return;
                }

                String maxPriceStr = JOptionPane.showInputDialog("Enter maximum price (RM):");
                if (maxPriceStr == null || maxPriceStr.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Maximum price cannot be empty!");
                    return;
                }

                double minPrice, maxPrice;
                try {
                    minPrice = Double.parseDouble(minPriceStr.trim());
                    maxPrice = Double.parseDouble(maxPriceStr.trim());
                    
                    if (minPrice < 0) {
                        JOptionPane.showMessageDialog(null, "Minimum price cannot be negative!");
                        return;
                    }
                    if (maxPrice < 0) {
                        JOptionPane.showMessageDialog(null, "Maximum price cannot be negative!");
                        return;
                    }
                    if (minPrice > maxPrice) {
                        JOptionPane.showMessageDialog(null, "Minimum price cannot be greater than maximum price!");
                        return;
                    }
                    if (maxPrice > 10000000) { // 10 million limit
                        JOptionPane.showMessageDialog(null, "Maximum price seems unrealistic. Please enter a reasonable amount.");
                        return;
                    }
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Please enter valid numbers for price range!");
                    return;
                }
                
                StringBuilder priceResults = new StringBuilder("=== CARS IN PRICE RANGE RM" + minPrice + " - RM" + maxPrice + " ===\n");
                boolean found = false;
                
                for (Car c : DataIO.allCars) {
                    if (c.price >= minPrice && c.price <= maxPrice && c.status.equalsIgnoreCase("available")) {
                        priceResults.append("ID: ").append(c.id)
                                   .append(" | Brand: ").append(c.brand)
                                   .append(" | Model: ").append(c.model)
                                   .append(" | Price: RM").append(c.price).append("\n");
                        found = true;
                    }
                }
                
                if (!found) {
                    priceResults.append("No available cars found in this price range.");
                } else {
                    priceResults.append("\nAll cars shown are available for purchase.");
                }
                
                JTextArea textArea = new JTextArea(priceResults.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(450, 250));
                JOptionPane.showMessageDialog(null, scrollPane, "Price Search Results", JOptionPane.INFORMATION_MESSAGE);

            } else if (e.getSource() == viewCarDetails) {
                String carId = JOptionPane.showInputDialog("Enter Car ID for detailed view:");
                if (carId == null || carId.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Car ID cannot be empty!");
                    return;
                }
                
                Car foundCar = null;
                for (Car c : DataIO.allCars) {
                    if (c.id.equalsIgnoreCase(carId.trim())) {
                        foundCar = c;
                        break;
                    }
                }
                
                if (foundCar == null) {
                    JOptionPane.showMessageDialog(null, "Car with ID '" + carId + "' not found.");
                    return;
                }
                
                String availabilityMsg = "";
                if (foundCar.status.equalsIgnoreCase("available")) {
                    availabilityMsg = "AVAILABLE FOR PURCHASE";
                } else if (foundCar.status.equalsIgnoreCase("booked")) {
                    availabilityMsg = "BOOKED (Payment Pending)";
                } else if (foundCar.status.equalsIgnoreCase("sold")) {
                    availabilityMsg = "SOLD";
                } else {
                    availabilityMsg = "Status: " + foundCar.status.toUpperCase();
                }
                
                String carDetails = "=== CAR DETAILS ===\n" +
                                  "ID: " + foundCar.id + "\n" +
                                  "Brand: " + foundCar.brand + "\n" +
                                  "Model: " + foundCar.model + "\n" +
                                  "Price: RM" + foundCar.price + "\n" +
                                  "Status: " + availabilityMsg + "\n\n";
                
                if (foundCar.status.equalsIgnoreCase("available")) {
                    carDetails += "This car is available for purchase!\n";
                    carDetails += "Contact a salesman to proceed with booking.";
                } else {
                    carDetails += "This car is currently not available for purchase.";
                }
                
                JOptionPane.showMessageDialog(null, carDetails);

            } else if (e.getSource() == back) {
                x.setVisible(false);
                new Page2Customer(); // Return to customer dashboard
            }

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Please enter valid numbers for price range.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error occurred: " + ex.getMessage());
        }
    }
}

