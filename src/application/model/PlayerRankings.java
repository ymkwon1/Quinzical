package application.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PlayerRankings {
	private ObservableList<Player> _playerList = FXCollections.observableArrayList();
	private static PlayerRankings single_instance = null;
	
	private PlayerRankings(){
	}
	
	public void add(Player player) {
		_playerList.add(player);
	}
	
	public ObservableList<Player> getPlayerList() {
		return _playerList;
	}
	
	public static PlayerRankings getInstace() {
		if (single_instance == null) {
			single_instance = new PlayerRankings();
		}
		return single_instance;
	}
	
}
