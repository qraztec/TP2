package application;

import databasePart1.DatabaseHelper;
import java.sql.*;

public class Question {
    private int id;
    private String content;

    // Constructor for a new Question (id will be generated)
    public Question(String content) {
        if (!isValidContent(content)) {
            throw new IllegalArgumentException("Question content cannot be empty.");
        }
        this.content = content;
    }

    // Constructor for an existing Question (with id)
    public Question(int id, String content) {
        if (!isValidContent(content)) {
            throw new IllegalArgumentException("Question content cannot be empty.");
        }
        this.id = id;
        this.content = content;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if (!isValidContent(content)) {
            throw new IllegalArgumentException("Question content cannot be empty.");
        }
        this.content = content;
    }

    // Simple validation method
    private boolean isValidContent(String content) {
        return content != null && !content.trim().isEmpty();
    }

    // -------------------------------
    // CRUD Operations using DatabaseHelper
    // -------------------------------

    // Create a new Question in the database.
    public void create(DatabaseHelper dbHelper) throws SQLException {
        String sql = "INSERT INTO Questions (content) VALUES (?)";
        try (PreparedStatement pstmt = dbHelper.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, this.content);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating question failed, no rows affected.");
            }
            // Retrieve the generated id
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.id = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating question failed, no ID obtained.");
                }
            }
        }
    }

    // Read a Question from the database by id.
    public static Question read(DatabaseHelper dbHelper, int questionId) throws SQLException {
        String sql = "SELECT id, content FROM Questions WHERE id = ?";
        try (PreparedStatement pstmt = dbHelper.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, questionId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String content = rs.getString("content");
                    return new Question(id, content);
                } else {
                    return null; // Question not found
                }
            }
        }
    }

    // Update an existing Question in the database.
    public void update(DatabaseHelper dbHelper, String newContent) throws SQLException {
        if (!isValidContent(newContent)) {
            throw new IllegalArgumentException("New question content cannot be empty.");
        }
        String sql = "UPDATE Questions SET content = ? WHERE id = ?";
        try (PreparedStatement pstmt = dbHelper.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, newContent);
            pstmt.setInt(2, this.id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating question failed, no rows affected.");
            }
            this.content = newContent;
        }
    }

    // Delete a Question from the database.
    public void delete(DatabaseHelper dbHelper) throws SQLException {
        String sql = "DELETE FROM Questions WHERE id = ?";
        try (PreparedStatement pstmt = dbHelper.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, this.id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting question failed, no rows affected.");
            }
        }
    }

    @Override
    public String toString() {
        return "Question [id=" + id + ", content=" + content + "]";
    }
}
