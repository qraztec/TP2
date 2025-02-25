package application;
import java.util.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
//import passwordEvaluationTestbed.PasswordEvaluator;

import java.sql.SQLException;

import databasePart1.*;

/**
 * The SetupAdmin class handles the setup process for creating an administrator account.
 * This is intended to be used by the first user to initialize the system with admin credentials.
 */
public class AdminSetupPage {
	
    private final DatabaseHelper databaseHelper;

    public AdminSetupPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
    	// Input fields for userName and password
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter Admin userName");
        userNameField.setMaxWidth(250);
        //Text field for now instead of password field
        TextField passwordField = new TextField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        Label label_Password = new Label("Password doesn't match requirements");
        label_Password.setStyle("-fx-text-fill: red;");
        label_Password.setVisible(false);
        Label label_UserName = new Label("UserName doesn't match requirements");
        label_UserName.setStyle("-fx-text-fill: red;");
        label_UserName.setVisible(false);
        Button setupButton = new Button("Setup");
        
        setupButton.setOnAction(a -> {
        	// Retrieve user input
            String userName = userNameField.getText();
            //check if valid username from usernamerecognizer class
            
            String errName = UserNameRecognizer.checkForValidUserName(userName);
            
            //Checks to keep track if password and name are valid
            Boolean nameCheck = false;
            Boolean passCheck = false;
            
            if (errName != "") {
            	nameCheck = true;
            }
            
            
            //Check if valid password by seeing if it has error message
            String password = passwordField.getText();
            String errPass = PasswordEvaluator.evaluatePassword(password);
            if (errPass != "") {
            	passCheck = true;
            }
            try {
            	//If namecheck and passcheck meet requirements, then admin gets registered
            	if (nameCheck != true && passCheck != true) {
	            	// Create a new User object with admin role and register in the database
	            	User user=new User(userName, password, "Admin");
	                databaseHelper.register(user);
	                System.out.println("Administrator setup completed.");
	                
	                // Navigate to the Welcome Login Page
	                new WelcomeLoginPage(databaseHelper).show(primaryStage,user);
            	}
            	//Show password or username error if it has errormessage
            	if (!nameCheck) {
            		label_UserName.setVisible(false);
            	}
            	if (!passCheck) {
            		label_Password.setVisible(false);
            	}
            	if (nameCheck) {
            		label_UserName.setVisible(true);
            	}
            	if (passCheck) {
            		label_Password.setVisible(true);
            	}
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        });

        VBox layout = new VBox(10, userNameField, passwordField, setupButton, label_UserName, label_Password);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Administrator Setup");
        primaryStage.show();
    }
}
