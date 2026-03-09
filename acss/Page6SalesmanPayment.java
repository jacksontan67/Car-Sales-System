package acss;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Page6SalesmanPayment implements ActionListener {
    JFrame x;
    Button processPayment, viewPendingPayments, addSaleComment, back;

    public Page6SalesmanPayment() {
        x = new JFrame("Collect Payment & Comments");
        x.setSize(400, 150);
        x.setLocation(600, 300);
        x.setLayout(new FlowLayout());

        processPayment = new Button("Process Payment");
        viewPendingPayments = new Button("View Pending Payments");
        addSaleComment = new Button("Add Sale Comment");
        back = new Button("Back");

        processPayment.addActionListener(this);
        viewPendingPayments.addActionListener(this);
        addSaleComment.addActionListener(this);
        back.addActionListener(this);

        x.add(processPayment);
        x.add(viewPendingPayments);
        x.add(addSaleComment);
        x.add(back);
        x.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == processPayment) {
                String carId = JOptionPane.showInputDialog("Enter Car ID for payment:");
                if (carId == null || carId.trim().isEmpty()) {
                    return;
                }
                
                // Find the car
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
                
                // Show car details
                String carInfo = "Car Details:\n" +
                               "ID: " + foundCar.id + "\n" +
                               "Brand: " + foundCar.brand + "\n" +
                               "Model: " + foundCar.model + "\n" +
                               "Price: RM" + foundCar.price + "\n" +
                               "Status: " + foundCar.status;
                
                JOptionPane.showMessageDialog(null, carInfo);
                
                // Get customer username
                String customerUsername = JOptionPane.showInputDialog("Enter Customer Username:");
                if (customerUsername == null || customerUsername.trim().isEmpty()) {
                    return;
                }
                
                // Find customer
                Customer buyer = DataIO.searchCustomer(customerUsername.trim());
                if (buyer == null) {
                    JOptionPane.showMessageDialog(null, "Customer '" + customerUsername + "' not found.");
                    return;
                }
                
                if (!buyer.isApproved) {
                    JOptionPane.showMessageDialog(null, "Customer is not approved yet.");
                    return;
                }
                
                //PAYMENT AMOUNT VALIDATION
                String paymentStr = JOptionPane.showInputDialog("Enter payment amount (Default: RM" + foundCar.price + "):");
                double paymentAmount;
                if (paymentStr == null || paymentStr.trim().isEmpty()) {
                    paymentAmount = foundCar.price;
                } else {
                    try {
                        paymentAmount = Double.parseDouble(paymentStr.trim());
                        if (paymentAmount <= 0) {
                            JOptionPane.showMessageDialog(null, "Payment amount must be greater than zero!");
                            return;
                        }
                        if (paymentAmount > foundCar.price * 2) {
                            JOptionPane.showMessageDialog(null, "Payment amount seems unusually high. Please verify.");
                            return;
                        }
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid payment amount!");
                        return;
                    }
                }
                
                // Get payment comment
                String comment = JOptionPane.showInputDialog("Enter sale comment/notes (This will be saved with the sale record):");
                if (comment == null || comment.trim().isEmpty()) {
                    comment = "No comment";
                }
                
                // Create sale record with comment
                String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                Sale newSale = new Sale(foundCar, buyer, (Salesman)ACSS.currentUser, paymentAmount, currentDate, comment);
                
                // Add to collections
                DataIO.allSales.add(newSale);
                ((Salesman)ACSS.currentUser).sales.add(newSale);
                buyer.purchasedCars.add(foundCar);
                
                // Update car status to sold
                foundCar.status = "sold";
                
                // Save to file
                DataIO.writeToFile();
                
                String successMsg = "=== PAYMENT PROCESSED ===\n" +
                                  "Car: " + foundCar.brand + " " + foundCar.model + "\n" +
                                  "Customer: " + buyer.username + "\n" +
                                  "Amount: RM" + paymentAmount + "\n" +
                                  "Salesman: " + ACSS.currentUser.username + "\n" +
                                  "Date: " + currentDate + "\n" +
                                  "Sale Comment: " + comment + "\n" +
                                  "Car Status: Updated to SOLD";
                
                JOptionPane.showMessageDialog(null, successMsg);

            } else if (e.getSource() == viewPendingPayments) {
                // Show cars that are booked (pending payment)
                StringBuilder pendingList = new StringBuilder("=== PENDING PAYMENTS ===\n");
                boolean foundPending = false;
                
                for (Car c : DataIO.allCars) {
                    if (c.status.equalsIgnoreCase("booked")) {
                        pendingList.append("ID: ").append(c.id)
                                   .append(" | ").append(c.brand).append(" ").append(c.model)
                                   .append(" | RM").append(c.price)
                                   .append(" | Status: BOOKED").append("\n");
                        foundPending = true;
                    }
                }
                
                if (!foundPending) {
                    pendingList.append("No cars with 'booked' status found.");
                }
                
                JTextArea textArea = new JTextArea(pendingList.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(450, 250));
                JOptionPane.showMessageDialog(null, scrollPane, "Pending Payments", JOptionPane.INFORMATION_MESSAGE);

            } else if (e.getSource() == addSaleComment) {
                // Allow adding/updating comments to existing sales
                Salesman currentSalesman = (Salesman) ACSS.currentUser;
                
                if (currentSalesman.sales.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "You have no sales records yet.");
                    return;
                }
                
                StringBuilder salesList = new StringBuilder("Your Sales Records:\n\n");
                for (int i = 0; i < currentSalesman.sales.size(); i++) {
                    Sale s = currentSalesman.sales.get(i);
                    salesList.append((i+1)).append(". Car: ").append(s.car.brand)
                            .append(" ").append(s.car.model)
                            .append(" (ID: ").append(s.car.id).append(")")
                            .append(" | Customer: ").append(s.buyer.username)
                            .append(" | Amount: RM").append(s.price)
                            .append(" | Current Comment: ").append(s.comment).append("\n");
                }
                
                JTextArea textArea = new JTextArea(salesList.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(600, 300));
                JOptionPane.showMessageDialog(null, scrollPane, "Your Sales", JOptionPane.INFORMATION_MESSAGE);
                
                // Ask which sale to add comment to
                String saleNumber = JOptionPane.showInputDialog("Enter sale number to add/update comment (1-" + currentSalesman.sales.size() + "):");
                if (saleNumber != null && !saleNumber.trim().isEmpty()) {
                    try {
                        int saleIndex = Integer.parseInt(saleNumber) - 1;
                        if (saleIndex >= 0 && saleIndex < currentSalesman.sales.size()) {
                            Sale selectedSale = currentSalesman.sales.get(saleIndex);
                            
                            String newComment = JOptionPane.showInputDialog("Enter new comment for this sale:", selectedSale.comment);
                            if (newComment != null && !newComment.trim().isEmpty()) {
                                selectedSale.comment = newComment.trim();
                                
                                // Update in the main sales list as well
                                for (Sale sale : DataIO.allSales) {
                                    if (sale.car.id.equals(selectedSale.car.id) && 
                                        sale.buyer.username.equals(selectedSale.buyer.username) &&
                                        sale.date.equals(selectedSale.date)) {
                                        sale.comment = newComment.trim();
                                        break;
                                    }
                                }
                                
                                DataIO.writeToFile();
                                JOptionPane.showMessageDialog(null, "Comment updated successfully!\nNew comment: " + newComment);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid sale number!");
                        }
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid number!");
                    }
                }

            } else if (e.getSource() == back) {
                x.setVisible(false);
                new Page2Salesman(); // Return to salesman dashboard
            }

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number for payment amount.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error occurred: " + ex.getMessage());
        }
    }
}