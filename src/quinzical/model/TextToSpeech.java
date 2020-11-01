package quinzical.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.stream.Stream;

import quinzical.util.BashCmdUtil;

public class TextToSpeech {
	private int _ttsSpeed = 175;
	
	public TextToSpeech() {
		loadTTSSpeed();
	}
	
	/**
	 * text to speech a string
	 */
	public void tts(String string) {
		loadTTSSpeed();
		BashCmdUtil.bashCmdNoOutput(String.format("espeak \"%s\" --stdout -s %d | aplay", string, _ttsSpeed));

	}

	/**
	 * stops the TTS system from speaking
	 */
	public void stopTts () {
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
		saveTTSSpeed();
	}

	/**
	 * decrease tts speed
	 */
	public void decreaseTTSSpeed() {
		_ttsSpeed = _ttsSpeed - 10;
		saveTTSSpeed();
	}

	/**
	 * saves tts speed in an external file
	 */
	public void saveTTSSpeed() {
		BashCmdUtil.bashCmdNoOutput("> data/tts_speed");
		BashCmdUtil.bashCmdNoOutput(String.format("echo \"%d\" >> data/tts_speed", _ttsSpeed));
	}
	
	/**
	 * gets the tts speed
	 * @return
	 */
	public int getTTSSpeed() {
		return _ttsSpeed;
	}
	
	/**
	 * loads the saved tts speed from "data/tts_speed"
	 * and stores it
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
}
