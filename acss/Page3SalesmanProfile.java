package acss;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Page3SalesmanProfile implements ActionListener {
    JFrame x;
    Button changePassword, viewProfile, back;

    public Page3SalesmanProfile() {
        x = new JFrame("Edit Profile - " + ACSS.currentUser.username);
        x.setSize(350, 150);
        x.setLocation(600, 300);
        x.setLayout(new FlowLayout());

        changePassword = new Button("Change Password");
        viewProfile = new Button("View Profile");
        back = new Button("Back");

        changePassword.addActionListener(this);
        viewProfile.addActionListener(this);
        back.addActionListener(this);

        x.add(changePassword);
        x.add(viewProfile);
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
                String confirmPass = JOptionPane.showInputDialog("Confirm new password:");
                
                if (!newPass.equals(confirmPass)) {
                    JOptionPane.showMessageDialog(null, "Passwords do not match!");
                    return;
                }
                
                ACSS.currentUser.password = newPass;
                
                // Update in data lists
                for (Salesman s : DataIO.allSalesmen) {
                    if (s.username.equals(ACSS.currentUser.username)) {
                        s.password = newPass;
                        break;
                    }
                }
                
                DataIO.writeToFile();
                JOptionPane.showMessageDialog(null, "Password changed successfully!");

            } else if (e.getSource() == viewProfile) {
                Salesman currentSalesman = (Salesman) ACSS.currentUser;
                
                String profileInfo = "=== PROFILE INFORMATION ===\n" +
                                   "Username: " + currentSalesman.username + "\n" +
                                   "Role: Salesman\n" +
                                   "Total Sales: " + currentSalesman.sales.size() + "\n";
                
                JOptionPane.showMessageDialog(null, profileInfo);

            } else if (e.getSource() == back) {
                x.setVisible(false);
                new Page2Salesman(); // Return to dashboard
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Invalid input or operation failed.");
        }
    }
}