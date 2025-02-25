

package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import databasePart1.*;

/**
 * The UserLoginPage class provides a login interface for users.
 */
public class UserLoginPage {

    private final DatabaseHelper databaseHelper;

    public UserLoginPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        Button loginButton = new Button("Login");

        loginButton.setOnAction(a -> {
            String userName = userNameField.getText();
            String password = passwordField.getText();

            try {
                String role = databaseHelper.getUserRole(userName);
                if (role != null) {
                    User user = new User(userName, password, role);
                    if (databaseHelper.login(user)) {
                        List<String> roleList = Arrays.asList(role.split(","));
                        if (roleList.size() > 1) {
                            new WelcomeLoginPage(databaseHelper).show(primaryStage, user);
                        } else {
                            navigateToRoleHome(primaryStage, user);
                        }
                    } else {
                        errorLabel.setText("Error logging in");
                    }
                } else {
                    errorLabel.setText("User account doesn't exist");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                errorLabel.setText("Database error");
            }
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(userNameField, passwordField, loginButton, errorLabel);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("User Login");
        primaryStage.show();
    }

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

