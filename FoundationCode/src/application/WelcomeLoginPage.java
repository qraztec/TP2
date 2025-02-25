//package application;
//
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//import javafx.application.Platform;
//import databasePart1.*;
//
//import java.util.List;
//import java.util.Arrays;
//
///**
// * The WelcomeLoginPage class displays a welcome screen for authenticated users.
// * It allows users to navigate to their respective pages based on their role or quit the application.
// */
//public class WelcomeLoginPage {
//	
//	private final DatabaseHelper databaseHelper;
//
//    public WelcomeLoginPage(DatabaseHelper databaseHelper) {
//        this.databaseHelper = databaseHelper;
//    }
//    public void show( Stage primaryStage, User user) {
//    	
//    	VBox layout = new VBox(10);
//	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
//	    
//	    Label welcomeLabel = new Label("Welcome!!");
//	    welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
//	    
//	    // Get user's roles (A.P.)
//	    List<String> roleList = Arrays.asList(user.getRole().split(","));
//	    
//	    // If a user has multiple roles show a drop down to select one (A.P.)
//	    
//	    	Label roleLabel = new Label("Select your role:");
//	    	ComboBox<String> roleDropdown = new ComboBox<>();
//	    	roleDropdown.getItems().addAll(roleList);
//	    	
//	    	Button confirmButton = new Button("Confirm Role");
//	    	confirmButton.setOnAction(e -> {
//	    			user.setRole(roleDropdown.getValue());
//	    			String role =user.getRole();
//		    		System.out.println(role);
//		    	
//		    		if(role.equals("Admin")) {
//		    			new AdminHomePage().show(primaryStage, this.databaseHelper);
//		    		} else if(role.equals("Student")) {
//		    			new StudentHomePage().show(primaryStage, this.databaseHelper);
//		    		} else if(role.equals("Instructor")) {
//		    			new InstructorHomePage().show(primaryStage, this.databaseHelper);
//		    		} else if(role.equals("Staff")) {
//		    			new StaffHomePage().show(primaryStage, this.databaseHelper);
//		    		} else if(role.equals("Reviewer")) {
//		    			new ReviewerHomePage().show(primaryStage, this.databaseHelper);
//		    		} 
//	    	});
//	    	
//	    
//	    	layout.getChildren().addAll(welcomeLabel, roleLabel, roleDropdown, confirmButton);	    
//	   
//	    
//
//	
//	    Scene welcomeScene = new Scene(layout, 800, 400);
//
//	    // Set the scene to primary stage
//	    primaryStage.setScene(welcomeScene);
//	    primaryStage.setTitle("Welcome Page");
//    }
//}

package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import databasePart1.*;

import java.util.List;
import java.util.Arrays;

/**
 * The WelcomeLoginPage class allows users to pick a role if they have multiple roles.
 */
public class WelcomeLoginPage {

    private final DatabaseHelper databaseHelper;

    public WelcomeLoginPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage, User user) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label welcomeLabel = new Label("Welcome, " + user.getUserName() + "!");
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Get user's roles
        List<String> roleList = Arrays.asList(user.getRole().split(","));

        Label roleLabel = new Label("Select your role:");
        ComboBox<String> roleDropdown = new ComboBox<>();
        roleDropdown.getItems().addAll(roleList);

        Button confirmButton = new Button("Confirm Role");
        confirmButton.setOnAction(e -> {
            user.setRole(roleDropdown.getValue());
            navigateToRoleHome(primaryStage, user);
        });

        // Logout button to return to login
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> new UserLoginPage(databaseHelper).show(primaryStage));

        layout.getChildren().addAll(welcomeLabel, roleLabel, roleDropdown, confirmButton, logoutButton);

        Scene welcomeScene = new Scene(layout, 800, 400);
        primaryStage.setScene(welcomeScene);
        primaryStage.setTitle("Role Selection");
    }

    // Navigates to the selected role’s home page
    private void navigateToRoleHome(Stage primaryStage, User user) {
        switch (user.getRole()) {
            case "Admin":
                new AdminHomePage().show(primaryStage, databaseHelper, user);
                break;
            case "Student":
                new StudentHomePage().show(primaryStage, databaseHelper, user);
                break;
            case "Instructor":
                new InstructorHomePage().show(primaryStage, databaseHelper, user);
                break;
            case "Staff":
                new StaffHomePage().show(primaryStage, databaseHelper, user);
                break;
            case "Reviewer":
                new ReviewerHomePage().show(primaryStage, databaseHelper, user);
                break;
            default:
                System.out.println("Invalid Role Selected");
        }
    }
}
