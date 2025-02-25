package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import databasePart1.*;

/**
 * SetupAccountPage class handles the account setup process for new users.
 * Users provide their userName, password, and a valid invitation code to register.
 */
public class SetupAccountPage {
	
    private final DatabaseHelper databaseHelper;
    // DatabaseHelper to handle database operations.
    public SetupAccountPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the Setup Account page in the provided stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
    	// Input fields for userName, password, and invitation code
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
        TextField inviteCodeField = new TextField();
        inviteCodeField.setPromptText("Enter InvitationCode");
        inviteCodeField.setMaxWidth(250);
        
        // Role Boxes (A.P.)
        CheckBox adminRole = new CheckBox("Admin");
        CheckBox studentRole = new CheckBox("Student");
        CheckBox instructorRole = new CheckBox("Instructor");
        CheckBox staffRole = new CheckBox("Staff");
        CheckBox reviewerRole = new CheckBox("Reviewer");
        
        // Label to display error messages for invalid input or registration issues
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        Button setupButton = new Button("Setup");
        
        setupButton.setOnAction(a -> {
        	// Retrieve user input
            String userName = userNameField.getText();
            String password = passwordField.getText();
            String code = inviteCodeField.getText();
            
            // adds a role to the users account (A.P.)
            List<String> selectedRoles = new ArrayList<>();
            if (adminRole.isSelected()) selectedRoles.add("Admin");
            if (studentRole.isSelected()) selectedRoles.add("Student");
            if (instructorRole.isSelected()) selectedRoles.add("Instructor");
            if (staffRole.isSelected()) selectedRoles.add("Staff");
            if (reviewerRole.isSelected()) selectedRoles.add("Reviewer");
            
            if (selectedRoles.isEmpty()) {
            	errorLabel.setText("Please select a role");
            	return;
            }
            
            String roles = String.join(",", selectedRoles); //Stores roles in the database (A.P.)
            
            try {
            	// Check if the user already exists
            	if(!databaseHelper.doesUserExist(userName)) {
            		
            		// Validate the invitation code
            		if(databaseHelper.validateInvitationCode(code)) {
            			
            			// Create a new user and register them in the database
            			// added the role for the user (A.P.)
		            	User user=new User(userName, password, roles);
		                databaseHelper.register(user);
		                
		             // Navigate to the Welcome Login Page
		                new WelcomeLoginPage(databaseHelper).show(primaryStage,user);
            		}
            		else {
            			errorLabel.setText("Please enter a valid invitation code");
            		}
            	}
            	else {
            		errorLabel.setText("This useruserName is taken!!.. Please use another to setup an account");
            	}
            	
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(userNameField, passwordField, inviteCodeField, setupButton, errorLabel, 
        		adminRole, studentRole, instructorRole, staffRole, reviewerRole); //added the role boxes (A.P.)

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Account Setup");
        primaryStage.show();
    }
}