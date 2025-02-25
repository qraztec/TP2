//package application;
//
//import java.util.ArrayList;
//
//import databasePart1.DatabaseHelper;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//
///**
// * This page displays a simple welcome message for the user.
// */
//
//public class UserList {
//
//    public void show(Stage primaryStage, DatabaseHelper databaseHelper, ArrayList<String[]> listOfUsers) {
//    	VBox layout = new VBox();
//	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
//	    
//	    Label titleLabel = new Label("List of Users:");
//        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
//
//        layout.getChildren().add(titleLabel);
//        
//	    for (String[] user : listOfUsers) {
//            if (user.length == 2) { // Ensure the array has exactly 2 elements
//                String userName = user[0];
//                String role = user[1];
//
//             // Create labels for username and role
//                Label userLabel = new Label("Username: " + userName);
//                Label roleLabel = new Label(" | Role: " + role);
//
//                // Create Add and Remove buttons
//                Button addButton = new Button("Add");
//                Button removeButton = new Button("Remove");
//
//                // Set button actions (you can replace these with actual logic)
//                addButton.setOnAction(e -> System.out.println("Adding: " + userName));
//                removeButton.setOnAction(e -> System.out.println("Removing: " + userName));
//
//                // Create an HBox row for the user
//                HBox userRow = new HBox(10, userLabel, roleLabel, addButton, removeButton);
//                userRow.setStyle("-fx-padding: 5;");
//
//                layout.getChildren().add(userRow);
//            }
//        }
//	    
//	   
//	    Scene userScene = new Scene(layout, 800, 400);
//
//	    // Set the scene to primary stage
//	    primaryStage.setScene(userScene);
//	    primaryStage.setTitle("User List");
//    	
//    }
//}

package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserList {

    public void show(Stage primaryStage, DatabaseHelper databaseHelper, ArrayList<String[]> listOfUsers) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label titleLabel = new Label("List of Users:");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        layout.getChildren().add(titleLabel);

        List<String> validRoles = Arrays.asList("Admin", "Student", "Instructor", "Staff", "Reviewer");

        for (String[] user : listOfUsers) {
            if (user.length == 2) {
                String userName = user[0];
                String role = user[1];

                // Labels for username and role
                Label userLabel = new Label("Username: " + userName);
                Label roleLabel = new Label(" | Role: " + role);

                // TextField to enter role for modification
                TextField roleInput = new TextField();
                roleInput.setPromptText("Enter role");
                roleInput.setMaxWidth(100);

                // Buttons for modifying roles
                Button addButton = new Button("Add");
                Button removeButton = new Button("Remove");

                // Add role logic
                addButton.setOnAction(e -> {
                    String newRole = roleInput.getText().trim();
                    if (!validRoles.contains(newRole)) {
                        showAlert("Invalid Role", "Valid roles: " + validRoles);
                    } else if (databaseHelper.userHasRole(userName, newRole)) {
                        showAlert("Error", userName + " already has the " + newRole + " role.");
                    } else {
                        databaseHelper.addUserRole(userName, newRole);
                        showAlert("Success", newRole + " role added to " + userName);
                    }
                });

                // Remove role logic
                removeButton.setOnAction(e -> {
                    String removeRole = roleInput.getText().trim();
                    if (!validRoles.contains(removeRole)) {
                        showAlert("Invalid Role", "Valid roles: " + validRoles);
                    } else if (!databaseHelper.userHasRole(userName, removeRole)) {
                        showAlert("Error", userName + " does not have the " + removeRole + " role.");
                    } else if (removeRole.equals("Admin") && databaseHelper.isLastAdmin(userName)) {
                        showAlert("Error", "Cannot remove the last Admin!");
                    } else {
                        databaseHelper.removeUserRole(userName, removeRole);
                        showAlert("Success", removeRole + " role removed from " + userName);
                    }
                });

                // Layout for each user entry
                HBox userRow = new HBox(10, userLabel, roleLabel, roleInput, addButton, removeButton);
                userRow.setStyle("-fx-padding: 5;");

                layout.getChildren().add(userRow);
            }
        }

        Scene userScene = new Scene(layout, 800, 400);
        primaryStage.setScene(userScene);
        primaryStage.setTitle("User List");
    }

    // Helper function to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
