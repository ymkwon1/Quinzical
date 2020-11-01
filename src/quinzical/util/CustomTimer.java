package quinzical.util;


/**
 * CustomTime class was made so that the timer can be tracked through multiple screens
 * @author se206-2020
 *
 */
public class CustomTimer {
	private final static int DEFAULT_TIMER = 60;
	private int _secondsLeft = DEFAULT_TIMER;
	private static CustomTimer single_instance = null;


	
	/**
	 * private constructor needed for singleton class
	 */
	private CustomTimer() {
	}
	
	
	/**
	 * Subtracts one from the number of seconds left
	 */
	public void countDown() {
		if (_secondsLeft > 0) {
			_secondsLeft--;
		}
	}
	
	/**
	 * resets timer to the default timer
	 */
	public void resetTimer() {
		_secondsLeft = DEFAULT_TIMER;
	}
	
	
	/**
	 * gets seconds left
	 * @return
	 */
	public int getSecondsLeft() {
		return _secondsLeft;
	}
	
	
	/**
	 * creates a single instance of the CustomTime class
	 * @return
	 */
	public static CustomTimer getInstance() {
		if (single_instance == null) {
			single_instance = new CustomTimer();
		}
		return single_instance;
	}
}

