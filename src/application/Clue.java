package application;

import application.model.QuinzicalModel;
import javafx.scene.control.Label;

public class Clue {
	private String _clue;
	private String[] _answers;
	private String _answer;
	private String _question;
	private String _category;
	private String _tag;
	private String _hint;
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
	
	public void ttsQuestion() {
		_model.tts(_question);
	}
	
	public void ttsAnswer() {
		_model.tts(String.format("The answer was %s", _answer));
	}
	
	public void setQuestionLabel(Label label) {
		label.setText(_question);
	}
	
	public void setTagLabel (Label label) {
		label.setText(_tag);
	}
	
	public void setHintLabel (Label label) {
		if (_attempts == 2) {
			_hint = String.format("Hint: The answer starts with the letter \"%s\"", _answer.charAt(0));
		} 
		else {
			_hint = "";
		}
		label.setText(_hint);
	}
	
	public void setAttemptsLabel (Label label) {
		label.setText (String.format("Attempts: %s/3",_attempts));
	}
	
	public void setAnswerLabel (Label label) {
		if (_attempts == 3) {
			label.setText(String.format("The answer was %s", _answer));
		}
		else {
			label.setText(null);
		}
	}
	
	public void resetAttempts () {
		_attempts = 0;
	}
	
	public int attemptsLeft () {
		return 3-_attempts; 
	}
	
	public boolean checkAnswer(String userInput) {
		for (String answer: _answers) {
			if (userInput.equalsIgnoreCase(answer.trim())) {
				_attempts=0;
				return true;
			}
		}
		_attempts++;
		return false;
	}

}
