package acss;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Page5CustomerFeedback implements ActionListener {
    JFrame x;
    Button giveFeedback, viewMyFeedback, back;

    public Page5CustomerFeedback() {
        x = new JFrame("Feedback & Ratings");
        x.setSize(350, 150);
        x.setLocation(600, 300);
        x.setLayout(new FlowLayout());

        giveFeedback = new Button("Give Feedback");
        viewMyFeedback = new Button("View My Feedback");
        back = new Button("Back");

        giveFeedback.addActionListener(this);
        viewMyFeedback.addActionListener(this);
        back.addActionListener(this);

        x.add(giveFeedback);
        x.add(viewMyFeedback);
        x.add(back);
        x.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Customer currentCustomer = (Customer) ACSS.currentUser;

            if (e.getSource() == giveFeedback) {
                if (currentCustomer.purchasedCars.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "You have no purchases yet. Buy a car first to give feedback!");
                    return;
                }
                
                // Show purchased cars
                StringBuilder purchasedList = new StringBuilder("Your Purchased Cars:\n\n");
                for (int i = 0; i < currentCustomer.purchasedCars.size(); i++) {
                    Car car = currentCustomer.purchasedCars.get(i);
                    purchasedList.append((i + 1)).append(". ").append(car.brand)
                                .append(" ").append(car.model)
                                .append(" (ID: ").append(car.id).append(")")
                                .append(" - RM").append(car.price).append("\n");
                }
                
                JOptionPane.showMessageDialog(null, purchasedList.toString());
                
                // Get car ID for feedback
                String carId = JOptionPane.showInputDialog("Enter Car ID to give feedback:");
                if (carId == null || carId.trim().isEmpty()) {
                    return;
                }
                
                // Check if customer owns this car
                Car feedbackCar = null;
                for (Car car : currentCustomer.purchasedCars) {
                    if (car.id.equalsIgnoreCase(carId.trim())) {
                        feedbackCar = car;
                        break;
                    }
                }
                
                if (feedbackCar == null) {
                    JOptionPane.showMessageDialog(null, "You can only give feedback on cars you have purchased!");
                    return;
                }
                
                // Get rating
                String[] ratingOptions = {"5 - Excellent", "4 - Very Good", "3 - Good", "2 - Fair", "1 - Poor"};
                String selectedRating = (String) JOptionPane.showInputDialog(null, 
                    "Rate your purchase: " + feedbackCar.brand + " " + feedbackCar.model, 
                    "Rate Purchase", 
                    JOptionPane.QUESTION_MESSAGE, 
                    null, 
                    ratingOptions, 
                    ratingOptions[0]);
                
                if (selectedRating == null) return;
                
                int rating = Integer.parseInt(selectedRating.substring(0, 1));
                
                // Get comment
                String comment = JOptionPane.showInputDialog("Add your comment (optional):");
                if (comment == null) comment = "No comment";
                
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                
                // Create and save feedback
                Feedback newFeedback = new Feedback(
                    currentCustomer.username,
                    feedbackCar.id,
                    feedbackCar.brand,
                    feedbackCar.model,
                    rating,
                    comment,
                    timestamp
                );
                
                DataIO.allFeedbacks.add(newFeedback);
                DataIO.writeToFile();
                
                String confirmMsg = "Feedback Submitted Successfully!\n\n" +
                                  "Car: " + feedbackCar.brand + " " + feedbackCar.model + "\n" +
                                  "Rating: " + rating + "/5 stars\n" +
                                  "Comment: " + comment + "\n" +
                                  "Date: " + timestamp + "\n\n" +
                                  "Thank you for your valuable feedback!";
                
                JOptionPane.showMessageDialog(null, confirmMsg);

            } else if (e.getSource() == viewMyFeedback) {
                // Show feedbacks given by current customer
                StringBuilder myFeedbacks = new StringBuilder("=== MY FEEDBACK HISTORY ===\n");
                myFeedbacks.append("Customer: ").append(currentCustomer.username).append("\n\n");
                
                boolean foundFeedback = false;
                for (Feedback feedback : DataIO.allFeedbacks) {
                    if (feedback.customerUsername.equals(currentCustomer.username)) {
                        myFeedbacks.append("Car: ").append(feedback.carBrand).append(" ").append(feedback.carModel).append("\n");
                        myFeedbacks.append("Rating: ").append(feedback.rating).append("/5 stars\n");
                        myFeedbacks.append("Comment: ").append(feedback.comment).append("\n");
                        myFeedbacks.append("Date: ").append(feedback.date).append("\n");
                        myFeedbacks.append("-----------------------------------\n");
                        foundFeedback = true;
                    }
                }
                
                if (!foundFeedback) {
                    myFeedbacks.append("You haven't given any feedback yet.\n");
                    myFeedbacks.append("Purchase a car and give feedback to help us improve!");
                }
                
                JTextArea textArea = new JTextArea(myFeedbacks.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(450, 300));
                JOptionPane.showMessageDialog(null, scrollPane, "My Feedback History", JOptionPane.INFORMATION_MESSAGE);

            } else if (e.getSource() == back) {
                x.setVisible(false);
                new Page2Customer(); // Return to customer dashboard
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error occurred: " + ex.getMessage());
        }
    }
}