package application.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PlayerRankings {
	private ObservableList<Player> _playerList = FXCollections.observableArrayList();
	private static PlayerRankings single_instance = null;
	
	private PlayerRankings(){
		loadPlayerRankings();
	}
	
	public void add(Player player) {
		_playerList.add(player);
	}
	
	public ObservableList<Player> getPlayerList() {
		return _playerList;
	}
	
	public void savePlayerRankings() {
		BashCmdUtil.bashCmdNoOutput("touch data/player_rankings");
		BashCmdUtil.bashCmdNoOutput("> data/player_rankings");
		for (Player player: _playerList) {
			BashCmdUtil.bashCmdNoOutput(String.format("echo \"%s / %d\" >> data/player_rankings", player.getName(), player.getPoints()));
		}
	}
	
	public void loadPlayerRankings() {
		Scanner sc;
		File file = new File("data/player_rankings");
		if(file.exists()) {
			try {
				sc = new Scanner(new File("data/player_rankings"));
				while(sc.hasNextLine()) {
					String line = sc.nextLine();
					String[] playerData = line.split("/");
					Player player = new Player(playerData[0].trim(), Integer.valueOf(playerData[1].trim()));
					_playerList.add(player);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			BashCmdUtil.bashCmdNoOutput("touch data/player_rankings");
		}
	}
	
	public void reset() {
		_playerList.clear();
		BashCmdUtil.bashCmdNoOutput("> data/player_rankings");
	}
	
	public static PlayerRankings getInstance() {
		if (single_instance == null) {
			single_instance = new PlayerRankings();
		}
		return single_instance;
	}
}
