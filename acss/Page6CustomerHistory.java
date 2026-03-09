package acss;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Page6CustomerHistory implements ActionListener {
    JFrame x;
    Button viewPurchaseHistory, viewDetailedHistory, viewSpendingAnalysis, searchPurchaseHistory, viewRatingHistory, back;

    public Page6CustomerHistory() {
        x = new JFrame("Purchase History & Ratings - " + ACSS.currentUser.username);
        x.setSize(450, 200);
        x.setLocation(600, 300);
        x.setLayout(new FlowLayout());

        viewPurchaseHistory = new Button("Purchase History");
        viewDetailedHistory = new Button("Detailed History");
        viewSpendingAnalysis = new Button("Spending Analysis");
        searchPurchaseHistory = new Button("Search History");
        viewRatingHistory = new Button("Rating History");
        back = new Button("Back");

        viewPurchaseHistory.addActionListener(this);
        viewDetailedHistory.addActionListener(this);
        viewSpendingAnalysis.addActionListener(this);
        searchPurchaseHistory.addActionListener(this);
        viewRatingHistory.addActionListener(this);
        back.addActionListener(this);

        x.add(viewPurchaseHistory);
        x.add(viewDetailedHistory);
        x.add(viewSpendingAnalysis);
        x.add(searchPurchaseHistory);
        x.add(viewRatingHistory);
        x.add(back);
        x.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Customer currentCustomer = (Customer) ACSS.currentUser;

            if (e.getSource() == viewPurchaseHistory) {
                if (currentCustomer.purchasedCars.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "You have no purchase history yet.\nBrowse available cars to make your first purchase!");
                    return;
                }
                
                StringBuilder historyReport = new StringBuilder("=== PURCHASE HISTORY ===\n");
                historyReport.append("Customer: ").append(currentCustomer.username).append("\n");
                historyReport.append("Total Purchases: ").append(currentCustomer.purchasedCars.size()).append("\n\n");
                
                double totalSpent = 0;
                for (int i = 0; i < currentCustomer.purchasedCars.size(); i++) {
                    Car car = currentCustomer.purchasedCars.get(i);
                    historyReport.append("Purchase #").append(i + 1).append(":\n");
                    historyReport.append("  Car ID: ").append(car.id).append("\n");
                    historyReport.append("  Brand: ").append(car.brand).append("\n");
                    historyReport.append("  Model: ").append(car.model).append("\n");
                    historyReport.append("  Price: RM").append(car.price).append("\n");
                    historyReport.append("  Status: ").append(car.status.toUpperCase()).append("\n");
                    
                    // Find rating for this car
                    Feedback carFeedback = findFeedbackForCar(currentCustomer.username, car.id);
                    if (carFeedback != null) {
                        historyReport.append("  Your Rating: ").append(carFeedback.rating).append("/5 stars\n");
                        historyReport.append("  Your Review: ").append(carFeedback.comment).append("\n");
                        historyReport.append("  Review Date: ").append(carFeedback.date).append("\n");
                    } else {
                        historyReport.append("  Your Rating: Not rated yet\n");
                    }
                    
                    historyReport.append("-----------------------------------\n");
                    totalSpent += car.price;
                }
                
                historyReport.append("\nSUMMARY:\n");
                historyReport.append("Total Amount Spent: RM").append(String.format("%.2f", totalSpent)).append("\n");
                historyReport.append("Average Purchase: RM").append(String.format("%.2f", totalSpent / currentCustomer.purchasedCars.size()));
                
                JTextArea textArea = new JTextArea(historyReport.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 400));
                JOptionPane.showMessageDialog(null, scrollPane, "Purchase History", JOptionPane.INFORMATION_MESSAGE);

            } else if (e.getSource() == viewDetailedHistory) {
                if (currentCustomer.purchasedCars.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "You have no purchase history yet.");
                    return;
                }
                
                // Find sales records for customer
                StringBuilder detailedReport = new StringBuilder("=== DETAILED PURCHASE HISTORY ===\n");
                detailedReport.append("Customer: ").append(currentCustomer.username).append("\n\n");
                
                boolean foundSales = false;
                for (Sale sale : DataIO.allSales) {
                    if (sale.buyer.username.equals(currentCustomer.username)) {
                        detailedReport.append("PURCHASE DETAILS:\n");
                        detailedReport.append("Car: ").append(sale.car.brand).append(" ").append(sale.car.model);
                        detailedReport.append(" (ID: ").append(sale.car.id).append(")\n");
                        detailedReport.append("Purchase Price: RM").append(sale.price).append("\n");
                        detailedReport.append("Original Price: RM").append(sale.car.price).append("\n");
                        detailedReport.append("Salesman: ").append(sale.seller.username).append("\n");
                        detailedReport.append("Purchase Date: ").append(sale.date).append("\n");
                        
                        if (sale.price != sale.car.price) {
                            double discount = sale.car.price - sale.price;
                            detailedReport.append("Discount Received: RM").append(String.format("%.2f", discount)).append("\n");
                        }
                        
                        detailedReport.append("===================================\n");
                        foundSales = true;
                    }
                }
                
                if (!foundSales) {
                    detailedReport.append("Detailed sales records not found.\n");
                    detailedReport.append("This might be because purchases were made before the sales tracking system was implemented.\n\n");
                    detailedReport.append("Basic Purchase List:\n");
                    for (int i = 0; i < currentCustomer.purchasedCars.size(); i++) {
                        Car car = currentCustomer.purchasedCars.get(i);
                        detailedReport.append((i + 1)).append(". ").append(car.brand).append(" ").append(car.model)
                                     .append(" - RM").append(car.price).append("\n");
                    }
                }
                
                JTextArea textArea = new JTextArea(detailedReport.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 400));
                JOptionPane.showMessageDialog(null, scrollPane, "Detailed History", JOptionPane.INFORMATION_MESSAGE);

            } else if (e.getSource() == viewSpendingAnalysis) {
                if (currentCustomer.purchasedCars.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "You have no purchases yet for analysis.");
                    return;
                }
                
                // Calculate spending analysis
                double totalSpent = 0;
                double highestPurchase = 0;
                double lowestPurchase = Double.MAX_VALUE;
                java.util.HashMap<String, Integer> brandCount = new java.util.HashMap<>();
                java.util.HashMap<String, Double> brandSpending = new java.util.HashMap<>();
                
                for (Car car : currentCustomer.purchasedCars) {
                    totalSpent += car.price;
                    if (car.price > highestPurchase) highestPurchase = car.price;
                    if (car.price < lowestPurchase) lowestPurchase = car.price;
                    
                    brandCount.put(car.brand, brandCount.getOrDefault(car.brand, 0) + 1);
                    brandSpending.put(car.brand, brandSpending.getOrDefault(car.brand, 0.0) + car.price);
                }
                
                double averageSpending = totalSpent / currentCustomer.purchasedCars.size();
                
                // Find favorite brand
                String favoriteBrand = "";
                int maxBrandCount = 0;
                for (java.util.Map.Entry<String, Integer> entry : brandCount.entrySet()) {
                    if (entry.getValue() > maxBrandCount) {
                        maxBrandCount = entry.getValue();
                        favoriteBrand = entry.getKey();
                    }
                }
                
                StringBuilder analysisReport = new StringBuilder("=== SPENDING ANALYSIS ===\n");
                analysisReport.append("Customer: ").append(currentCustomer.username).append("\n\n");
                
                analysisReport.append("FINANCIAL SUMMARY:\n");
                analysisReport.append("Total Spent: RM").append(String.format("%.2f", totalSpent)).append("\n");
                analysisReport.append("Average Purchase: RM").append(String.format("%.2f", averageSpending)).append("\n");
                analysisReport.append("Highest Purchase: RM").append(String.format("%.2f", highestPurchase)).append("\n");
                analysisReport.append("Lowest Purchase: RM").append(String.format("%.2f", lowestPurchase)).append("\n\n");
                
                analysisReport.append("BUYING PREFERENCES:\n");
                analysisReport.append("Total Cars Purchased: ").append(currentCustomer.purchasedCars.size()).append("\n");
                analysisReport.append("Favorite Brand: ").append(favoriteBrand).append(" (").append(maxBrandCount).append(" cars)\n\n");
                
                analysisReport.append("BRAND BREAKDOWN:\n");
                for (java.util.Map.Entry<String, Integer> entry : brandCount.entrySet()) {
                    String brand = entry.getKey();
                    int count = entry.getValue();
                    double spending = brandSpending.get(brand);
                    analysisReport.append("• ").append(brand).append(": ").append(count)
                                 .append(" cars, RM").append(String.format("%.2f", spending)).append(" spent\n");
                }
                
                analysisReport.append("\nCUSTOMER RATING:\n");
                if (totalSpent >= 100000) {
                    analysisReport.append("VIP Customer - Premium Buyer");
                } else if (totalSpent >= 50000) {
                    analysisReport.append("Gold Customer - Valued Buyer");
                } else if (totalSpent >= 20000) {
                    analysisReport.append("Silver Customer - Regular Buyer");
                } else {
                    analysisReport.append("Bronze Customer - New Buyer");
                }
                
                JTextArea textArea = new JTextArea(analysisReport.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 400));
                JOptionPane.showMessageDialog(null, scrollPane, "Spending Analysis", JOptionPane.INFORMATION_MESSAGE);

            } else if (e.getSource() == searchPurchaseHistory) {
                if (currentCustomer.purchasedCars.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "You have no purchases to search.");
                    return;
                }
                
                String[] searchOptions = {"Search by Brand", "Search by Price Range", "Search by Car ID"};
                String searchType = (String) JOptionPane.showInputDialog(null, 
                    "How would you like to search your purchase history?", 
                    "Search Options", 
                    JOptionPane.QUESTION_MESSAGE, 
                    null, 
                    searchOptions, 
                    searchOptions[0]);
                
                if (searchType == null) return;
                
                StringBuilder searchResults = new StringBuilder("=== SEARCH RESULTS ===\n");
                boolean found = false;
                
                if (searchType.equals("Search by Brand")) {
                    String brand = JOptionPane.showInputDialog("Enter brand name:");
                    if (brand != null && !brand.trim().isEmpty()) {
                        searchResults.append("Your ").append(brand.toUpperCase()).append(" purchases:\n\n");
                        for (Car car : currentCustomer.purchasedCars) {
                            if (car.brand.equalsIgnoreCase(brand.trim())) {
                                searchResults.append("• ").append(car.brand).append(" ").append(car.model)
                                           .append(" (ID: ").append(car.id).append(") - RM").append(car.price).append("\n");
                                found = true;
                            }
                        }
                    }
                } else if (searchType.equals("Search by Price Range")) {
                    String minStr = JOptionPane.showInputDialog("Enter minimum price:");
                    String maxStr = JOptionPane.showInputDialog("Enter maximum price:");
                    if (minStr != null && maxStr != null) {
                        double min = Double.parseDouble(minStr);
                        double max = Double.parseDouble(maxStr);
                        searchResults.append("Your purchases between RM").append(min).append(" - RM").append(max).append(":\n\n");
                        for (Car car : currentCustomer.purchasedCars) {
                            if (car.price >= min && car.price <= max) {
                                searchResults.append("• ").append(car.brand).append(" ").append(car.model)
                                           .append(" (ID: ").append(car.id).append(") - RM").append(car.price).append("\n");
                                found = true;
                            }
                        }
                    }
                } else if (searchType.equals("Search by Car ID")) {
                    String carId = JOptionPane.showInputDialog("Enter Car ID:");
                    if (carId != null && !carId.trim().isEmpty()) {
                        for (Car car : currentCustomer.purchasedCars) {
                            if (car.id.equalsIgnoreCase(carId.trim())) {
                                searchResults.append("Car found in your purchases:\n\n");
                                searchResults.append("ID: ").append(car.id).append("\n");
                                searchResults.append("Brand: ").append(car.brand).append("\n");
                                searchResults.append("Model: ").append(car.model).append("\n");
                                searchResults.append("Price: RM").append(car.price).append("\n");
                                searchResults.append("Status: ").append(car.status).append("\n");
                                found = true;
                                break;
                            }
                        }
                    }
                }
                
                if (!found) {
                    searchResults.append("No purchases found matching your search criteria.");
                }
                
                JTextArea textArea = new JTextArea(searchResults.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(450, 300));
                JOptionPane.showMessageDialog(null, scrollPane, "Search Results", JOptionPane.INFORMATION_MESSAGE);

            } else if (e.getSource() == viewRatingHistory) {
                // Show rating history from feedback data
                StringBuilder ratingReport = new StringBuilder("=== RATING HISTORY ===\n");
                ratingReport.append("Customer: ").append(currentCustomer.username).append("\n\n");
                
                boolean hasRatings = false;
                for (Feedback feedback : DataIO.allFeedbacks) {
                    if (feedback.customerUsername.equals(currentCustomer.username)) {
                        ratingReport.append("Car: ").append(feedback.carBrand).append(" ").append(feedback.carModel).append("\n");
                        ratingReport.append("Rating: ").append(feedback.rating).append("/5 stars\n");
                        ratingReport.append("Comment: ").append(feedback.comment).append("\n");
                        ratingReport.append("Date: ").append(feedback.date).append("\n");
                        ratingReport.append("-----------------------------------\n");
                        hasRatings = true;
                    }
                }
                
                if (!hasRatings) {
                    ratingReport.append("You haven't given any ratings yet.\n");
                    ratingReport.append("Purchase cars and give ratings to help others!");
                }
                
                JTextArea textArea = new JTextArea(ratingReport.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(450, 300));
                JOptionPane.showMessageDialog(null, scrollPane, "Rating History", JOptionPane.INFORMATION_MESSAGE);

            } else if (e.getSource() == back) {
                x.setVisible(false);
                new Page2Customer(); // Return to customer dashboard
            }

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Please enter valid numbers for price search.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error occurred: " + ex.getMessage());
        }
    }
    
    //find feedback for a specific car
    private Feedback findFeedbackForCar(String customerUsername, String carId) {
        for (Feedback feedback : DataIO.allFeedbacks) {
            if (feedback.customerUsername.equals(customerUsername) && feedback.carId.equals(carId)) {
                return feedback;
            }
        }
        return null;
    }
}