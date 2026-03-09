package acss;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Page2Salesman implements ActionListener {
    JFrame x;
    Button editProfile, viewCars, updateCarStatus, collectPayment, viewSalesRecords, logout;

    public Page2Salesman() {
        x = new JFrame("Salesman Dashboard - " + ACSS.currentUser.username);
        x.setSize(400, 200);
        x.setLocation(600, 300);
        x.setLayout(new FlowLayout());

        editProfile = new Button("Edit Profile");
        viewCars = new Button("View Cars");
        updateCarStatus = new Button("Update Car Status");
        collectPayment = new Button("Collect Payment");
        viewSalesRecords = new Button("View Sales Records");
        logout = new Button("Logout");

        editProfile.addActionListener(this);
        viewCars.addActionListener(this);
        updateCarStatus.addActionListener(this);
        collectPayment.addActionListener(this);
        viewSalesRecords.addActionListener(this);
        logout.addActionListener(this);

        x.add(editProfile);
        x.add(viewCars);
        x.add(updateCarStatus);
        x.add(collectPayment);
        x.add(viewSalesRecords);
        x.add(logout);
        x.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == editProfile) {
                x.setVisible(false);
                new Page3SalesmanProfile(); 

            } else if (e.getSource() == viewCars) {
                x.setVisible(false);
                new Page4SalesmanViewCars(); 

            } else if (e.getSource() == updateCarStatus) {
                x.setVisible(false);
                new Page5SalesmanUpdateStatus(); 

            } else if (e.getSource() == collectPayment) {
                x.setVisible(false);
                new Page6SalesmanPayment(); 

            } else if (e.getSource() == viewSalesRecords) {
                x.setVisible(false);
                new Page7SalesmanRecords(); 

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