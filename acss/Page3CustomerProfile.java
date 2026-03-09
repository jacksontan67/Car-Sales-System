package acss;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Page3CustomerProfile implements ActionListener {
    JFrame x;
    Button changePassword, viewProfile, viewAccountStatus, back;

    public Page3CustomerProfile() {
        x = new JFrame("Edit Profile - " + ACSS.currentUser.username);
        x.setSize(350, 150);
        x.setLocation(600, 300);
        x.setLayout(new FlowLayout());

        changePassword = new Button("Change Password");
        viewProfile = new Button("View Profile");
        viewAccountStatus = new Button("Account Status");
        back = new Button("Back");

        changePassword.addActionListener(this);
        viewProfile.addActionListener(this);
        viewAccountStatus.addActionListener(this);
        back.addActionListener(this);

        x.add(changePassword);
        x.add(viewProfile);
        x.add(viewAccountStatus);
        x.add(back);
        x.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == changePassword) {
                String oldPass = JOptionPane.showInputDialog("Enter current password:");
                if (!ACSS.currentUser.password.equals(oldPass)) {
                    JOptionPane.showMessageDialog(null, "Incorrect current password!");
                    return;
                }
                
                String newPass = JOptionPane.showInputDialog("Enter new password:");
                if (newPass == null || newPass.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Password cannot be empty!");
                    return;
                }
                
                String confirmPass = JOptionPane.showInputDialog("Confirm new password:");
                
                if (!newPass.equals(confirmPass)) {
                    JOptionPane.showMessageDialog(null, "Passwords do not match!");
                    return;
                }
                
                ACSS.currentUser.password = newPass;
                
                
                for (Customer c : DataIO.allCustomers) {
                    if (c.username.equals(ACSS.currentUser.username)) {
                        c.password = newPass;
                        break;
                    }
                }
                
                DataIO.writeToFile();
                JOptionPane.showMessageDialog(null, "Password changed successfully!");

            } else if (e.getSource() == viewProfile) {
                Customer currentCustomer = (Customer) ACSS.currentUser;
                
                String approvalStatus = currentCustomer.isApproved ? "APPROVED" : "PENDING APPROVAL";
                String profileInfo = "=== PROFILE INFORMATION ===\n" +
                                   "Username: " + currentCustomer.username + "\n" +
                                   "Role: Customer\n" +
                                   "Account Status: " + approvalStatus + "\n" +
                                   "Total Purchases: " + currentCustomer.purchasedCars.size() + "\n";
                
                if (currentCustomer.purchasedCars.size() > 0) {
                    profileInfo += "\nRecent Purchases:\n";
                    int count = 0;
                    for (Car car : currentCustomer.purchasedCars) {
                        if (count < 3) { // Show only last 3 purchases
                            profileInfo += "- " + car.brand + " " + car.model + " (RM" + car.price + ")\n";
                            count++;
                        }
                    }
                    if (currentCustomer.purchasedCars.size() > 3) {
                        profileInfo += "... and " + (currentCustomer.purchasedCars.size() - 3) + " more";
                    }
                }
                
                JOptionPane.showMessageDialog(null, profileInfo);

            } else if (e.getSource() == viewAccountStatus) {
                Customer currentCustomer = (Customer) ACSS.currentUser;
                
                String statusInfo = "=== ACCOUNT STATUS ===\n";
                statusInfo += "Username: " + currentCustomer.username + "\n";
                statusInfo += "Account Type: Customer\n";
                
                if (currentCustomer.isApproved) {
                    statusInfo += "Status: ✓ APPROVED\n";
                    statusInfo += "You can browse and purchase cars.\n\n";
                    statusInfo += "Account Benefits:\n";
                    statusInfo += "• Browse available cars\n";
                    statusInfo += "• Make purchases\n";
                    statusInfo += "• View purchase history\n";
                    statusInfo += "• Give feedback on purchases\n";
                } else {
                    statusInfo += "Status: ⏳ PENDING APPROVAL\n";
                    statusInfo += "Your account is waiting for manager approval.\n\n";
                    statusInfo += "What you can do:\n";
                    statusInfo += "• Edit your profile\n";
                    statusInfo += "• Wait for approval notification\n\n";
                    statusInfo += "What you cannot do yet:\n";
                    statusInfo += "• Browse cars for purchase\n";
                    statusInfo += "• Make purchases\n";
                }
                
                statusInfo += "\nTotal Purchases Made: " + currentCustomer.purchasedCars.size();
                
                JOptionPane.showMessageDialog(null, statusInfo);

            } else if (e.getSource() == back) {
                x.setVisible(false);
                new Page2Customer(); // Return to customer dashboard
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Invalid input or operation failed.");
        }
    }
}