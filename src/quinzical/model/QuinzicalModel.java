package quinzical.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Stream;

import quinzical.util.BashCmdUtil;



/**
 * The QuinicalModel class has the main implementations of how the game is played
 * being responsible for extracting data from the category files, saving data
 * and storing data for the game
 * @author Dylan Xin, Young Min Kwon
 *
 */
public class QuinzicalModel {

	// for singleton class
	private static QuinzicalModel instance = null;
	private List<String> _nzCategories;
	private List<String> _intCategories;
	private List<String> _fiveRandomCategories = new ArrayList<String>();
	private List<String> _fiveRandomClues;
	private Map<String, List<String>> _nzData = new HashMap<String, List<String>>();
	private Map<String, List<String>> _gamesData = new HashMap<String, List<String>>();
	private Map<String, List<String>> _intData = new HashMap<String, List<String>>();
	private Clue _currentClue;
	private String _currentPlayer = null;
	private int [] _answeredQuestions = {0,0,0,0,0};
	private int _winnings = 0;
	private int _previousScene = 1;
	private PlayerRankings _playerRankings;
	private boolean _internationalUnlocked = false;
	public boolean _internationalSelected = false;

	/**
	 * Constructor for the QuinzicalModel class
	 * loads all necessary data from data so that
	 * the game is ready to go
	 * @throws Exception
	 */
	public QuinzicalModel() throws Exception {
		initialiseCategories();
		readCategories();
		setFiveRandomCategories();
		loadCurrentPlayer();
		setAnsweredQuestions();
		File winnings = new File("data/winnings");
		if (!winnings.exists()) {
			BashCmdUtil.bashCmdNoOutput("touch data/winnings");
			BashCmdUtil.bashCmdNoOutput("echo 0 >> data/winnings");
		}
		BashCmdUtil.bashCmdNoOutput("touch data/tts_speed");
		loadInternationalUnlocked();
	}

	/**
	 * using bash command to find the names of the categories in the category folder
	 * each category is added to the list
	 */
	private void initialiseCategories() {
		_nzCategories = BashCmdUtil.bashCmdHasOutput("ls categories/nz");
		_intCategories = BashCmdUtil.bashCmdHasOutput("ls categories/international");
	}

