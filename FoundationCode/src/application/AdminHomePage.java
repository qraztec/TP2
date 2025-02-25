

package application;

import java.util.ArrayList;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * AdminPage class represents the user interface for the admin user.
 * This page displays a simple welcome message for the admin.
 */

public class AdminHomePage {
	
    public void show(Stage primaryStage, DatabaseHelper databaseHelper, User user) {
    	VBox layout = new VBox();
    	
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // label to display the welcome message for the admin
	    Label adminLabel = new Label("Hello, Admin!");
	    
	    adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	 // label for text after delete button
	    Label deleteLabel = new Label();
	    deleteLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
	    
	 // TextField to enter user name to delete User
	    TextField deleteUser = new TextField(); 
	    deleteUser.setMaxWidth(250);
	    deleteUser.setPromptText("Enter Username to Delete");
	    
	 // Button to find and delete user
	    Button deleteButton = new Button("Delete User"); 
	    deleteButton.setOnAction(e -> {
	    	String userName = deleteUser.getText();
	    	if (databaseHelper.doesUserExist(userName)) { // User found, delete user
	    		// deleteLabel.setText("Are you sure you want to delete " + userName); 
	    		// Not sure if we want to just delete user after button is pressed
	    		databaseHelper.deleteUser(userName); // deletes User
	    		deleteLabel.setText(userName + " was successfully deleted");
	    		deleteUser.clear(); // clear TextField
	    	}
	    	else if (databaseHelper.getUserRole(userName) == "Admin") { // admin user found, cannot delete
	    		deleteLabel.setText("Cannot delete admin");
	    	}
	    	else // User is not found
	    		deleteLabel.setText("User not Found");
	    });
	    
	 // Button to list all users in database
	    Button listUsersButton = new Button("List Users");
	    listUsersButton.setOnAction(e -> {
	    	ArrayList<String[]> userList = databaseHelper.listUsers(); // prints list of users
	    	new UserList().show(primaryStage, databaseHelper, userList);
	    });
	    
	    
	    layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
	    layout.getChildren().addAll(adminLabel, deleteLabel, deleteUser, deleteButton, listUsersButton); // add all
	    Scene adminScene = new Scene(layout, 800, 400);

	 // Logout button to return to login page
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> new UserLoginPage(databaseHelper).show(primaryStage));

        // Switch Role button (Only shown if user has multiple roles)
        Button switchRoleButton = new Button("Switch Role");
        switchRoleButton.setOnAction(e -> new WelcomeLoginPage(databaseHelper).show(primaryStage, user));
        switchRoleButton.setVisible(user.getRole().contains(",")); // Show only if multiple roles

        layout.getChildren().addAll(logoutButton, switchRoleButton);
	    
	    // Set the scene to primary stage
	    primaryStage.setScene(adminScene);
	    primaryStage.setTitle("Admin Page");
	    
	    Button inviteButton = new Button("Invite");
		inviteButton.setOnAction(a -> {
			new InvitationPage().show(databaseHelper, primaryStage);
		});
		layout.getChildren().add(inviteButton);
	   
        Button OTPButton = new Button("OTP");
            OTPButton.setOnAction(a -> {
                new OTP_PAGE().show(databaseHelper, primaryStage);
            });
            layout.getChildren().add(OTPButton);
	    
    }
}