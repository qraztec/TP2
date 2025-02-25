package application;

import databasePart1.DatabaseHelper;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

/**
 * The AnswerApp class demonstrates CRUD operations on the Answer class.
 * It provides a simple JavaFX interface to create, read, update, and delete an answer.
 */
public class AnswerApp extends Application {

    private DatabaseHelper dbHelper = new DatabaseHelper();
    private Answer currentAnswer; // holds the currently created answer for demo

    @Override
    public void start(Stage primaryStage) {
        try {
            dbHelper.connectToDatabase();
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to connect to database: " + e.getMessage());
            return;
        }
        
        // UI Elements
        Label titleLabel = new Label("Answer CRUD Demo");
        TextField questionIdField = new TextField();
        questionIdField.setPromptText("Enter Question ID");
        
        TextField contentField = new TextField();
        contentField.setPromptText("Enter answer content");
        
        Button createButton = new Button("Create Answer");
        Button readButton = new Button("Read Answer");
        Button updateButton = new Button("Update Answer");
        Button deleteButton = new Button("Delete Answer");
        
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        
        // Set up actions for each button
        
        // Create Answer
        createButton.setOnAction(e -> {
            try {
                // Parse questionId from text field
                int questionId = Integer.parseInt(questionIdField.getText().trim());
                String content = contentField.getText();
                currentAnswer = new Answer(questionId, content);
                currentAnswer.create(dbHelper);
                outputArea.appendText("Created: " + currentAnswer + "\n");
            } catch (NumberFormatException nfe) {
                outputArea.appendText("Invalid Question ID. Please enter a valid integer.\n");
            } catch (Exception ex) {
                outputArea.appendText("Error creating answer: " + ex.getMessage() + "\n");
            }
        });
        
        // Read Answer
        readButton.setOnAction(e -> {
            if (currentAnswer != null) {
                try {
                    Answer readAnswer = Answer.read(dbHelper, currentAnswer.getId());
                    if (readAnswer != null) {
                        outputArea.appendText("Read: " + readAnswer + "\n");
                    } else {
                        outputArea.appendText("Answer not found.\n");
                    }
                } catch (SQLException ex) {
                    outputArea.appendText("Error reading answer: " + ex.getMessage() + "\n");
                }
            } else {
                outputArea.appendText("No answer created yet.\n");
            }
        });
        
        // Update Answer
        updateButton.setOnAction(e -> {
            if (currentAnswer != null) {
                try {
                    String newContent = contentField.getText();
                    currentAnswer.update(dbHelper, newContent);
                    outputArea.appendText("Updated: " + currentAnswer + "\n");
                } catch (Exception ex) {
                    outputArea.appendText("Error updating answer: " + ex.getMessage() + "\n");
                }
            } else {
                outputArea.appendText("No answer created to update.\n");
            }
        });
        
        // Delete Answer
        deleteButton.setOnAction(e -> {
            if (currentAnswer != null) {
                try {
                    currentAnswer.delete(dbHelper);
                    outputArea.appendText("Deleted answer with id: " + currentAnswer.getId() + "\n");
                    currentAnswer = null;
                } catch (SQLException ex) {
                    outputArea.appendText("Error deleting answer: " + ex.getMessage() + "\n");
                }
            } else {
                outputArea.appendText("No answer created to delete.\n");
            }
        });
        
        // Layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        root.getChildren().addAll(
            titleLabel,
            new Label("Question ID:"), questionIdField,
            new Label("Answer Content:"), contentField,
            createButton, readButton, updateButton, deleteButton,
            outputArea
        );
        
        Scene scene = new Scene(root, 500, 500);
        primaryStage.setTitle("Answer CRUD Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * Utility method to show an error alert.
     *
     * @param title   The title of the alert.
     * @param message The message to display.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
    
    /**
     * Main entry point for launching the AnswerApp.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
