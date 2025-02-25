package application;

import databasePart1.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Questions class manages a collection of Question objects.
 * It supports loading all questions from the database, searching for specific questions,
 * and managing the local list of questions.
 */
public class Questions {

    // List to hold all Question objects
    private List<Question> questionList;

    /**
     * Constructs an empty Questions container.
     */
    public Questions() {
        questionList = new ArrayList<>();
    }

    /**
     * Loads all questions from the database into the local list.
     *
     * @param dbHelper the DatabaseHelper instance providing the database connection.
     * @throws SQLException if an error occurs during database access.
     */
    public void loadAllQuestions(DatabaseHelper dbHelper) throws SQLException {
        String sql = "SELECT id, content FROM Questions";
        try (PreparedStatement pstmt = dbHelper.getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            // Clear the current list before loading new data
            questionList.clear();
            while (rs.next()) {
                int id = rs.getInt("id");
                String content = rs.getString("content");
                // Create a new Question object using the data from the database
                Question q = new Question(id, content);
                questionList.add(q);
            }
        }
    }

    /**
     * Searches for questions that contain the specified keyword.
     *
     * @param keyword the keyword to search for.
     * @return a list of Question objects whose content contains the keyword.
     */
    public List<Question> searchQuestions(String keyword) {
        List<Question> result = new ArrayList<>();
        // Convert keyword to lower case for a case-insensitive search
        String lowerKeyword = keyword.toLowerCase();
        for (Question q : questionList) {
            if (q.getContent().toLowerCase().contains(lowerKeyword)) {
                result.add(q);
            }
        }
        return result;
    }

    /**
     * Adds a Question to the local list.
     * Note: This method only adds the question to the list. If you want to persist it,
     *       you should call the create() method on the Question object.
     *
     * @param q the Question object to add.
     */
    public void addQuestion(Question q) {
        questionList.add(q);
    }

    /**
     * Removes a Question from the local list.
     * Note: This method only removes the question from the local list. To delete it from the database,
     *       call the delete() method on the Question object.
     *
     * @param q the Question object to remove.
     */
    public void removeQuestion(Question q) {
        questionList.remove(q);
    }

    /**
     * Retrieves the local list of all loaded questions.
     *
     * @return the list of Question objects.
     */
    public List<Question> getQuestionList() {
        return questionList;
    }
}
