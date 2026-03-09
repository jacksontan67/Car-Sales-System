package acss;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Page7SalesmanRecords implements ActionListener {
    JFrame x;
    Button viewAllSales, viewSalesStats, searchSaleByDate, searchSaleByCustomer, back;

    public Page7SalesmanRecords() {
        x = new JFrame("My Sales Records - " + ACSS.currentUser.username);
        x.setSize(400, 180);
        x.setLocation(600, 300);
        x.setLayout(new FlowLayout());

        viewAllSales = new Button("View All My Sales");
        viewSalesStats = new Button("View Sales Statistics");
        searchSaleByDate = new Button("Search by Date");
        searchSaleByCustomer = new Button("Search by Customer");
        back = new Button("Back");

        viewAllSales.addActionListener(this);
        viewSalesStats.addActionListener(this);
        searchSaleByDate.addActionListener(this);
        searchSaleByCustomer.addActionListener(this);
        back.addActionListener(this);

        x.add(viewAllSales);
        x.add(viewSalesStats);
        x.add(searchSaleByDate);
        x.add(searchSaleByCustomer);
        x.add(back);
        x.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Salesman currentSalesman = (Salesman) ACSS.currentUser;

            if (e.getSource() == viewAllSales) {
                if (currentSalesman.sales.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "You have no sales records yet.");
                    return;
                }
                
                StringBuilder salesReport = new StringBuilder("=== MY SALES RECORDS ===\n");
                salesReport.append("Salesman: ").append(currentSalesman.username).append("\n");
                salesReport.append("Total Sales: ").append(currentSalesman.sales.size()).append("\n\n");
                
                double totalRevenue = 0;
                for (int i = 0; i < currentSalesman.sales.size(); i++) {
                    Sale s = currentSalesman.sales.get(i);
                    salesReport.append("Sale #").append(i + 1).append(":\n");
                    salesReport.append("  Car: ").append(s.car.brand).append(" ").append(s.car.model);
                    salesReport.append(" (ID: ").append(s.car.id).append(")\n");
                    salesReport.append("  Customer: ").append(s.buyer.username).append("\n");
                    salesReport.append("  Sale Price: RM").append(s.price).append("\n");
                    salesReport.append("  Date: ").append(s.date).append("\n");
                    salesReport.append("  Original Price: RM").append(s.car.price).append("\n");
                    salesReport.append("  Sale Comment: ").append(s.comment).append("\n"); 
                    salesReport.append("-----------------------------------\n");
                    totalRevenue += s.price;
                }
                
                salesReport.append("\nTOTAL REVENUE: RM").append(totalRevenue);
                
                JTextArea textArea = new JTextArea(salesReport.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 400));
                JOptionPane.showMessageDialog(null, scrollPane, "My Sales Records", JOptionPane.INFORMATION_MESSAGE);

            } else if (e.getSource() == viewSalesStats) {
                if (currentSalesman.sales.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "You have no sales records yet.");
                    return;
                }
                
                // Calculate statistics
                double totalRevenue = 0;
                double highestSale = 0;
                double lowestSale = Double.MAX_VALUE;
                String bestCustomer = "";
                int bestCustomerCount = 0;
                
                // Count sales per customer
                java.util.HashMap<String, Integer> customerCount = new java.util.HashMap<>();
                
                for (Sale s : currentSalesman.sales) {
                    totalRevenue += s.price;
                    if (s.price > highestSale) highestSale = s.price;
                    if (s.price < lowestSale) lowestSale = s.price;
                    
                    String customer = s.buyer.username;
                    customerCount.put(customer, customerCount.getOrDefault(customer, 0) + 1);
                    
                    if (customerCount.get(customer) > bestCustomerCount) {
                        bestCustomerCount = customerCount.get(customer);
                        bestCustomer = customer;
                    }
                }
                
                double averageSale = totalRevenue / currentSalesman.sales.size();
                
                String statsReport = "=== SALES STATISTICS ===\n" +
                                   "Salesman: " + currentSalesman.username + "\n\n" +
                                   "Total Sales: " + currentSalesman.sales.size() + "\n" +
                                   "Total Revenue: RM" + String.format("%.2f", totalRevenue) + "\n" +
                                   "Average Sale: RM" + String.format("%.2f", averageSale) + "\n" +
                                   "Highest Sale: RM" + String.format("%.2f", highestSale) + "\n" +
                                   "Lowest Sale: RM" + String.format("%.2f", lowestSale) + "\n" +
                                   "Best Customer: " + bestCustomer + " (" + bestCustomerCount + " purchases)\n\n" +
                                   "Performance Rating: " + getPerformanceRating(currentSalesman.sales.size());
                
                JOptionPane.showMessageDialog(null, statsReport);

            } else if (e.getSource() == searchSaleByDate) {
                if (currentSalesman.sales.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "You have no sales records yet.");
                    return;
                }
                
                String searchDate = JOptionPane.showInputDialog("Enter date to search (YYYY-MM-DD format):");
                if (searchDate == null || searchDate.trim().isEmpty()) {
                    return;
                }
                
                StringBuilder dateResults = new StringBuilder("=== SALES ON " + searchDate + " ===\n");
                boolean found = false;
                
                for (Sale s : currentSalesman.sales) {
                    if (s.date.contains(searchDate)) {
                        dateResults.append("Car: ").append(s.car.brand).append(" ").append(s.car.model);
                        dateResults.append(" | Customer: ").append(s.buyer.username);
                        dateResults.append(" | Amount: RM").append(s.price);
                        dateResults.append(" | Time: ").append(s.date);
                        dateResults.append(" | Comment: ").append(s.comment).append("\n"); 
                        found = true;
                    }
                }
                
                if (!found) {
                    dateResults.append("No sales found on this date.");
                }
                
                JTextArea textArea = new JTextArea(dateResults.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 250));
                JOptionPane.showMessageDialog(null, scrollPane, "Search Results", JOptionPane.INFORMATION_MESSAGE);

            } else if (e.getSource() == searchSaleByCustomer) {
                if (currentSalesman.sales.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "You have no sales records yet.");
                    return;
                }
                
                String customerName = JOptionPane.showInputDialog("Enter customer username to search:");
                if (customerName == null || customerName.trim().isEmpty()) {
                    return;
                }
                
                StringBuilder customerResults = new StringBuilder("=== SALES TO " + customerName.toUpperCase() + " ===\n");
                boolean found = false;
                double totalToCustomer = 0;
                
                for (Sale s : currentSalesman.sales) {
                    if (s.buyer.username.equalsIgnoreCase(customerName.trim())) {
                        customerResults.append("Car: ").append(s.car.brand).append(" ").append(s.car.model);
                        customerResults.append(" | Amount: RM").append(s.price);
                        customerResults.append(" | Date: ").append(s.date);
                        customerResults.append(" | Comment: ").append(s.comment).append("\n"); 
                        totalToCustomer += s.price;
                        found = true;
                    }
                }
                
                if (found) {
                    customerResults.append("\nTotal sales to this customer: RM").append(totalToCustomer);
                } else {
                    customerResults.append("No sales found to this customer.");
                }
                
                JTextArea textArea = new JTextArea(customerResults.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 250));
                JOptionPane.showMessageDialog(null, scrollPane, "Customer Sales", JOptionPane.INFORMATION_MESSAGE);

            } else if (e.getSource() == back) {
                x.setVisible(false);
                new Page2Salesman(); // Return to salesman dashboard
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error occurred: " + ex.getMessage());
        }
    }
    
    private String getPerformanceRating(int salesCount) {
        if (salesCount >= 10) return "Excellent";
        else if (salesCount >= 5) return "Good";
        else if (salesCount >= 3) return "Average";
        else if (salesCount >= 1) return "Needs Improvement";
        else return "No Sales Yet";
    }
}