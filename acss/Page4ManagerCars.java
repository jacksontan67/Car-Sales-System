package acss;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Page4ManagerCars implements ActionListener {
    JFrame x;
    Button add, update, delete, search, back;

    public Page4ManagerCars() {
        x = new JFrame("Manage Cars");
        x.setSize(400, 200);
        x.setLocation(600, 300);
        x.setLayout(new FlowLayout());

        add = new Button("Add Car");
        update = new Button("Update Car");
        delete = new Button("Delete Car");
        search = new Button("Search Car");
        back = new Button("Back");

        add.addActionListener(this);
        update.addActionListener(this);
        delete.addActionListener(this);
        search.addActionListener(this);
        back.addActionListener(this);

        x.add(add);
        x.add(update);
        x.add(delete);
        x.add(search);
        x.add(back);
        x.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == add) {
                String id = JOptionPane.showInputDialog("Car ID:");
                if (id == null || id.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Car ID cannot be empty!");
                    return;
                }
                
                // Check if Car ID contains only numbers
                try {
                    Integer.parseInt(id.trim());
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Car ID must contain only numbers!");
                    return;
                }
                
                // Check if Car ID already exists in DataIO.allCars 
                Car existingCar = null;
                for (Car car : DataIO.allCars) {
                    if (car.id.equals(id.trim())) {
                        existingCar = car;
                        break;
                    }
                }
                
                if (existingCar != null) {
                    JOptionPane.showMessageDialog(null, "Car ID '" + id.trim() + "' already exists! Please use a different ID.");
                    return;
                }
                
                String brand = JOptionPane.showInputDialog("Brand:");
                if (brand == null || brand.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Brand cannot be empty!");
                    return;
                }
                
                String model = JOptionPane.showInputDialog("Model:");
                if (model == null || model.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Model cannot be empty!");
                    return;
                }
                
                String priceStr = JOptionPane.showInputDialog("Price:");
                if (priceStr == null || priceStr.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Price cannot be empty!");
                    return;
                }
                
                double price;
                try {
                    price = Double.parseDouble(priceStr.trim());
                    if (price < 0) {
                        JOptionPane.showMessageDialog(null, "Price cannot be negative! Please enter a positive number.");
                        return;
                    }
                    if (price == 0) {
                        JOptionPane.showMessageDialog(null, "Price cannot be zero! Please enter a positive number.");
                        return;
                    }
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Price must be a number! Please enter numbers only (e.g., 25000.50)");
                    return;
                }
                
                String status = "available";
                DataIO.allCars.add(new Car(id.trim(), brand.trim(), model.trim(), price, status));
                JOptionPane.showMessageDialog(null, "Car added successfully.");
                DataIO.writeToFile();

            } else if (e.getSource() == update) {
                String id = JOptionPane.showInputDialog("Enter Car ID to update:");
                if (id == null || id.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Car ID cannot be empty!");
                    return;
                }
                
                // Check if Car ID contains only numbers
                try {
                    Integer.parseInt(id.trim());
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Car ID must contain only numbers!");
                    return;
                }
                
                Car found = null;
                for (Car c : DataIO.allCars) {
                    if (c.id.equals(id.trim())) {
                        found = c;
                        break;
                    }
                }
                if (found != null) {
                    String newBrand = JOptionPane.showInputDialog("New brand:", found.brand);
                    if (newBrand != null && !newBrand.trim().isEmpty()) {
                        found.brand = newBrand.trim();
                    }
                    
                    String newModel = JOptionPane.showInputDialog("New model:", found.model);
                    if (newModel != null && !newModel.trim().isEmpty()) {
                        found.model = newModel.trim();
                    }
                    
                    String newPriceStr = JOptionPane.showInputDialog("New price:", found.price);
                    if (newPriceStr != null && !newPriceStr.trim().isEmpty()) {
                        try {
                            double newPrice = Double.parseDouble(newPriceStr);
                            if (newPrice >= 0) {
                                found.price = newPrice;
                            } else {
                                JOptionPane.showMessageDialog(null, "Price cannot be negative!");
                            }
                        } catch (NumberFormatException nfe) {
                            JOptionPane.showMessageDialog(null, "Invalid price format!");
                        }
                    }
                    
                    String newStatus = JOptionPane.showInputDialog("New status:", found.status);
                    if (newStatus != null && !newStatus.trim().isEmpty()) {
                        found.status = newStatus.trim();
                    }
                    
                    JOptionPane.showMessageDialog(null, "Car updated successfully.");
                    DataIO.writeToFile();
                } else {
                    JOptionPane.showMessageDialog(null, "Car with ID '" + id + "' not found.");
                }

            } else if (e.getSource() == delete) {
                String id = JOptionPane.showInputDialog("Enter Car ID to delete:");
                if (id == null || id.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Car ID cannot be empty!");
                    return;
                }
                
                // Check if Car ID contains only numbers
                try {
                    Integer.parseInt(id.trim());
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Car ID must contain only numbers!");
                    return;
                }
                
                Car toRemove = null;
                for (Car c : DataIO.allCars) {
                    if (c.id.equals(id.trim())) {
                        toRemove = c;
                        break;
                    }
                }
                if (toRemove != null) {
                    DataIO.allCars.remove(toRemove);
                    JOptionPane.showMessageDialog(null, "Car deleted successfully.");
                    DataIO.writeToFile();
                } else {
                    JOptionPane.showMessageDialog(null, "Car with ID '" + id + "' not found.");
                }

            } else if (e.getSource() == search) {
                String id = JOptionPane.showInputDialog("Enter Car ID to search:");
                if (id == null || id.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Car ID cannot be empty!");
                    return;
                }
                
                // Check if Car ID contains only numbers
                try {
                    Integer.parseInt(id.trim());
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Car ID must contain only numbers!");
                    return;
                }
                
                Car found = null;
                for (Car c : DataIO.allCars) {
                    if (c.id.equals(id.trim())) {
                        found = c;
                        break;
                    }
                }
                if (found != null) {
                    JOptionPane.showMessageDialog(null, 
                        "Brand: " + found.brand + 
                        "\nModel: " + found.model + 
                        "\nPrice: RM" + found.price + 
                        "\nStatus: " + found.status);
                } else {
                    JOptionPane.showMessageDialog(null, "Car not found.");
                }

            } else if (e.getSource() == back) {
                x.setVisible(false);
                new Page2Manager(); // Back to dashboard
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage());
        }
    }
}