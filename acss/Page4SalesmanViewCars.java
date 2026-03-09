package acss;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Page4SalesmanViewCars implements ActionListener {
    JFrame x;
    Button viewAll, searchByStatus, searchById, back;

    public Page4SalesmanViewCars() {
        x = new JFrame("View Cars Status");
        x.setSize(400, 150);
        x.setLocation(600, 300);
        x.setLayout(new FlowLayout());

        viewAll = new Button("View All Cars");
        searchByStatus = new Button("Search by Status");
        searchById = new Button("Search by ID");
        back = new Button("Back");

        viewAll.addActionListener(this);
        searchByStatus.addActionListener(this);
        searchById.addActionListener(this);
        back.addActionListener(this);

        x.add(viewAll);
        x.add(searchByStatus);
        x.add(searchById);
        x.add(back);
        x.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == viewAll) {
                if (DataIO.allCars.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No cars in the system.");
                    return;
                }
                
                StringBuilder carList = new StringBuilder("=== ALL CARS ===\n");
                for (Car c : DataIO.allCars) {
                    carList.append("ID: ").append(c.id)
                           .append(" | Brand: ").append(c.brand)
                           .append(" | Model: ").append(c.model)
                           .append(" | Price: RM").append(c.price)
                           .append(" | Status: ").append(c.status).append("\n");
                }
                
                JTextArea textArea = new JTextArea(carList.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 300));
                JOptionPane.showMessageDialog(null, scrollPane, "All Cars", JOptionPane.INFORMATION_MESSAGE);

            } else if (e.getSource() == searchByStatus) {
                String[] statusOptions = {"available", "booked", "sold"};
                String selectedStatus = (String) JOptionPane.showInputDialog(null, 
                    "Select status to search:", "Search by Status", 
                    JOptionPane.QUESTION_MESSAGE, null, statusOptions, statusOptions[0]);
                
                if (selectedStatus != null) {
                    StringBuilder result = new StringBuilder("=== CARS WITH STATUS: " + selectedStatus.toUpperCase() + " ===\n");
                    boolean found = false;
                    
                    for (Car c : DataIO.allCars) {
                        if (c.status.equalsIgnoreCase(selectedStatus)) {
                            result.append("ID: ").append(c.id)
                                  .append(" | Brand: ").append(c.brand)
                                  .append(" | Model: ").append(c.model)
                                  .append(" | Price: RM").append(c.price).append("\n");
                            found = true;
                        }
                    }
                    
                    if (!found) {
                        result.append("No cars found with status: ").append(selectedStatus);
                    }
                    
                    JTextArea textArea = new JTextArea(result.toString());
                    textArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(450, 200));
                    JOptionPane.showMessageDialog(null, scrollPane, "Search Results", JOptionPane.INFORMATION_MESSAGE);
                }

            } else if (e.getSource() == searchById) {
                String carId = JOptionPane.showInputDialog("Enter Car ID to search:");
                if (carId != null && !carId.trim().isEmpty()) {
                    for (Car c : DataIO.allCars) {
                        if (c.id.equalsIgnoreCase(carId.trim())) {
                            String carInfo = "=== CAR DETAILS ===\n" +
                                           "ID: " + c.id + "\n" +
                                           "Brand: " + c.brand + "\n" +
                                           "Model: " + c.model + "\n" +
                                           "Price: RM" + c.price + "\n" +
                                           "Status: " + c.status;
                            JOptionPane.showMessageDialog(null, carInfo);
                            return;
                        }
                    }
                    JOptionPane.showMessageDialog(null, "Car with ID '" + carId + "' not found.");
                }

            } else if (e.getSource() == back) {
                x.setVisible(false);
                new Page2Salesman(); // Return to salesman dashboard
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error occurred: " + ex.getMessage());
        }
    }
}