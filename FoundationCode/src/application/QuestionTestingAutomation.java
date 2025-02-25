package application;

import application.Question;
import databasePart1.DatabaseHelper;
import java.sql.SQLException;

/**
 * QuestionTestingAutomation
 * 
 * A simple automated test suite for testing CRUD operations on the Question class.
 * This class is modeled after the PasswordEvaluationTestingAutomation example.
 */
public class QuestionTestingAutomation {

    static int numPassed = 0;  // Counter for passed tests
    static int numFailed = 0;  // Counter for failed tests

    public static void main(String[] args) {
        System.out.println("______________________________________");
        System.out.println("\nQuestion Testing Automation");
        
        // Test case 1: Create a valid question, then read and delete it.
        performTestCase(1, "What is CRUD?", true);
        
        // Test case 2: Try to create a question with invalid content (empty string).
        performTestCase(2, "", false);
        
        // Test case 3: Create a valid question and then update its content.
        performUpdateTestCase(3, "What is JDBC?", "What is Java Database Connectivity?", true);
        
        System.out.println("____________________________________________________________________________");
        System.out.println("\nNumber of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
    }

    /**
     * This method tests creating, reading, and deleting a Question.
     * 
     * @param testCase     The test case number.
     * @param content      The content for the question.
     * @param expectedPass true if the test is expected to pass; false otherwise.
     */
    private static void performTestCase(int testCase, String content, boolean expectedPass) {
        System.out.println("____________________________________________________________________________\nTest case: " + testCase);
        System.out.println("Input content: \"" + content + "\"");
        
        DatabaseHelper dbHelper = new DatabaseHelper();
        try {
            dbHelper.connectToDatabase();
        } catch (SQLException e) {
            System.out.println("DB Connection Error: " + e.getMessage());
            numFailed++;
            return;
        }

        try {
            // Attempt to create the question.
            Question q = new Question(content);
            q.create(dbHelper);
            
            // If creation succeeded but we expected failure, flag a test failure.
            if (!expectedPass) {
                System.out.println("***Failure***: A question was created with invalid content.");
                numFailed++;
                // Cleanup if needed.
                q.delete(dbHelper);
            } else {
                // Otherwise, attempt to read back the question.
                Question readQ = Question.read(dbHelper, q.getId());
                if (readQ != null && readQ.getContent().equals(content)) {
                    System.out.println("***Success***: Question created and read successfully.");
                    numPassed++;
                } else {
                    System.out.println("***Failure***: The read question does not match the created one.");
                    numFailed++;
                }
                // Cleanup: delete the question.
                q.delete(dbHelper);
            }
        } catch (IllegalArgumentException e) {
            // Expected if content is invalid.
            if (!expectedPass) {
                System.out.println("***Success***: Invalid question was rejected: " + e.getMessage());
                numPassed++;
            } else {
                System.out.println("***Failure***: Valid question was rejected: " + e.getMessage());
                numFailed++;
            }
        } catch (SQLException se) {
            System.out.println("***Failure***: SQL error: " + se.getMessage());
            numFailed++;
        } finally {
            dbHelper.closeConnection();
        }
    }

    /**
     * This method tests updating a Question.
     * 
     * @param testCase       The test case number.
     * @param initialContent The initial content of the question.
     * @param updatedContent The new content to update the question with.
     * @param expectedPass   true if the update is expected to succeed; false otherwise.
     */
    private static void performUpdateTestCase(int testCase, String initialContent, String updatedContent, boolean expectedPass) {
        System.out.println("____________________________________________________________________________\nTest case: " + testCase + " (Update Test)");
        System.out.println("Initial content: \"" + initialContent + "\"");
        System.out.println("Updated content: \"" + updatedContent + "\"");
        
        DatabaseHelper dbHelper = new DatabaseHelper();
        try {
            dbHelper.connectToDatabase();
        } catch (SQLException e) {
            System.out.println("DB Connection Error: " + e.getMessage());
            numFailed++;
            return;
        }
        
        try {
            // Create the question.
            Question q = new Question(initialContent);
            q.create(dbHelper);
            
            // Attempt to update the question.
            q.update(dbHelper, updatedContent);
            
            // Read back the updated question.
            Question updatedQ = Question.read(dbHelper, q.getId());
            if (updatedQ != null && updatedQ.getContent().equals(updatedContent)) {
                System.out.println("***Success***: Question updated successfully.");
                numPassed++;
            } else {
                System.out.println("***Failure***: Question update failed. Updated content does not match.");
                numFailed++;
            }
            // Cleanup.
            q.delete(dbHelper);
        } catch (IllegalArgumentException e) {
            if (!expectedPass) {
                System.out.println("***Success***: Invalid update was rejected: " + e.getMessage());
                numPassed++;
            } else {
                System.out.println("***Failure***: Valid update was rejected: " + e.getMessage());
                numFailed++;
            }
        } catch (SQLException se) {
            System.out.println("***Failure***: SQL error: " + se.getMessage());
            numFailed++;
        } finally {
            dbHelper.closeConnection();
        }
    }
}
