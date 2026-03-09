package acss;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Page2Manager implements ActionListener {
    JFrame x;
    Button manageUsers, manageCars, viewReports, logout;

    public Page2Manager() {
        x = new JFrame("Manager Dashboard");
        x.setSize(350, 150);
        x.setLocation(600, 300);
        x.setLayout(new FlowLayout());

        manageUsers = new Button("Manage Users");
        manageCars = new Button("Manage Cars");
        viewReports = new Button("View Reports");
        logout = new Button("Logout");

        manageUsers.addActionListener(this);
        manageCars.addActionListener(this);
        viewReports.addActionListener(this);
        logout.addActionListener(this);

        x.add(manageUsers);
        x.add(manageCars);
        x.add(viewReports);
        x.add(logout);
        x.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == manageUsers) {
                x.setVisible(false);
                new Page3ManagerUsers(); 
            } else if (e.getSource() == manageCars) {
                x.setVisible(false);
                new Page4ManagerCars(); 
            } else if (e.getSource() == viewReports) {
                x.setVisible(false);
                new Page5ManagerReports(); 
            } else if (e.getSource() == logout) {
                ACSS.currentUser = null;
                x.setVisible(false);
                ACSS.firstPage.x.setVisible(true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Something went wrong!");
        }
    }
}
