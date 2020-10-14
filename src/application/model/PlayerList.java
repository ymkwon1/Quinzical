package application.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PlayerList {
	private ObservableList<Player> _playerList = FXCollections.observableArrayList();
	private static PlayerList single_instance = null;
	
	private PlayerList(){
	}
	
	public void add(Player player) {
		_playerList.add(player);
	}
	
	public ObservableList<Player> getPlayerList() {
		return _playerList;
	}
	
	public static PlayerList getInstace() {
		if (single_instance == null) {
			single_instance = new PlayerList();
		}
		return single_instance;
	}
	
}
