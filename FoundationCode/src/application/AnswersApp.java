package application;

import databasePart1.DatabaseHelper;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

/**
 * The AnswersApp class demonstrates how to load and display all answers
 * from the database using the Answers collection manager.
 */
public class AnswersApp extends Application {

    private DatabaseHelper dbHelper = new DatabaseHelper();
    private Answers answersManager = new Answers();

    @Override
    public void start(Stage primaryStage) {
        try {
            // Connect to the database
            dbHelper.connectToDatabase();

            // Create UI elements
            Label titleLabel = new Label("All Answers");
            ListView<String> listView = new ListView<>();
            Button loadButton = new Button("Load Answers");
            TextField searchField = new TextField();
            searchField.setPromptText("Enter keyword to search");
            Button searchButton = new Button("Search");

            // Layout
            VBox root = new VBox(10, titleLabel, loadButton, searchField, searchButton, listView);
            root.setPadding(new Insets(15));

            // Load all answers button action
            loadButton.setOnAction(e -> {
                try {
                    answersManager.loadAllAnswers(dbHelper);
                    // Clear the ListView and add loaded answers
                    listView.getItems().clear();
                    for (Answer a : answersManager.getAnswerList()) {
                        listView.getItems().add(a.toString());
                    }
                } catch (SQLException ex) {
                    showAlert("Error", "Failed to load answers: " + ex.getMessage());
                }
            });

            // Search button action
            searchButton.setOnAction(e -> {
                String keyword = searchField.getText();
                List<Answer> results = answersManager.searchAnswers(keyword);
                listView.getItems().clear();
                if (results.isEmpty()) {
                    listView.getItems().add("No answers found with keyword: " + keyword);
                } else {
                    for (Answer a : results) {
                        listView.getItems().add(a.toString());
                    }
                }
            });

            Scene scene = new Scene(root, 500, 400);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Answers Collection Demo");
            primaryStage.show();

        } catch (SQLException ex) {
            showAlert("Database Error", "Failed to connect: " + ex.getMessage());
        }
    }

    /**
     * Utility method to show an alert dialog.
     *
     * @param title   The title of the alert.
     * @param message The message content of the alert.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }

    /**
     * Main entry point for launching the AnswersApp.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
