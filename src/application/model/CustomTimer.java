package application.model;


public class CustomTimer {
	private final static int DEFAULT_TIMER = 60;
	private int _secondsLeft = DEFAULT_TIMER;
	private boolean _hasStarted = false;
	private static CustomTimer single_instance = null;


	private CustomTimer() {
		_hasStarted = true;
	}
	
	
	public void countDown() {
		if (_secondsLeft > 0) {
			_secondsLeft--;
		}
	}
	
	public void resetTimer() {
		_secondsLeft = DEFAULT_TIMER;
		_hasStarted = false;
	}
	
	public int getSecondsLeft() {
		return _secondsLeft;
	}
	
	public boolean hasStarted() {
		return _hasStarted;
	}
	
	public static CustomTimer getInstance() {
		if (single_instance == null) {
			single_instance = new CustomTimer();
		}
		return single_instance;
	}
}

