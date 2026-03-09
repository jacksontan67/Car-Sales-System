package acss;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Page5ManagerReports implements ActionListener {
    JFrame x;
    Button topSelling, salesBySalesman, paymentAnalysis, feedbackAnalysis, back;

    public Page5ManagerReports() {
        x = new JFrame("Reports & Analysis");
        x.setSize(400, 200);
        x.setLocation(600, 300);
        x.setLayout(new FlowLayout());

        topSelling = new Button("Top-Selling Cars");
        salesBySalesman = new Button("Sales by Salesman");
        paymentAnalysis = new Button("Payment Analysis");
        feedbackAnalysis = new Button("Feedback Analysis");
        back = new Button("Back");

        topSelling.addActionListener(this);
        salesBySalesman.addActionListener(this);
        paymentAnalysis.addActionListener(this);
        feedbackAnalysis.addActionListener(this);
        back.addActionListener(this);

        x.add(topSelling);
        x.add(salesBySalesman);
        x.add(paymentAnalysis);
        x.add(feedbackAnalysis);
        x.add(back);
        x.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == topSelling) {
                if (DataIO.allSales.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No sales yet.");
                    return;
                }
                
                HashMap<String, Integer> carSalesCount = new HashMap<>();
                
                // Count sales for each car model
                for (Sale sale : DataIO.allSales) {
                    String carKey = sale.car.brand + " " + sale.car.model;
                    carSalesCount.put(carKey, carSalesCount.getOrDefault(carKey, 0) + 1);
                }
                
                StringBuilder result = new StringBuilder("=== TOP-SELLING CARS ===\n\n");
                result.append("Total Sales Recorded: ").append(DataIO.allSales.size()).append("\n\n");
                
                // Sort by sales count
                carSalesCount.entrySet().stream()
                    .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                    .forEach(entry -> {
                        result.append("Car: ").append(entry.getKey())
                              .append(" | Times Sold: ").append(entry.getValue()).append("\n");
                    });
                
                if (carSalesCount.isEmpty()) {
                    result.append("No car sales data available.");
                }
                
                JTextArea textArea = new JTextArea(result.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 300));
                JOptionPane.showMessageDialog(null, scrollPane, "Top-Selling Cars", JOptionPane.INFORMATION_MESSAGE);

            } else if (e.getSource() == salesBySalesman) {
                if (DataIO.allSales.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No sales yet.");
                    return;
                }
                
                HashMap<String, Integer> salesmanSalesCount = new HashMap<>();
                HashMap<String, Double> salesmanRevenue = new HashMap<>();
                
                // Count sales and revenue for each salesman
                for (Sale sale : DataIO.allSales) {
                    String salesmanName = sale.seller.username;
                    salesmanSalesCount.put(salesmanName, salesmanSalesCount.getOrDefault(salesmanName, 0) + 1);
                    salesmanRevenue.put(salesmanName, salesmanRevenue.getOrDefault(salesmanName, 0.0) + sale.price);
                }
                
                StringBuilder result = new StringBuilder("=== SALES BY SALESMAN ===\n\n");
                result.append("Total Sales: ").append(DataIO.allSales.size()).append("\n\n");
                
                for (Map.Entry<String, Integer> entry : salesmanSalesCount.entrySet()) {
                    String salesmanName = entry.getKey();
                    int salesCount = entry.getValue();
                    double revenue = salesmanRevenue.get(salesmanName);
                    
                    result.append("Salesman: ").append(salesmanName).append("\n");
                    result.append("  Sales Count: ").append(salesCount).append("\n");
                    result.append("  Total Revenue: RM").append(String.format("%.2f", revenue)).append("\n");
                    result.append("  Average Sale: RM").append(String.format("%.2f", revenue/salesCount)).append("\n");
                    result.append("-----------------------------------\n");
                }
                
                JTextArea textArea = new JTextArea(result.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(450, 350));
                JOptionPane.showMessageDialog(null, scrollPane, "Sales by Salesman", JOptionPane.INFORMATION_MESSAGE);

            } else if (e.getSource() == paymentAnalysis) {
                if (DataIO.allSales.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No payment data available yet.");
                    return;
                }
                
                double totalRevenue = 0;
                double highestPayment = 0;
                double lowestPayment = Double.MAX_VALUE;
                
                for (Sale sale : DataIO.allSales) {
                    totalRevenue += sale.price;
                    if (sale.price > highestPayment) {
                        highestPayment = sale.price;
                    }
                    if (sale.price < lowestPayment) {
                        lowestPayment = sale.price;
                    }
                }
                
                double averagePayment = totalRevenue / DataIO.allSales.size();
                
                StringBuilder analysis = new StringBuilder("=== PAYMENT ANALYSIS ===\n\n");
                analysis.append("PAYMENT SUMMARY:\n");
                analysis.append("Total Transactions: ").append(DataIO.allSales.size()).append("\n");
                analysis.append("Total Revenue: RM").append(String.format("%.2f", totalRevenue)).append("\n");
                analysis.append("Average Payment: RM").append(String.format("%.2f", averagePayment)).append("\n");
                analysis.append("Highest Payment: RM").append(String.format("%.2f", highestPayment)).append("\n");
                analysis.append("Lowest Payment: RM").append(String.format("%.2f", lowestPayment)).append("\n\n");
                
                analysis.append("PAYMENT BREAKDOWN:\n");
                for (Sale sale : DataIO.allSales) {
                    analysis.append("Customer: ").append(sale.buyer.username);
                    analysis.append(" | Car: ").append(sale.car.brand).append(" ").append(sale.car.model);
                    analysis.append(" | Amount: RM").append(sale.price);
                    analysis.append(" | Date: ").append(sale.date).append("\n");
                }
                
                JTextArea textArea = new JTextArea(analysis.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(600, 400));
                JOptionPane.showMessageDialog(null, scrollPane, "Payment Analysis", JOptionPane.INFORMATION_MESSAGE);

            } else if (e.getSource() == feedbackAnalysis) {
                if (DataIO.allFeedbacks.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No feedback data available yet.\nCustomers need to give feedback first!");
                    return;
                }
                
                int totalFeedbacks = DataIO.allFeedbacks.size();
                int rating5 = 0, rating4 = 0, rating3 = 0, rating2 = 0, rating1 = 0;
                double totalRatingSum = 0;
                
                HashMap<String, Integer> brandRatings = new HashMap<>();
                HashMap<String, Integer> brandCounts = new HashMap<>();
                
                StringBuilder analysis = new StringBuilder("=== FEEDBACK ANALYSIS ===\n\n");
                analysis.append("FEEDBACK SUMMARY:\n");
                analysis.append("Total Feedbacks: ").append(totalFeedbacks).append("\n\n");
                
                for (Feedback feedback : DataIO.allFeedbacks) {
                    totalRatingSum += feedback.rating;
                    
                    // Count ratings
                    switch (feedback.rating) {
                        case 5: rating5++; break;
                        case 4: rating4++; break;
                        case 3: rating3++; break;
                        case 2: rating2++; break;
                        case 1: rating1++; break;
                    }
                    
                    // Brand ratings
                    String brand = feedback.carBrand;
                    brandRatings.put(brand, brandRatings.getOrDefault(brand, 0) + feedback.rating);
                    brandCounts.put(brand, brandCounts.getOrDefault(brand, 0) + 1);
                }
                
                double averageRating = totalRatingSum / totalFeedbacks;
                
                analysis.append("RATING DISTRIBUTION:\n");
                analysis.append("5 Stars (Excellent): ").append(rating5).append(" (").append(String.format("%.1f", rating5*100.0/totalFeedbacks)).append("%)\n");
                analysis.append("4 Stars (Very Good): ").append(rating4).append(" (").append(String.format("%.1f", rating4*100.0/totalFeedbacks)).append("%)\n");
                analysis.append("3 Stars (Good): ").append(rating3).append(" (").append(String.format("%.1f", rating3*100.0/totalFeedbacks)).append("%)\n");
                analysis.append("2 Stars (Fair): ").append(rating2).append(" (").append(String.format("%.1f", rating2*100.0/totalFeedbacks)).append("%)\n");
                analysis.append("1 Star (Poor): ").append(rating1).append(" (").append(String.format("%.1f", rating1*100.0/totalFeedbacks)).append("%)\n\n");
                
                analysis.append("OVERALL RATING: ").append(String.format("%.1f", averageRating)).append("/5.0\n\n");
                
                // Brand performance
                analysis.append("BRAND PERFORMANCE:\n");
                for (Map.Entry<String, Integer> entry : brandCounts.entrySet()) {
                    String brand = entry.getKey();
                    int count = entry.getValue();
                    double avgBrandRating = brandRatings.get(brand) / (double) count;
                    analysis.append("Brand: ").append(brand);
                    analysis.append(" | Avg Rating: ").append(String.format("%.1f", avgBrandRating)).append("/5.0");
                    analysis.append(" (").append(count).append(" feedbacks)\n");
                }
                
                analysis.append("\nRECENT FEEDBACKS:\n");
                int count = 0;
                for (int i = DataIO.allFeedbacks.size() - 1; i >= 0 && count < 5; i--, count++) {
                    Feedback feedback = DataIO.allFeedbacks.get(i);
                    analysis.append("Customer: ").append(feedback.customerUsername);
                    analysis.append(" | Car: ").append(feedback.carBrand).append(" ").append(feedback.carModel);
                    analysis.append(" | Rating: ").append(feedback.rating).append("/5");
                    analysis.append(" | Comment: ").append(feedback.comment).append("\n");
                }
                
                JTextArea textArea = new JTextArea(analysis.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(600, 450));
                JOptionPane.showMessageDialog(null, scrollPane, "Feedback Analysis", JOptionPane.INFORMATION_MESSAGE);

            } else if (e.getSource() == back) {
                x.setVisible(false);
                new Page2Manager();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error generating report: " + ex.getMessage());
        }
    }
}