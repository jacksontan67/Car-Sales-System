package acss;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Page5SalesmanUpdateStatus implements ActionListener {
    JFrame x;
    Button updateStatus, viewMyCars, back;

    public Page5SalesmanUpdateStatus() {
        x = new JFrame("Update Car Status");
        x.setSize(350, 150);
        x.setLocation(600, 300);
        x.setLayout(new FlowLayout());

        updateStatus = new Button("Update Car Status");
        viewMyCars = new Button("View My Cars");
        back = new Button("Back");

        updateStatus.addActionListener(this);
        viewMyCars.addActionListener(this);
        back.addActionListener(this);

        x.add(updateStatus);
        x.add(viewMyCars);
        x.add(back);
        x.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == updateStatus) {
                String carId = JOptionPane.showInputDialog("Enter Car ID to update:");
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
                
                // Show current status
                String currentInfo = "Current Car Details:\n" +
                                   "ID: " + foundCar.id + "\n" +
                                   "Brand: " + foundCar.brand + "\n" +
                                   "Model: " + foundCar.model + "\n" +
                                   "Price: RM" + foundCar.price + "\n" +
                                   "Current Status: " + foundCar.status;
                
                JOptionPane.showMessageDialog(null, currentInfo);
                
                // Status options for salesmen
                String[] statusOptions = {"available", "booked", "paid", "cancel"};
                String newStatus = (String) JOptionPane.showInputDialog(null, 
                    "Select new status for Car ID: " + foundCar.id, 
                    "Update Car Status", 
                    JOptionPane.QUESTION_MESSAGE, 
                    null, 
                    statusOptions, 
                    foundCar.status);
                
                if (newStatus != null && !newStatus.equals(foundCar.status)) {
                    String oldStatus = foundCar.status;
                    foundCar.status = newStatus;
                    
                    // Save to file
                    DataIO.writeToFile();
                    
                    String updateMsg = "Car Status Updated Successfully!\n" +
                                     "Car ID: " + foundCar.id + "\n" +
                                     "Old Status: " + oldStatus + "\n" +
                                     "New Status: " + newStatus + "\n" +
                                     "Updated by: " + ACSS.currentUser.username;
                    
                    JOptionPane.showMessageDialog(null, updateMsg);
                } else if (newStatus != null) {
                    JOptionPane.showMessageDialog(null, "Status unchanged.");
                }

            } else if (e.getSource() == viewMyCars) {
                // Show all cars (salesmen can see all cars to manage)
                if (DataIO.allCars.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No cars in the system.");
                    return;
                }
                
                StringBuilder carList = new StringBuilder("=== CARS YOU CAN MANAGE ===\n");
                carList.append("Salesman: ").append(ACSS.currentUser.username).append("\n\n");
                
                for (Car c : DataIO.allCars) {
                    carList.append("ID: ").append(c.id)
                           .append(" | ").append(c.brand).append(" ").append(c.model)
                           .append(" | RM").append(c.price)
                           .append(" | Status: ").append(c.status).append("\n");
                }
                
                JTextArea textArea = new JTextArea(carList.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 300));
                JOptionPane.showMessageDialog(null, scrollPane, "My Cars", JOptionPane.INFORMATION_MESSAGE);

            } else if (e.getSource() == back) {
                x.setVisible(false);
                new Page2Salesman(); // Return to salesman dashboard
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error occurred: " + ex.getMessage());
        }
    }
}