	/**
	 * read the data from the category files 
	 * and store into 2d array data in the form of i.e.
	 * [animals line line line line line
	 * countries line line line line line]
	 */
	private void readCategories() throws Exception {
		//reads files in nz category
		if (_nzCategories.size() > 0) {
			for (String category: _nzCategories) {
				// read each individual line from the category files and store it
				try {
					File file = new File("categories/nz/", category);
					Scanner myReader = new Scanner(file);
					List<String> allLines = new ArrayList<String>();
					while(myReader.hasNextLine()) {
						String fileLine = myReader.nextLine();
						allLines.add(fileLine);
					}
					List<String> copy = new ArrayList<String>(allLines);
					_nzData.put(category, copy);
					allLines.clear();
					myReader.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		//reads file in international categories
		if (_intCategories.size() > 0) {
			for (String category: _intCategories) {
				// read each individual line from the category files and store it
				try {
					File file = new File("categories/international/", category);
					Scanner myReader = new Scanner(file);
					List<String> allLines = new ArrayList<String>();
					while(myReader.hasNextLine()) {
						String fileLine = myReader.nextLine();
						allLines.add(fileLine);
					}
					List<String> copy = new ArrayList<String>(allLines);
					_intData.put(category, copy);
					allLines.clear();
					myReader.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Prepares 5 random categories to be played by the user in the game
	 * selected from all the category files within the categories folder
	 */
	public void setFiveRandomCategories () {
		//create a file to store the selected five categories
		BashCmdUtil.bashCmdNoOutput("mkdir -p data");
		File five_random_categories = new File ("data/five_random_categories");
		if (!five_random_categories.exists()) {
			BashCmdUtil.bashCmdNoOutput("touch data/five_random_categories");
		}
		// if five random categories dont exist, i.e. new game being started
		if (five_random_categories.length() == 0) {
			List<String> shuffledCategories = new ArrayList<String>(_nzCategories);
			Collections.shuffle(shuffledCategories);
			String str = "";
			for (int i = 0; i < 5; i++) {
				try {
					//create files that are used to store the clues of 
					//the selected five categories
					File dir = new File ("data/games_module");
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
				_fiveRandomCategories.add(shuffledCategories.get(i));
				setFiveRandomClues(shuffledCategories.get(i));
				str = str + shuffledCategories.get(i) + ",";
				_gamesData.put(shuffledCategories.get(i), _fiveRandomClues);
			}
			BashCmdUtil.bashCmdNoOutput(String.format("echo \"%s\" >> data/five_random_categories",str));
		}
		else { // if files already exist, i.e. the game has already started
			BufferedReader reader;
			String str = "";
			try {
				reader = new BufferedReader(new FileReader("data/five_random_categories"));
				String line = reader.readLine();
				str = line;
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String[] splitStr = str.split(",");
			for (int i = 0; i<5;i++) {
				_fiveRandomCategories.add(splitStr[i]);
				setFiveRandomClues(splitStr[i]);
				_gamesData.put(splitStr[i], _fiveRandomClues);
			}
		}
	}

	/**
	 * Get five random categories for the games module.
	 */
	public List<String> getFiveRandomCategories () {
		return _fiveRandomCategories;
	}

	/**
	 * Set five random clues for a given category
	 * @param category the clues are set for
	 */
	public void setFiveRandomClues (String category){
		File file = new File("data/games_module", category);
		// writing 5 random clues to the category file chosen if file is empty
		if (file.length() == 0) {
			List<String> clues = new ArrayList<String>();
			clues = _nzData.get(category);
			Collections.shuffle(clues);
			List<String> temp = new ArrayList<String>();
			for (int i = 0; i<5;i++) {
				temp.add(clues.get(i));
				BashCmdUtil.bashCmdNoOutput(String.format("echo \"%s\" >> data/games_module/\"%s\"",clues.get(i), category));
			}
			_fiveRandomClues = temp;
		}
		else {
			try {
				Scanner myReader = new Scanner(file);
				List<String> temp= new ArrayList<String>();
				while(myReader.hasNextLine()) {
					String fileLine = myReader.nextLine();
					temp.add(fileLine);
				}
				_fiveRandomClues = new ArrayList<String>(temp);
				myReader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * loading a random clue necessary for the 
	 * practice module and the international module
	 * @param module
	 * @param category
	 */
	public void loadRandomClue(String module, String category) {
		List<String> clues = new ArrayList<String>();
		if (module == "nz") {
			clues = _nzData.get(category);
		} else if (module == "int"){
			clues = _intData.get(category);
		}
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
	 * Load the clue from five randomly chosen clues.
	 */
	public void loadClue(String category,int index) {
		String clue = _gamesData.get(category).get(index);
		try {
			_currentClue = new Clue (clue,(index+1)*100);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	
	/**
	 * getter for the current clue selected for the game
	 * @return
	 */
	public Clue getCurrentClue() {
		return _currentClue;
	}

	
	/**
	 * get how many questions in a category have been answered
	 * @param index is the category
	 * @return
	 */
	public int getAnsweredQuestions(int index){
		return _answeredQuestions[index];

	}


	/**
	 * getter for the current Player playing the game
	 * @return
	 */
	public String getCurrentPlayer() {
		return _currentPlayer;
	}

	
	/**
	 * sets the current player playing the game
	 * @param currentPlayer
	 */
	public void setCurrentPlayer(String currentPlayer) {
		_currentPlayer = currentPlayer;
	}


	
	/**
	 * save which questions have been answered already in the game
	 * in an external file in "data/answered_questions"
	 */
	public void setAnsweredQuestions() {
		File file = new File("data/answered_questions");
		if(!file.exists()) {
			BashCmdUtil.bashCmdNoOutput("touch data/answered_questions");

			String answeredQuestions = "";
			for (int i = 0;i<5;i++) {
				answeredQuestions = answeredQuestions + " " + String.valueOf(_answeredQuestions[i]);
			}
			BashCmdUtil.bashCmdNoOutput(String.format("echo \"%s\" >> data/answered_questions", answeredQuestions));

		}
		Scanner myReader;
		try {
			BashCmdUtil.bashCmdNoOutput("touch data/answered_questions");
			myReader = new Scanner(file);
			while(myReader.hasNextLine()) {
				String fileLine = myReader.nextLine();
				String[] splitFileLine = fileLine.trim().split(" ");
				for (int i=0; i<5; i++) {
					_answeredQuestions[i] = Integer.parseInt(splitFileLine[i]);
				}
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
	/**
	 * how the questions being answered are being tracked, by adding 1
	 * to the category index up to 5, where 5 means all questions have
	 * been asnwered
	 * @param index
	 */
	public void addAnsweredQuestions (int index) {

		_answeredQuestions[index]=_answeredQuestions[index]+1;
		BashCmdUtil.bashCmdNoOutput("rm data/answered_questions");
		BashCmdUtil.bashCmdNoOutput("touch data/answered_questions");
		String answeredQuestions = "";
		for (int i = 0;i<5;i++) {
			answeredQuestions = answeredQuestions + " " + String.valueOf(_answeredQuestions[i]);
		}
		BashCmdUtil.bashCmdNoOutput(String.format("echo \"%s\" >> data/answered_questions", answeredQuestions));



	}
	/**
	 * get previous scene, 1 for menu 2 for game question scene
	 * @return _previousScene;
	 */
	public int getPreviousScene(){
		return _previousScene;
	}

	/**
	 * set previous scene, 1 for menu 2 for game question scene
	 */
	public void setPreviousScene(int scene){
		_previousScene = scene;
	}

	/**
	 * get all categories in the game as a list of strings
	 * @return categories
	 */
	public List<String> getCategories(Boolean internationalSelected){
		if (internationalSelected) {
			return _intCategories;
		}
		else {
			return _nzCategories;
		}
	}
	
	/**
	 * get the players winnings from "data/winnings"
	 * @return
	 */
	public int getWinnings() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("data/winnings"));
			String line = reader.readLine();
			_winnings = Integer.parseInt(line.trim());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return _winnings;
	}

	/**
	 * adds to the users current winnings by the input value
	 * @param value
	 */
	public void addWinnings(int value) {
		int winnings = getWinnings();
		winnings = winnings + value;
		String strWinnings = Integer.toString(winnings);
		BashCmdUtil.bashCmdNoOutput("sed -i \"1s/.*/ "+strWinnings+" /\" data/winnings");
	}

	/**
	 * decrease the users current winnings by the input value
	 * @param value
	 */
	public void decreaseWinnings(int value) {
		int winnings = getWinnings();
		winnings = winnings - value;
		String strWinnings = Integer.toString(winnings);
		BashCmdUtil.bashCmdNoOutput("sed -i \"1s/.*/ "+strWinnings+" /\" data/winnings");
	}

	/**
	 * adds player to player rankings
	 */
	public void addPlayerRanking() {
		Player player = new Player(_currentPlayer, _winnings);
		_playerRankings = PlayerRankings.getInstance();
		_playerRankings.add(player);
		_playerRankings.savePlayerRankings();
	}

	
	/**
	 * resets the entire game except for player rankings
	 */
	public void reset(){
		BashCmdUtil.bashCmdNoOutput("rm -r data/games_module");
		BashCmdUtil.bashCmdNoOutput("sed -i \"1s/.*/ "+"0"+" /\" data/winnings");
		BashCmdUtil.bashCmdNoOutput("sed -i \"1s/.*/ "+"175"+" /\" data/tts_speed");
		BashCmdUtil.bashCmdNoOutput("rm data/answered_questions");
		BashCmdUtil.bashCmdNoOutput("rm data/five_random_categories");
		BashCmdUtil.bashCmdNoOutput("rm data/current_player");
		_currentPlayer = null;
		_internationalUnlocked = false;
		_gamesData.clear();
		_fiveRandomCategories.clear();
		for (int i= 0; i<5;i++) {
			_answeredQuestions[i]=0;
		}
		BashCmdUtil.bashCmdNoOutput("touch data/answered_questions");
		initialiseCategories();
		try {
			readCategories();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setFiveRandomCategories();
	}

	
	/**
	 * returns true of the game is completed
	 * otherwise returns false
	 * @return
	 */
	public boolean gameCompleted() {
		int[] expected = {5,5,5,5,5};
		if(Arrays.equals(_answeredQuestions, expected)) {
			return true;
		}
		return false;
	}	

	
	/**
	 * saves the name of the current player
	 */
	public void saveCurrentPlayer() {
		BashCmdUtil.bashCmdNoOutput("touch data/current_player");
		BashCmdUtil.bashCmdNoOutput(String.format("echo \"%s\" >> data/current_player",_currentPlayer));
	}

	
	/**
	 * loads the name of the current player in 'data/current_player'
	 */
	public void loadCurrentPlayer() {
		Scanner sc;
		File file = new File("data/current_player");
		if(file.exists()) {
			try {
				sc = new Scanner(new File("data/current_player"));
				if(sc.hasNextLine()) {
					String player = sc.nextLine();
					_currentPlayer= player;
				}
				else {
					_currentPlayer = null;
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			BashCmdUtil.bashCmdNoOutput("touch data/current_player");
			_currentPlayer = null;
		}
	}

	
	/**
	 * returns true if the internation mode is unlocked,
	 * that is 2 full categories are completed in game
	 * returns false otherwise
	 * @return
	 */
	public boolean InternationalUnlocked() {
		int count = 0;
		for (int category: _answeredQuestions) {
			if (category == 5) {
				count++;
			}
		}
		if (count >= 2) {
			_internationalUnlocked = true;
		}
		else {
			_internationalUnlocked = false;
		}
		saveInternationalUnlocked();
		return _internationalUnlocked;
	}


	/**
	 * saves whether the international mode is unlocked
	 */
	public void saveInternationalUnlocked() {
		BashCmdUtil.bashCmdNoOutput("touch data/internatonal_unlocked");
		BashCmdUtil.bashCmdNoOutput("> data/internatonal_unlocked");
		BashCmdUtil.bashCmdNoOutput(String.format("echo \"%s\" >> data/internatonal_unlocked",Boolean.toString(_internationalUnlocked)));
	}


	/**
	 * loads and stores whether the international mode is unlocked
	 */
	public void loadInternationalUnlocked() {
		Scanner sc;
		File file = new File("data/internatonal_unlocked");
		if(file.exists()) {
			try {
				sc = new Scanner(new File("data/internatonal_unlocked"));
				if(sc.hasNextLine()) {
					String interntionalUnlocked = sc.nextLine();
					_internationalUnlocked = Boolean.parseBoolean(interntionalUnlocked.trim());
				}
				else {
					_internationalUnlocked = false;
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			BashCmdUtil.bashCmdNoOutput("data/internatonal_unlocked");
			_internationalUnlocked = false;
		}
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
