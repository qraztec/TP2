package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This page displays a simple welcome message for the user.
 */

public class StaffHomePage {

    public void show(Stage primaryStage, DatabaseHelper databaseHelper, User user) {
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display Hello user
	    Label userLabel = new Label("Hello, Staff!");
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

	    layout.getChildren().add(userLabel);
	    
	 // Logout button to return to login page
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> new UserLoginPage(databaseHelper).show(primaryStage));

        // Switch Role button (Only shown if user has multiple roles)
        Button switchRoleButton = new Button("Switch Role");
        switchRoleButton.setOnAction(e -> new WelcomeLoginPage(databaseHelper).show(primaryStage, user));
        switchRoleButton.setVisible(user.getRole().contains(",")); // Show only if multiple roles

        layout.getChildren().addAll(logoutButton, switchRoleButton);
	   
	    Scene userScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("User Page");
    	
    }
}