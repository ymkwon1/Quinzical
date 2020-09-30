package application.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class QuinzicalModel {

	// for singleton class
	private static QuinzicalModel instance = null;

	private List<String> categories = new ArrayList<String>();
	private List<String> fiveRandomCategories = new ArrayList<String>();
	private Map<String, List<String>> data = new HashMap<String, List<String>>();
	private String[] currentAnswers;
	private String currentAnswer;
	private String currentQuestion;
	private String currentCategory;
	private String currentTag;
	private List<String> _fiveRandomClues;
	private int currentValue;
	private int attempts = 0;

	public QuinzicalModel() throws Exception {
		initialiseCategories();
		readCategories();
		//		for (Map.Entry<String, List<String>> entry: data.entrySet()) {
		//			String key = entry.getKey();
		//			List<String> values = entry.getValue();
		//			System.out.println("Key = " + key);
		//			System.out.println(values);
		//		}
	}
	
	private void formatQuestionSet (String questionSet) {
        String[] splitString = questionSet.split("[\\(\\)]");
		
		String question = splitString[0].trim();
		String answer = splitString[2].trim();
		question = question.replaceAll("[,]$|[.]$","");
		answer = answer.replaceAll("[,]$|[.]$","");

		currentAnswers = answer.split("/");

		setCurrentQuestion(question);
		setCurrentAnswers(currentAnswers);
		setCurrentAnswer(answer);
		setCurrentTag(splitString[1].trim());

		System.out.println(question);
		System.out.println(splitString[1].trim().replace("[^a-zA-Z0-9 ]", ""));
		for (String answers: currentAnswers) {
			System.out.println(answers.trim());
		}
	}
	public void loadRandomQuestionAndAnswer(String category) {
		List<String> questionSets = new ArrayList<String>();
		questionSets = data.get(category);
		int size = questionSets.size();
		Random rand = new Random();
		int randomIndex = rand.nextInt(size);
		String randomQuestionSet = questionSets.get(randomIndex);
		formatQuestionSet(randomQuestionSet);
	}
	/**
	 * Set five random clues and answers of a category
	 */
	public void setFiveRandomClues (String category){
		List<String> questionSets = new ArrayList<String>();
		questionSets = data.get(category);
		Collections.shuffle(questionSets);
		List<String> temp = new ArrayList<String>();
		for (int i = 0; i<5;i++) {
			temp.add(questionSets.get(i));
		}
		_fiveRandomClues = temp;
		System.out.println(_fiveRandomClues);
	}
	/**
	 * Load the question set from five randomly chosen questions.
	 */
	public void loadQuestionSet(int index) {
		String questionSet = _fiveRandomClues.get(index);
		formatQuestionSet(questionSet);
	}
	
	/**
	 * checkAnswers check if the string userInput is equal case insensitive 
	 * to the current answer
	 * @param userInput is a string
	 * @return true if userInput is the correct answer false otherwise
	 */
	public boolean checkAnswer(String userInput) {
		for (String answer: currentAnswers) {
			if (userInput.equalsIgnoreCase(answer.trim())) {
				attempts=0;
				return true;
			}
		}
		attempts++;
		return false;
	}


	/**
	 * set current category
	 * @param category
	 */
	public void setCurrentCategory(String category){
		currentCategory = category;
	}	
	/**
	 * get currentCategory
	 * @return currentCategory
	 */
	public String getCurrentCategory() {
		return currentCategory;
	}	

	/**
	 * set currentQuestion
	 * @param question
	 */
	public void setCurrentQuestion(String question){
		currentQuestion = question;
	}	
	/**
	 * get currentQuestion
	 * @return currentQuestion
	 */
	public String getCurrentQuestion() {
		return currentQuestion;
	}	

	/**
	 * set current answer
	 * @param answer
	 */
	public void setCurrentAnswers(String[] answers) {
		currentAnswers = answers;
	}	
	/**
	 * get current answer
	 * @return currentAnswer
	 */
	public String[] getCurrentAnswers() {
		return currentAnswers;
	}
	
	/**
	 * set current answer
	 * @param answer
	 */
	public void setCurrentAnswer(String answer) {
		currentAnswer = answer;
	}	
	/**
	 * get current answer
	 * @return currentAnswer
	 */
	public String getCurrentAnswer() {
		return currentAnswer;
	}

	/**
	 * set currentValue
	 * @param value is the value of the current question
	 */
	public void setCurrentValue(int value) {
		currentValue = value;
	}
	/**
	 * get current value
	 * @return currentValue
	 */
	public int getCurrentValue() {
		return currentValue;
	}

	/**
	 * set currentTag
	 * @param value is the value of the current question
	 */
	public void setCurrentTag(String tag) {
		currentTag = tag;
	}
	/**
	 * get current tag
	 * @return currentValue
	 */
	public String getCurrentTag() {
		return currentTag;
	}

	/**
	 * get String of attempts
	 * @return winnings
	 */
	public void setAttempts(int numberOfAttempts) {
		attempts = numberOfAttempts;
	}

	/**
	 * get String of attempts
	 * @return winnings
	 */
	public String getAttempts() {
		return String.valueOf(attempts);
	}


	/**
	 * get all categories in the game as a list of strings
	 * @return categories
	 */
	public List<String> getCategory(){
		return categories;
	}




	/**
	 * using bash command to find the names of the categories in the category folder
	 * each category is added to the list
	 */
	private void initialiseCategories() {
		categories = executeBashCmdWithOutput("ls categories");
		System.out.println(categories);
	}
	
	/**
	 * Get five random categories for the games module.
	 */
	public List<String> getFiveRandomCategories () {
		if (fiveRandomCategories.size() < 5 ) {
			List<String> shuffledCategories = new ArrayList<String>(categories);
			Collections.shuffle(shuffledCategories);
			System.out.println(shuffledCategories);
			for (int i = 0; i < 5; i++) {
				fiveRandomCategories.add(shuffledCategories.get(i));
			}
		}
		return fiveRandomCategories;
	}

	/**
	 * read the data from the category files 
	 * and store into 2d array data in the form of i.e.
	 * [animals line line line line line
	 * countries line line line line line]
	 */
	private void readCategories() throws Exception {
		if (this.getCategory().size() > 0) {
			for (String category: categories) {

				try {
					File file = new File("categories/", category);
					Scanner myReader = new Scanner(file);
					List<String> allLines = new ArrayList<String>();
					while(myReader.hasNextLine()) {
						String fileLine = myReader.nextLine();
						allLines.add(fileLine);
					}
					List<String> copy = new ArrayList<String>(allLines);
					data.put(category, copy);
					allLines.clear();
					myReader.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}


	/**
	 * executes bash command that generates an output into a list of strings
	 * @param command the command we are executing in bash
	 * @return List<String> the output of bash command
	 */
	private List<String> executeBashCmdWithOutput(String command) {
		List<String> bashOutput = new ArrayList<String>();
		try {
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

			Process process = pb.start();

			BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			int exitStatus = process.waitFor();

			if (exitStatus == 0) {
				String line;
				while ((line = stdout.readLine()) != null) {
					bashOutput.add(line);
				}
			} else {
				String line;
				while ((line = stderr.readLine()) != null) {
					System.err.println(line);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bashOutput;
	}


	/**
	 * execute a bash command that doesn't have an output
	 * @param command
	 */
	@SuppressWarnings("unused")
	private void executeBashCmdNoOutput(String command) {
		try {
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
			Process process = pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * text to speech a string
	 */
	public void tts(String string) {
		executeBashCmdNoOutput(String.format("echo \"%s\" | festival --tts", string));
	}

	/**
	 *  this static method is for creating a 
	 *  singleton class of Jeopardy 
	 */ 
	public static QuinzicalModel createInstance() throws Exception {
		if (instance == null) {
			instance = new QuinzicalModel();
		}
		return instance;
	}
}
