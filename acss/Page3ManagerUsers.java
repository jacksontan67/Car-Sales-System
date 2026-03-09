package acss;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Page3ManagerUsers implements ActionListener {
    JFrame x;
    Button addManager, deleteManager, searchManager, updateManager;
    Button addSalesman, deleteSalesman, searchSalesman, updateSalesman;
    Button approveCustomer, deleteCustomer, searchCustomer, updateCustomer, back;

    public Page3ManagerUsers() {
        x = new JFrame("Manage Users");
        x.setSize(550, 350);
        x.setLocation(600, 300);
        x.setLayout(new FlowLayout());

        // Manager operations
        addManager = new Button("Add Manager");
        deleteManager = new Button("Delete Manager");
        searchManager = new Button("Search Manager");
        updateManager = new Button("Update Manager");
        
        // Salesman operations
        addSalesman = new Button("Add Salesman");
        deleteSalesman = new Button("Delete Salesman");
        searchSalesman = new Button("Search Salesman");
        updateSalesman = new Button("Update Salesman");
        
        // Customer operations 
        approveCustomer = new Button("Approve Customer");
        deleteCustomer = new Button("Delete Customer");
        searchCustomer = new Button("Search Customer");
        updateCustomer = new Button("Update Customer");
        back = new Button("Back");

        // Add action listeners
        addManager.addActionListener(this);
        deleteManager.addActionListener(this);
        searchManager.addActionListener(this);
        updateManager.addActionListener(this);
        addSalesman.addActionListener(this);
        deleteSalesman.addActionListener(this);
        searchSalesman.addActionListener(this);
        updateSalesman.addActionListener(this);
        approveCustomer.addActionListener(this);
        deleteCustomer.addActionListener(this);
        searchCustomer.addActionListener(this);
        updateCustomer.addActionListener(this);
        back.addActionListener(this);

        // Add components to frame
        x.add(addManager);
        x.add(deleteManager);
        x.add(searchManager);
        x.add(updateManager);
        x.add(addSalesman);
        x.add(deleteSalesman);
        x.add(searchSalesman);
        x.add(updateSalesman);
        x.add(approveCustomer);
        x.add(deleteCustomer);
        x.add(searchCustomer);
        x.add(updateCustomer);
        x.add(back);
        x.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // MANAGER OPERATIONS 
            if (e.getSource() == addManager) {
                String name = JOptionPane.showInputDialog("Enter new manager username:");
                //Check for null
                if (name == null) {
                    return; // User cancelled
                }
                if (name.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username cannot be empty!");
                    return;
                }
                
                // Check if manager already exists
                for (Manager m : DataIO.allManagers) {
                    if (m.username.equalsIgnoreCase(name.trim())) {
                        JOptionPane.showMessageDialog(null, "Manager with this username already exists!");
                        return;
                    }
                }
                
                String pass = JOptionPane.showInputDialog("Enter password:");
                // Check for empty password
                if (pass == null) {
                    return; 
                }
                if (pass.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Password cannot be empty!");
                    return;
                }
                
                DataIO.allManagers.add(new Manager(name.trim(), pass.trim()));
                JOptionPane.showMessageDialog(null, "Manager '" + name.trim() + "' added successfully.");
                DataIO.writeToFile();

            } else if (e.getSource() == deleteManager) {
                if (DataIO.allManagers.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No managers in the system.");
                    return;
                }
                
                String name = JOptionPane.showInputDialog("Enter manager username to delete:");
                if (name == null) return; 
                if (name.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username cannot be empty!");
                    return;
                }
                
                Manager toRemove = null;
                for (Manager m : DataIO.allManagers) {
                    if (m.username.equalsIgnoreCase(name.trim())) {
                        toRemove = m;
                        break;
                    }
                }
                if (toRemove != null) {
                    DataIO.allManagers.remove(toRemove);
                    JOptionPane.showMessageDialog(null, "Manager '" + name + "' deleted successfully.");
                    DataIO.writeToFile();
                } else {
                    JOptionPane.showMessageDialog(null, "Manager '" + name + "' not found.");
                }

            } else if (e.getSource() == searchManager) {
                if (DataIO.allManagers.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No managers in the system.");
                    return;
                }
                
                String name = JOptionPane.showInputDialog("Enter manager username to search:");
                if (name == null) return; 
                if (name.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username cannot be empty!");
                    return;
                }
                
                Manager found = null;
                for (Manager m : DataIO.allManagers) {
                    if (m.username.equalsIgnoreCase(name.trim())) {
                        found = m;
                        break;
                    }
                }
                
                if (found != null) {
                    String managerInfo = "=== MANAGER FOUND ===\n" +
                                       "Username: " + found.username + "\n" +
                                       "Role: Manager\n" +
                                       "Password: " + found.password + "\n" +
                                       "Status: Active";
                    JOptionPane.showMessageDialog(null, managerInfo);
                } else {
                    JOptionPane.showMessageDialog(null, "Manager '" + name + "' not found.");
                }

            } else if (e.getSource() == updateManager) {
                if (DataIO.allManagers.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No managers in the system.");
                    return;
                }
                
                String name = JOptionPane.showInputDialog("Enter manager username to update:");
                if (name == null) return; 
                if (name.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username cannot be empty!");
                    return;
                }
                
                Manager found = null;
                for (Manager m : DataIO.allManagers) {
                    if (m.username.equalsIgnoreCase(name.trim())) {
                        found = m;
                        break;
                    }
                }
                
                if (found != null) {
                    String newUsername = JOptionPane.showInputDialog("Enter new username (current: " + found.username + "):");
                    if (newUsername != null && !newUsername.trim().isEmpty()) {
                        found.username = newUsername.trim();
                    }
                    
                    String newPassword = JOptionPane.showInputDialog("Enter new password (current: " + found.password + "):");
                    if (newPassword != null && !newPassword.trim().isEmpty()) {
                        found.password = newPassword.trim();
                    }
                    
                    JOptionPane.showMessageDialog(null, "Manager information updated successfully.");
                    DataIO.writeToFile();
                } else {
                    JOptionPane.showMessageDialog(null, "Manager '" + name + "' not found.");
                }

            // SALESMAN OPERATIONS 
            } else if (e.getSource() == addSalesman) {
                String name = JOptionPane.showInputDialog("Enter new salesman username:");
                if (name == null) {
                    return; 
                }
                if (name.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username cannot be empty!");
                    return;
                }
                
                // Check if salesman already exists
                for (Salesman s : DataIO.allSalesmen) {
                    if (s.username.equalsIgnoreCase(name.trim())) {
                        JOptionPane.showMessageDialog(null, "Salesman with this username already exists!");
                        return;
                    }
                }               
                String pass = JOptionPane.showInputDialog("Enter password:");            
                if (pass == null) {
                    return; 
                }
                if (pass.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Password cannot be empty!");
                    return;
                }
                
                DataIO.allSalesmen.add(new Salesman(name.trim(), pass.trim()));
                JOptionPane.showMessageDialog(null, "Salesman '" + name.trim() + "' added successfully.");
                DataIO.writeToFile();

            } else if (e.getSource() == deleteSalesman) {
                if (DataIO.allSalesmen.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No salesmen in the system.");
                    return;
                }
                
                String name = JOptionPane.showInputDialog("Enter salesman username to delete:");
                if (name == null) return; 
                if (name.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username cannot be empty!");
                    return;
                }
                
                Salesman toRemove = null;
                for (Salesman s : DataIO.allSalesmen) {
                    if (s.username.equalsIgnoreCase(name.trim())) {
                        toRemove = s;
                        break;
                    }
                }
                if (toRemove != null) {
                    DataIO.allSalesmen.remove(toRemove);
                    JOptionPane.showMessageDialog(null, "Salesman '" + name + "' deleted successfully.");
                    DataIO.writeToFile();
                } else {
                    JOptionPane.showMessageDialog(null, "Salesman '" + name + "' not found.");
                }

            } else if (e.getSource() == searchSalesman) {
                if (DataIO.allSalesmen.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No salesmen in the system.");
                    return;
                }
                
                String name = JOptionPane.showInputDialog("Enter salesman username to search:");
                if (name == null) return; // User cancelled
                if (name.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username cannot be empty!");
                    return;
                }
                
                Salesman found = null;
                for (Salesman s : DataIO.allSalesmen) {
                    if (s.username.equalsIgnoreCase(name.trim())) {
                        found = s;
                        break;
                    }
                }
                
                if (found != null) {
                    String salesmanInfo = "=== SALESMAN FOUND ===\n" +
                                        "Username: " + found.username + "\n" +
                                        "Role: Salesman\n" +
                                        "Password: " + found.password + "\n" +
                                        "Total Sales: " + found.sales.size() + "\n" +
                                        "Status: Active";
                    JOptionPane.showMessageDialog(null, salesmanInfo);
                } else {
                    JOptionPane.showMessageDialog(null, "Salesman '" + name + "' not found.");
                }

            } else if (e.getSource() == updateSalesman) {
                if (DataIO.allSalesmen.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No salesmen in the system.");
                    return;
                }
                
                String name = JOptionPane.showInputDialog("Enter salesman username to update:");
                if (name == null) return; 
                if (name.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username cannot be empty!");
                    return;
                }
                
                Salesman found = null;
                for (Salesman s : DataIO.allSalesmen) {
                    if (s.username.equalsIgnoreCase(name.trim())) {
                        found = s;
                        break;
                    }
                }
                
                if (found != null) {
                    String newUsername = JOptionPane.showInputDialog("Enter new username (current: " + found.username + "):");
                    if (newUsername != null && !newUsername.trim().isEmpty()) {
                        found.username = newUsername.trim();
                    }
                    
                    String newPassword = JOptionPane.showInputDialog("Enter new password (current: " + found.password + "):");
                    if (newPassword != null && !newPassword.trim().isEmpty()) {
                        found.password = newPassword.trim();
                    }
                    
                    JOptionPane.showMessageDialog(null, "Salesman information updated successfully.");
                    DataIO.writeToFile();
                } else {
                    JOptionPane.showMessageDialog(null, "Salesman '" + name + "' not found.");
                }

            // CUSTOMER OPERATIONS 
            } else if (e.getSource() == approveCustomer) {
                String name = JOptionPane.showInputDialog("Enter customer username to approve:");
                if (name == null) return; 
                if (name.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username cannot be empty!");
                    return;
                }
                
                Customer c = DataIO.searchCustomer(name.trim());
                if (c != null) {
                    if (c.isApproved) {
                        JOptionPane.showMessageDialog(null, "Customer '" + name + "' is already approved.");
                    } else {
                        c.isApproved = true;
                        JOptionPane.showMessageDialog(null, "Customer '" + name + "' approved successfully.");
                        DataIO.writeToFile();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Customer '" + name + "' not found.");
                }

            } else if (e.getSource() == deleteCustomer) {
                String name = JOptionPane.showInputDialog("Enter customer username to delete:");
                if (name == null) return; 
                if (name.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username cannot be empty!");
                    return;
                }
                
                Customer toRemove = DataIO.searchCustomer(name.trim());
                if (toRemove != null) {
                    DataIO.allCustomers.remove(toRemove);
                    JOptionPane.showMessageDialog(null, "Customer '" + name + "' deleted successfully.");
                    DataIO.writeToFile();
                } else {
                    JOptionPane.showMessageDialog(null, "Customer '" + name + "' not found.");
                }

            //SEARCH CUSTOMER FUNCTIONALITY 
            } else if (e.getSource() == searchCustomer) {
                if (DataIO.allCustomers.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No customers in the system.");
                    return;
                }
                
                String name = JOptionPane.showInputDialog("Enter customer username to search:");
                if (name == null) return; // User cancelled
                if (name.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username cannot be empty!");
                    return;
                }
                
                Customer found = DataIO.searchCustomer(name.trim());
                
                if (found != null) {
                    String approvalStatus = found.isApproved ? "APPROVED" : "PENDING APPROVAL";
                    String customerInfo = "=== CUSTOMER FOUND ===\n" +
                                        "Username: " + found.username + "\n" +
                                        "Role: Customer\n" +
                                        "Password: " + found.password + "\n" +
                                        "Approval Status: " + approvalStatus + "\n" +
                                        "Total Purchases: " + found.purchasedCars.size() + "\n" +
                                        "Status: Active";
                    
                    if (found.purchasedCars.size() > 0) {
                        customerInfo += "\n\nRecent Purchases:";
                        for (int i = 0; i < Math.min(3, found.purchasedCars.size()); i++) {
                            Car car = found.purchasedCars.get(i);
                            customerInfo += "\n- " + car.brand + " " + car.model + " (RM" + car.price + ")";
                        }
                        if (found.purchasedCars.size() > 3) {
                            customerInfo += "\n... and " + (found.purchasedCars.size() - 3) + " more";
                        }
                    }
                    
                    JOptionPane.showMessageDialog(null, customerInfo);
                } else {
                    JOptionPane.showMessageDialog(null, "Customer '" + name + "' not found.");
                }

            // UPDATE CUSTOMER FUNCTIONALITY
            } else if (e.getSource() == updateCustomer) {
                if (DataIO.allCustomers.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No customers in the system.");
                    return;
                }
                
                String name = JOptionPane.showInputDialog("Enter customer username to update:");
                if (name == null) return; // User cancelled
                if (name.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username cannot be empty!");
                    return;
                }
                
                Customer found = DataIO.searchCustomer(name.trim());
                
                if (found != null) {
                    // Show current customer info
                    String currentInfo = "Current Customer Information:\n" +
                                       "Username: " + found.username + "\n" +
                                       "Password: " + found.password + "\n" +
                                       "Approval Status: " + (found.isApproved ? "APPROVED" : "PENDING") + "\n" +
                                       "Total Purchases: " + found.purchasedCars.size();
                    
                    JOptionPane.showMessageDialog(null, currentInfo);
                    
                    // Update username
                    String newUsername = JOptionPane.showInputDialog("Enter new username (current: " + found.username + "):");
                    if (newUsername != null && !newUsername.trim().isEmpty()) {
                        // Check if new username already exists
                        boolean usernameExists = false;
                        for (Customer c : DataIO.allCustomers) {
                            if (!c.equals(found) && c.username.equalsIgnoreCase(newUsername.trim())) {
                                usernameExists = true;
                                break;
                            }
                        }
                        
                        if (usernameExists) {
                            JOptionPane.showMessageDialog(null, "Username '" + newUsername + "' already exists!");
                        } else {
                            found.username = newUsername.trim();
                        }
                    }
                    
                    // Update password
                    String newPassword = JOptionPane.showInputDialog("Enter new password (current: " + found.password + "):");
                    if (newPassword != null && !newPassword.trim().isEmpty()) {
                        found.password = newPassword.trim();
                    }
                    
                    // Update approval status
                    String[] approvalOptions = {"APPROVED", "PENDING"};
                    String currentStatus = found.isApproved ? "APPROVED" : "PENDING";
                    String newApprovalStatus = (String) JOptionPane.showInputDialog(null,
                        "Select approval status (current: " + currentStatus + "):",
                        "Update Approval Status",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        approvalOptions,
                        currentStatus);
                    
                    if (newApprovalStatus != null) {
                        found.isApproved = newApprovalStatus.equals("APPROVED");
                    }
                    
                    JOptionPane.showMessageDialog(null, "Customer '" + found.username + "' information updated successfully.");
                    DataIO.writeToFile();
                } else {
                    JOptionPane.showMessageDialog(null, "Customer '" + name + "' not found.");
                }

            } else if (e.getSource() == back) {
                x.setVisible(false);
                new Page2Manager(); // Return to dashboard
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Invalid input or operation failed: " + ex.getMessage());
        }
    }
}