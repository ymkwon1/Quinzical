package application.model;

public class Player {
	private String _player;
	private String _finalScore;
	
	public Player(String player, String finalScore) {
		_player = player;
		_finalScore = finalScore;
	}
	
	
	public String getPlayer() {
		return _player;
	}
	
	public void setPlayer(String player) {
		_player = player;
	}
	
	public String getFinalScore() {
		return _finalScore;
	}
	
	public void setFinalScore(String score) {
		_player = _finalScore;
	}
}
