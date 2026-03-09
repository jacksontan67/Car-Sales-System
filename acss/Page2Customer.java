package acss;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Page2Customer implements ActionListener {
    JFrame x;
    Button editProfile, viewAvailableCars, giveFeedback, viewPurchaseHistory, logout;

    public Page2Customer() {
        x = new JFrame("Customer Dashboard - " + ACSS.currentUser.username);
        x.setSize(400, 180);
        x.setLocation(600, 300);
        x.setLayout(new FlowLayout());

        editProfile = new Button("Edit Profile");
        viewAvailableCars = new Button("View Available Cars");
        giveFeedback = new Button("Give Feedback");
        viewPurchaseHistory = new Button("Purchase History");
        logout = new Button("Logout");

        editProfile.addActionListener(this);
        viewAvailableCars.addActionListener(this);
        giveFeedback.addActionListener(this);
        viewPurchaseHistory.addActionListener(this);
        logout.addActionListener(this);

        x.add(editProfile);
        x.add(viewAvailableCars);
        x.add(giveFeedback);
        x.add(viewPurchaseHistory);
        x.add(logout);
        x.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == editProfile) {
                x.setVisible(false);
                new Page3CustomerProfile(); 

            } else if (e.getSource() == viewAvailableCars) {
                x.setVisible(false);
                new Page4CustomerViewCars(); 

            } else if (e.getSource() == giveFeedback) {
                x.setVisible(false);
                new Page5CustomerFeedback(); 

            } else if (e.getSource() == viewPurchaseHistory) {
                x.setVisible(false);
                new Page6CustomerHistory(); 

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


