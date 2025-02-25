package application;

import databasePart1.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Answers class manages a collection of Answer objects.
 * It supports loading all answers from the database, searching for specific answers,
 * and managing the local list of answers.
 */
public class Answers {

    // List to hold all Answer objects
    private List<Answer> answerList;

    /**
     * Constructs an empty Answers container.
     */
    public Answers() {
        answerList = new ArrayList<>();
    }

    /**
     * Loads all answers from the database into the local list.
     *
     * @param dbHelper the DatabaseHelper instance providing the database connection.
     * @throws SQLException if an error occurs during database access.
     */
    public void loadAllAnswers(DatabaseHelper dbHelper) throws SQLException {
        String sql = "SELECT id, questionId, content FROM Answers";
        try (PreparedStatement pstmt = dbHelper.getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            // Clear the current list before loading new data
            answerList.clear();
            while (rs.next()) {
                int id = rs.getInt("id");
                int questionId = rs.getInt("questionId");
                String content = rs.getString("content");
                // Create a new Answer object using the data from the database
                Answer answer = new Answer(id, questionId, content);
                answerList.add(answer);
            }
        }
    }

    /**
     * Searches for answers that contain the specified keyword.
     *
     * @param keyword the keyword to search for.
     * @return a list of Answer objects whose content contains the keyword.
     */
    public List<Answer> searchAnswers(String keyword) {
        List<Answer> result = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Answer a : answerList) {
            if (a.getContent().toLowerCase().contains(lowerKeyword)) {
                result.add(a);
            }
        }
        return result;
    }

    /**
     * Adds an Answer to the local list.
     * Note: This method only adds the answer to the list. To persist it in the database,
     *       call the create() method on the Answer object.
     *
     * @param a the Answer object to add.
     */
    public void addAnswer(Answer a) {
        answerList.add(a);
    }

    /**
     * Removes an Answer from the local list.
     * Note: This method only removes the answer from the local list. To delete it from the database,
     *       call the delete() method on the Answer object.
     *
     * @param a the Answer object to remove.
     */
    public void removeAnswer(Answer a) {
        answerList.remove(a);
    }

    /**
     * Retrieves the local list of all loaded answers.
     *
     * @return the list of Answer objects.
     */
    public List<Answer> getAnswerList() {
        return answerList;
    }
}
