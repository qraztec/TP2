package application;

import java.sql.SQLException;

import databasePart1.DatabaseHelper;

public class UpdateTestingAutomation {
	static int numPassed = 0; //counter of the number of passed tests
	static int numFailed = 0; //counter of the number of failed tests
	private static Question currentQuestion; //Question class for question test
	private static Answer currentAnswer; //Answer class for answer test cases
	private static DatabaseHelper dbHelper = new DatabaseHelper(); //Database helper class
	
	public static void main(String[] args) {
		try {
			dbHelper.connectToDatabase();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("___________________");
		System.out.println("\nTesting Automation");
		
		performQuestionCase(1, "First question", "Updated Question", true);
		performAnswerCase(2, "First answer", "Updated answer", true);
		
		
	}
	private static void performQuestionCase(int testCase, String initialText, String updatedText, boolean expectedPass) {
		System.out.println("_____________________");
		System.out.println("Input: \"" + initialText + "\"");
		System.out.println("Test Case #: \"" + testCase + "\"");
		System.out.println("Expected Update: \"" + updatedText + "\"");
		System.out.println("______________________");
		
		//Call the Question class to make and then update a question.
		try {
			//dbHelper.connectToDatabase();
			currentQuestion = new Question(initialText);
			currentQuestion.create(dbHelper);
			currentQuestion.update(dbHelper,  updatedText);
			Question q = Question.read(dbHelper, currentQuestion.getId());
			
			String compareText = q.getContent();
			System.out.println(compareText);
			if (expectedPass) {
				if (compareText != updatedText) {
					System.out.println("**Failure** Question Test failed but it was supposed to succeed");
				}
				else {
					System.out.println("**Success** Question Test succeeded and it was supposed to succeed");
				}
			}
			else {
				if (compareText != updatedText) {
					System.out.println("**Success** Question Test failed and it was supposed to fail");
				}
				else {
					System.out.println("**Failure** Question Test succeeded but it was supposed to fail");
				}
			}
			currentQuestion.delete(dbHelper);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Question Test failed");
		}
	}
	
	private static void performAnswerCase(int testCase, String initialText, String updatedText, boolean expectedPass) {
		System.out.println("_____________________");
		System.out.println("Input: \"" + initialText + "\"");
		System.out.println("Test Case #: \"" + testCase + "\"");
		System.out.println("Expected Update: \"" + updatedText + "\"");
		System.out.println("______________________");
		
		try {
			//dbHelper.connectToDatabase();
			currentAnswer = new Answer(1, initialText);
			currentAnswer.create(dbHelper);
			currentAnswer.update(dbHelper,  updatedText);
			Answer a = Answer.read(dbHelper, currentAnswer.getId());
			String compareText = a.getContent();
			if (expectedPass) {
				if (compareText != updatedText) {
					System.out.println("**Failure** Answer Test failed but it was supposed to succeed");
				}
				else {
					System.out.println("**Success** Answer Test succeeded and it was supposed to succeed");
				}
			}
			else {
				if (compareText != updatedText) {
					System.out.println("**Success** Answer Test failed and it was supposed to fail");
				}
				else {
					System.out.println("**Failure** Answer Test succeeded but it was supposed to fail");
				}
			}
			currentAnswer.delete(dbHelper);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Answer Test failed");
		}
	}
	
}
