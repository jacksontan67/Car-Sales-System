package acss;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Page1 implements ActionListener {
    JFrame x;
    Button register, login, stop;

    public Page1() {
        x = new JFrame("ACSS Login");
        x.setSize(300, 150);
        x.setLocation(500, 250);
        x.setLayout(new FlowLayout());

        register = new Button("Register");
        login = new Button("Login");
        stop = new Button("Exit");

        register.addActionListener(this);
        login.addActionListener(this);
        stop.addActionListener(this);

        x.add(register);
        x.add(login);
        x.add(stop);
        x.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == stop) {
                int confirm = JOptionPane.showConfirmDialog(null, "Exit the system?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    DataIO.writeToFile();
                    System.exit(0);
                }

            } else if (e.getSource() == register) {
                String name = JOptionPane.showInputDialog("Enter your username:");
                if (DataIO.searchCustomer(name) != null) {
                    throw new Exception();
                }
                String password = JOptionPane.showInputDialog("Enter your password:");
                Customer c = new Customer(name, password, false);
                DataIO.allCustomers.add(c);
                JOptionPane.showMessageDialog(null, "Registration successful!\nPlease wait for manager approval.");
                DataIO.writeToFile();

            } else if (e.getSource() == login) {
                String name = JOptionPane.showInputDialog("Enter your username:");
                String password = JOptionPane.showInputDialog("Enter your password:");

                // Manager login
                for (Manager m : DataIO.allManagers) {
                    if (m.username.equals(name) && m.password.equals(password)) {
                        ACSS.currentUser = m;
                        x.setVisible(false);
                        new Page2Manager(); // Manager dashboard 
                        return;
                    }
                }

                

                // Salesman login
                for (Salesman s : DataIO.allSalesmen) {
                    if (s.username.equals(name) && s.password.equals(password)) {
                         ACSS.currentUser = s;
                         x.setVisible(false);
                         new Page2Salesman(); 
                         return;
    }
}

                // Customer login
                for (Customer c : DataIO.allCustomers) {
                    if (c.username.equals(name) && c.password.equals(password)) {
                        if (!c.isApproved) {
                            JOptionPane.showMessageDialog(null, "Your account is not approved yet.");
                            return;
        }
        ACSS.currentUser = c;
        x.setVisible(false);
        new Page2Customer(); // Now this will work!
        return;
    }
}

                // If none match
                JOptionPane.showMessageDialog(null, "Invalid username or password!");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Invalid input or user already exists.");
        }
    }
}






