package application.model;


public class CustomTimer {
	private final static int DEFAULT_TIMER = 60;
	private int _secondsLeft = DEFAULT_TIMER;
	private static CustomTimer single_instance = null;


	private CustomTimer() {
	}
	
	
	public void countDown() {
		if (_secondsLeft > 0) {
			_secondsLeft--;
		}
	}
	
	public void resetTimer() {
		_secondsLeft = DEFAULT_TIMER;
	}
	
	public int getSecondsLeft() {
		return _secondsLeft;
	}
	
	
	public static CustomTimer getInstance() {
		if (single_instance == null) {
			single_instance = new CustomTimer();
		}
		return single_instance;
	}
}

