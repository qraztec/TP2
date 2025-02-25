package application;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OTP_PAGE {
	
	public void show(DatabaseHelper databaseHelper, Stage primaryStage) {
		VBox layout = new VBox();
    	
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // label to display the welcome message for the admin
	    Label adminLabel = new Label("OTP For Specified User");
	    adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        
        Label OTPLabel = new Label();
        OTPLabel.setStyle("--fx-font-size: 18px; -fx-font-weight: bold;");
	    // Create a textbox to get the name of Specified User
	    TextField userNameField = new TextField();
	    userNameField.setPromptText("Enter Username");
	    userNameField.setMaxWidth(250);
	    
	    Button GenerateOTP = new Button("Generate OTP");
	    // When Generate OTP button is pressed, with a valid user it goes to the passcode scene
	    GenerateOTP.setOnAction(a -> {
	    	String userName = userNameField.getText();
	    	if(databaseHelper.doesUserExist(userName)) {
				String OTPassword = databaseHelper.generateOTPCode(userName);
				OTPLabel.setText(OTPassword);
	    	}
			else {
				errorLabel.setText("Username does not exist");
			}
	    });
	    layout.getChildren().addAll(adminLabel, userNameField, GenerateOTP, OTPLabel, errorLabel);
	    Scene One_Time_Password = new Scene(layout, 800, 400);
	    
	    // Set the scene to primary stage
	    primaryStage.setScene(One_Time_Password);
	    primaryStage.setTitle("One-Time Password");
	    
	    
	    
	}
}