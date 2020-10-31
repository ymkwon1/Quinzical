package quinzical.model;


public class Clue {
	private String _clue;
	private String[] _answers;
	private String _answer;
	private String _question;
	private String _tag;
	private String _hint;
	private int _value;
	private int _attempts;
	private QuinzicalModel _model;
	
	public Clue (String clue) throws Exception {
		_model = QuinzicalModel.createInstance();
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
	
	public Clue (String clue,int value) throws Exception {
		_model = QuinzicalModel.createInstance();
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
	
	public void ttsQuestion() {
		_model.tts(_question);
	}
	
	public void ttsAnswer() {
		_model.tts(String.format("The answer was %s", _answer));
	}
	
	public String getQuestion() {
		return _question;
	}
	
	public String getTag() {
		return _tag;
	}
	
	public String getHint() {
		if (_attempts == 2) {
			_hint = String.format("Hint: The answer starts with the letter \"%s\"", _answer.charAt(0));
		} 
		else {
			_hint = "";
		}
		return _hint;
	}
	
	public String getAttempts () {
		return String.format("Attempts: %s/3",_attempts);
	}
	
	public String getAnswer () {
		if (_attempts == 3) {
			return String.format("The answer was %s", getAnswers());
		}
		else {
			return null;
		}
	}
	
	public String getAnswers () {
		return _answer;
	}
	
	public int getValue (){
		return _value;
	}
	
	public void resetAttempts () {
		_attempts = 0;
	}
	
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
