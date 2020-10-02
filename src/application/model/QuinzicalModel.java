package application.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Stream;



public class QuinzicalModel {

	// for singleton class
	private static QuinzicalModel instance = null;

	private List<String> _categories;
	private List<String> _fiveRandomCategories;
	private List<String> _fiveRandomClues;
	private Map<String, List<String>> _practiceData = new HashMap<String, List<String>>();
	private Map<String, List<String>> _gamesData = new HashMap<String, List<String>>();
	private Clue _currentClue;
	private int [] _answeredQuestions = {0,0,0,0,0};
	private int ttsSpeed = 175;

	public QuinzicalModel() throws Exception {
		initialisecategories();
		readcategories();
		setFiveRandomCategories();
		//		for (Map.Entry<String, List<String>> entry: data.entrySet()) {
		//			String key = entry.getKey();
		//			List<String> values = entry.getValue();
		//			System.out.println("Key = " + key);
		//			System.out.println(values);
		//		}
		File winnings = new File("winnings");
		if (!winnings.exists()) {
			executeBashCmdNoOutput("touch winnings");
			executeBashCmdNoOutput("echo 0 >> winnings");
		}
	}

	/**
	 * using bash command to find the names of the categories in the category folder
	 * each category is added to the list
	 */
	private void initialisecategories() {
		_categories = executeBashCmdWithOutput("ls categories");
		System.out.println(_categories);
	}

	/**
	 * read the data from the category files 
	 * and store into 2d array data in the form of i.e.
	 * [animals line line line line line
	 * countries line line line line line]
	 */
	private void readcategories() throws Exception {
		if (_categories.size() > 0) {
			for (String category: _categories) {

				try {
					File file = new File("categories/", category);
					Scanner myReader = new Scanner(file);
					List<String> allLines = new ArrayList<String>();
					while(myReader.hasNextLine()) {
						String fileLine = myReader.nextLine();
						allLines.add(fileLine);
					}
					List<String> copy = new ArrayList<String>(allLines);
					_practiceData.put(category, copy);
					allLines.clear();
					myReader.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void setFiveRandomCategories () {
		List<String> shuffledCategories = new ArrayList<String>(_categories);
		Collections.shuffle(shuffledCategories);
		//create a file to store the selected five categories
		File five_random_categories = new File ("five_random_categories");
		if (!five_random_categories.exists()) {
			try {
				five_random_categories.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (five_random_categories.length() == 0) {
			List<String> temp = new ArrayList<String> ();
			for (int i = 0; i < 5; i++) {
				try {
					//create files that are used to store the clues of 
					//the selected five categories
					File dir = new File ("games_module");
					if (!dir.exists()) {
						dir.mkdir();
					}
					File file = new File(dir,shuffledCategories.get(i));
					if (!file.exists()) {
						file.createNewFile();
					}		
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				temp.add(shuffledCategories.get(i));
				setFiveRandomClues(shuffledCategories.get(i));
				executeBashCmdNoOutput(String.format("echo \"%s\" >> five_random_categories",shuffledCategories.get(i)));
				_gamesData.put(shuffledCategories.get(i), _fiveRandomClues);
			}
			_fiveRandomCategories = temp;
		}
	}
	
	/**
	 * Get five random categories for the games module.
	 */
	public List<String> getFiveRandomCategories () {
		return _fiveRandomCategories;
	}
	
	public void loadRandomClue(String category) {
		List<String> clues = new ArrayList<String>();
		clues = _practiceData.get(category);
		int size = clues.size();
		Random rand = new Random();
		int randomIndex = rand.nextInt(size);
		try {
			_currentClue = new Clue(clues.get(randomIndex));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Set five random clues and answers of a category
	 */
	public void setFiveRandomClues (String category){
		List<String> clues = new ArrayList<String>();
		clues = _practiceData.get(category);
		Collections.shuffle(clues);
		List<String> temp = new ArrayList<String>();
		for (int i = 0; i<5;i++) {
			temp.add(clues.get(i));
		}
		_fiveRandomClues = temp;
		System.out.println(_fiveRandomClues);
	}
	/**
	 * Load the clue from five randomly chosen clues.
	 */
	public void loadClue(String category,int index) {
		String clue = _gamesData.get(category).get(index);
	    try {
			_currentClue = new Clue (clue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public Clue getCurrentClue() {
		return _currentClue;
	}
	
	public int getAnsweredQuestions(int index){
		return _answeredQuestions[index];
	}

	public void setAnsweredQuetsions(int index) {
		_answeredQuestions[index]++;
	}
	
	/**
	 * get all categories in the game as a list of strings
	 * @return categories
	 */
	public List<String> getCategories(){
		return _categories;
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
		executeBashCmdNoOutput(String.format("espeak \"%s\" --stdout -s %d | aplay", string, ttsSpeed));
		
	}
	
	public void stopTts () {
//		String command = "pkill -f espeak";
//		executeBashCmdNoOutput(command);
		Stream<ProcessHandle> descendents = ProcessHandle.current().descendants();
		descendents.filter(ProcessHandle::isAlive).forEach(ph -> {
			ph.destroy();
		});
	}

	/**
	 * increase tts speed
	 */
	public void increaseTTSSpeed() {
		ttsSpeed = ttsSpeed + 10;
	}

	/**
	 * decrease tts speed
	 */
	public void decreaseTTSSpeed() {
		ttsSpeed = ttsSpeed - 10;
	}
	
	/**
	 * get tts speed
	 * @return ttsSpeed
	 */
	public int getTTSSpeed() {
		return ttsSpeed;
	}

	/**
	 *  this static method is for creating a 
	 *  singleton class of Quinzical
	 */ 
	public static QuinzicalModel createInstance() throws Exception {
		if (instance == null) {
			instance = new QuinzicalModel();
		}
		return instance;
	}
}
