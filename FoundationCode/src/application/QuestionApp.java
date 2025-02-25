package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class QuestionApp {

    private DatabaseHelper dbHelper;
    private Question currentQuestion; // Holds the current question for demo

    public QuestionApp(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void show(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        
        Label titleLabel = new Label("Question CRUD Demo");
        TextField questionField = new TextField();
        questionField.setPromptText("Enter question content");
        
        Button createButton = new Button("Create Question");
        Button readButton = new Button("Read Question");
        Button updateButton = new Button("Update Question");
        Button deleteButton = new Button("Delete Question");
        TextArea outputArea = new TextArea();
        
        createButton.setOnAction(e -> {
            try {
                String content = questionField.getText();
                currentQuestion = new Question(content);
                currentQuestion.create(dbHelper);
                outputArea.appendText("Created: " + currentQuestion + "\n");
            } catch (Exception ex) {
                outputArea.appendText("Error creating question: " + ex.getMessage() + "\n");
            }
        });
        
        readButton.setOnAction(e -> {
            if (currentQuestion != null) {
                try {
                    Question q = Question.read(dbHelper, currentQuestion.getId());
                    if (q != null) {
                        outputArea.appendText("Read: " + q + "\n");
                    } else {
                        outputArea.appendText("Question not found.\n");
                    }
                } catch (SQLException ex) {
                    outputArea.appendText("Error reading question: " + ex.getMessage() + "\n");
                }
            } else {
                outputArea.appendText("No question created yet.\n");
            }
        });
        
        updateButton.setOnAction(e -> {
            if (currentQuestion != null) {
                try {
                    String newContent = questionField.getText();
                    currentQuestion.update(dbHelper, newContent);
                    outputArea.appendText("Updated: " + currentQuestion + "\n");
                } catch (Exception ex) {
                    outputArea.appendText("Error updating question: " + ex.getMessage() + "\n");
                }
            } else {
                outputArea.appendText("No question created to update.\n");
            }
        });
        
        deleteButton.setOnAction(e -> {
            if (currentQuestion != null) {
                try {
                    currentQuestion.delete(dbHelper);
                    outputArea.appendText("Deleted question with id: " + currentQuestion.getId() + "\n");
                    currentQuestion = null;
                } catch (SQLException ex) {
                    outputArea.appendText("Error deleting question: " + ex.getMessage() + "\n");
                }
            } else {
                outputArea.appendText("No question created to delete.\n");
            }
        });
        
        root.getChildren().addAll(titleLabel, questionField, createButton, readButton, updateButton, deleteButton, outputArea);
        Scene scene = new Scene(root, 400, 500);
        stage.setScene(scene);
        stage.setTitle("Question CRUD Demo");
        stage.show();
    }
}
