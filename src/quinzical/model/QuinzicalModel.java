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



public class QuinzicalModel {

	// for singleton class
	private static QuinzicalModel instance = null;
	private List<String> _categories;
	private List<String> _fiveRandomCategories = new ArrayList<String>();
	private List<String> _fiveRandomClues;
	private Map<String, List<String>> _practiceData = new HashMap<String, List<String>>();
	private Map<String, List<String>> _gamesData = new HashMap<String, List<String>>();
	private Clue _currentClue;
	private String _currentPlayer = null;
	private int [] _answeredQuestions = {0,0,0,0,0};
	private int _winnings = 0;
	private int _ttsSpeed = 175;
	private int _previousScene = 1;
	private PlayerRankings _playerRankings;
	private boolean _internationalUnlocked = false;

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
		loadTTSSpeed();
		loadInternationalUnlocked();
	}

	/**
	 * using bash command to find the names of the categories in the category folder
	 * each category is added to the list
	 */
	private void initialiseCategories() {
		_categories = BashCmdUtil.bashCmdHasOutput("ls categories/nz");
		//		System.out.println(_categories);
	}

	/**
	 * read the data from the category files 
	 * and store into 2d array data in the form of i.e.
	 * [animals line line line line line
	 * countries line line line line line]
	 */
	private void readCategories() throws Exception {
		if (_categories.size() > 0) {
			for (String category: _categories) {

				try {
					File file = new File("categories/nz/", category);
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
		//create a file to store the selected five categories
		BashCmdUtil.bashCmdNoOutput("mkdir -p data");
		File five_random_categories = new File ("data/five_random_categories");
		if (!five_random_categories.exists()) {
			BashCmdUtil.bashCmdNoOutput("touch data/five_random_categories");
		}
		if (five_random_categories.length() == 0) {
			//			List<String> temp = new ArrayList<String> ();
			List<String> shuffledCategories = new ArrayList<String>(_categories);
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
		else {
			BufferedReader reader;
			String str = "";
			try {
				reader = new BufferedReader(new FileReader("data/five_random_categories"));
				String line = reader.readLine();
				str = line;
				//System.out.println(str);
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
	 * Set five random clues and answers of a category
	 */
	public void setFiveRandomClues (String category){
		File file = new File("data/games_module", category);
		if (file.length() == 0) {
			List<String> clues = new ArrayList<String>();
			clues = _practiceData.get(category);
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

	public Clue getCurrentClue() {
		return _currentClue;
	}

	public int getAnsweredQuestions(int index){
		return _answeredQuestions[index];

	}

	public String getCurrentPlayer() {
		return _currentPlayer;
	}

	public void setCurrentPlayer(String currentPlayer) {
		_currentPlayer = currentPlayer;
	}


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
	public List<String> getCategories(){
		return _categories;
	}

	/**
	 * text to speech a string
	 */
	public void tts(String string) {
		BashCmdUtil.bashCmdNoOutput(String.format("espeak \"%s\" --stdout -s %d | aplay", string, _ttsSpeed));

	}

	public void stopTts () {
		//		String command = "pkill -f espeak";
		//		BashCmdUtil.bashCmdNoOutput(command);
		Stream<ProcessHandle> descendents = ProcessHandle.current().descendants();
		descendents.filter(ProcessHandle::isAlive).forEach(ph -> {
			ph.destroy();
		});
	}

	/**
	 * increase tts speed
	 */
	public void increaseTTSSpeed() {
		_ttsSpeed = _ttsSpeed + 10;
		BashCmdUtil.bashCmdNoOutput("> data/tts_speed");
		BashCmdUtil.bashCmdNoOutput(String.format("echo \"%d\" >> data/tts_speed", _ttsSpeed));
	}

	/**
	 * decrease tts speed
	 */
	public void decreaseTTSSpeed() {
		_ttsSpeed = _ttsSpeed - 10;
		BashCmdUtil.bashCmdNoOutput("> data/tts_speed");
		BashCmdUtil.bashCmdNoOutput(String.format("echo \"%d\" >> data/tts_speed", _ttsSpeed));
	}

	public int getTTSSpeed() {
		return _ttsSpeed;
	}
	/**
	 * get tts speed
	 * @return ttsSpeed
	 */
	public void loadTTSSpeed() {
		Scanner sc;
		File file = new File("data/tts_speed");
		if(file.exists()) {
			try {
				sc = new Scanner(new File("data/tts_speed"));
				if(sc.hasNextLine()) {
					String speed = sc.nextLine();
					_ttsSpeed = Integer.parseInt(speed.trim());
				}
				else {
					BashCmdUtil.bashCmdNoOutput("> data/tts_speed");
					BashCmdUtil.bashCmdNoOutput("echo \"175\" >> data/tts_speed");
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			BashCmdUtil.bashCmdNoOutput("touch data/tts_speed");
			BashCmdUtil.bashCmdNoOutput("> data/tts_speed");
			BashCmdUtil.bashCmdNoOutput("echo \"175\" >> data/tts_speed");
		}
	}

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

	public void addWinnings(int value) {
		int winnings = getWinnings();
		winnings = winnings + value;
		String strWinnings = Integer.toString(winnings);
		BashCmdUtil.bashCmdNoOutput("sed -i \"1s/.*/ "+strWinnings+" /\" data/winnings");
	}

	public void decreaseWinnings(int value) {
		int winnings = getWinnings();
		winnings = winnings - value;
		String strWinnings = Integer.toString(winnings);
		BashCmdUtil.bashCmdNoOutput("sed -i \"1s/.*/ "+strWinnings+" /\" data/winnings");
	}

	public void addPlayerRanking() {
		Player player = new Player(_currentPlayer, _winnings);
		_playerRankings = PlayerRankings.getInstance();
		_playerRankings.add(player);
		_playerRankings.savePlayerRankings();
	}

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

	public boolean gameCompleted() {
		int[] expected = {5,5,5,5,5};
		if(Arrays.equals(_answeredQuestions, expected)) {
			return true;
		}
		return false;
	}	

	public void saveCurrentPlayer() {
		BashCmdUtil.bashCmdNoOutput("touch data/current_player");
		BashCmdUtil.bashCmdNoOutput(String.format("echo \"%s\" >> data/current_player",_currentPlayer));
	}

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


	public void saveInternationalUnlocked() {
		BashCmdUtil.bashCmdNoOutput("touch data/internatonal_unlocked");
		BashCmdUtil.bashCmdNoOutput("> data/internatonal_unlocked");
		BashCmdUtil.bashCmdNoOutput(String.format("echo \"%s\" >> data/internatonal_unlocked",Boolean.toString(_internationalUnlocked)));
	}

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
