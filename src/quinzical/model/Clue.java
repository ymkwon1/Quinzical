package quinzical.model;
/**
 * The Clue class models and stores the data on the games Quinzicals'
 * question and answers 
 * @author Dylan Xin, Young Min Kwon
 */

public class Clue {
	private String _clue;
	private String[] _answers;
	private String _answer;
	private String _question;
	private String _tag;
	private String _hint;
	private int _value;
	private int _attempts;
	private TextToSpeech _tts;
	
	/**
	 * Constructor for Clue class without the clue being associated with a value.
	 * It correctly formats the data read from the files into the three strings,
	 * a question string, a tag string, and an answer string. the strings are stored 
	 * without leading or trailing white spaces, commas or full stops
	 * @param clue
	 * @throws Exception
	 */
	public Clue (String clue) throws Exception {
		_tts = new TextToSpeech();
		_clue = clue;
		String[] splitString = _clue.split("[\\(\\)]");
		String question = splitString[0].trim();
		String answer = splitString[2].trim();
		_question = question.replaceAll("[,]$|[.]$","");
		_answer = answer.replaceAll("[,]$|[.]$","");
		_answers = answer.split("/");
		_tag = splitString[1].trim();
		_hint = "";
		_attempts = 0;
	}
	
	/**
	 * Constructor for Clue class but the clue is associated with a value.
	 * It correctly formats the data read from the files into the three strings,
	 * a question string, a tag string, and an answer string. the strings are stored 
	 * without leading or trailing white spaces, commas or full stops
	 * @param clue
	 * @param value
	 * @throws Exception
	 */
	public Clue (String clue,int value) throws Exception {
		_tts = new TextToSpeech();
		_clue = clue;
		String[] splitString = _clue.split("[\\(\\)]");
		String question = splitString[0].trim();
		String answer = splitString[2].trim();
		_question = question.replaceAll("[,]$|[.]$","");
		_answer = answer.replaceAll("[,]$|[.]$","");
		_answers = answer.split("/");
		_tag = splitString[1].trim();
		_hint = "";
		_attempts = 0;
		_value=value;
	}
	
	/**
	 * Reads out the question with the TTS system
	 */
	public void ttsQuestion() {
		_tts.tts(_question);
	}
	
	/**
	 * Reads out the answer with the TTS system
	 */
	public void ttsAnswer() {
		_tts.tts(String.format("The answer was %s", _answer));
	}
	
	
	/**
	 * getter method to return the question of the clue
	 * @return String, the question
	 */
	public String getQuestion() {
		return _question;
	}
	
	/**
	 * getter method to return the tag as a string
	 * @return String, the tag
	 */
	public String getTag() {
		return _tag;
	}
	
	
	/**
	 * formats the text for the hint used in the practice model and returns
	 * it as a string
	 * @return String hint text
	 */
	public String getHint() {
		if (_attempts == 2) {
			_hint = String.format("Hint: The answer starts with the letter \"%s\"", _answer.charAt(0));
		} 
		else {
			_hint = "";
		}
		return _hint;
	}
	
	
	/**
	 * returns the formatted text for the number of attempts the user 
	 * has left to answer a question in the paactice module
	 * @return String attempts left
	 */
	public String getAttempts () {
		return String.format("Attempts: %s/3",_attempts);
	}
	
	/**
	 * formatted text to show the user what the answer was in the 
	 * practice module
	 * @return the answer
	 */
	public String showAnswer () {
		if (_attempts == 3) {
			return String.format("The answer was %s", getAnswers());
		}
		else {
			return null;
		}
	}
	
	/**
	 * getter method for the answer of the clue
	 * @return String answer of the clue
	 */
	public String getAnswers () {
		return _answer;
	}
	
	/**
	 * getter method for the value of the clue
	 * @return int value of the clue
	 */
	public int getValue (){
		return _value;
	}
	
	
	/**
	 *resets the number of attempts left for the user to answer
	 *questions in the practice module 
	 */
	public void resetAttempts () {
		_attempts = 0;
	}
	
	
	/**
	 * calculates the number of attempts left
	 * the user has left in answering a question in 
	 * the practice module
	 * @return
	 */
	public int attemptsLeft () {
		return 3-_attempts; 
	}
	
	/**
	 * checkAnswers check if the string userInput is equal case insensitive 
	 * to the current answer
	 * @param userInput is a string
	 * @return true if userInput is the correct answer false otherwise
	 */
	public boolean checkAnswer(String userInput) {
			for (String answer: _answers) {
				if (userInput.equalsIgnoreCase(answer.replaceAll("[,]$|[.]$","").trim())) {
					_attempts=0;
					return true;
				}
			}
			_attempts++;
			return false;	
	}

}
