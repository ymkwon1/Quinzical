package quinzical.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import quinzical.util.BashCmdUtil;

/**
 * The PlayerRankings class stores a collection of Players
 * that can be ranked with each other via the amount of points they have
 * this is used for the tableView component in PlayerRankingViewController class
 * It is also responsible for saving and loading of the Players as an external file
 * 
 * @author Dylan Xin, Young Min Kwon
 *
 */
public class PlayerRankings {
	private ObservableList<Player> _playerList = FXCollections.observableArrayList();
	private static PlayerRankings single_instance = null;
	
	/**
	 * constructor for PlayerRankings, loads the previously saved
	 * player rankings from an external file
	 */
	private PlayerRankings(){
		loadPlayerRankings();
	}
	
	
	/**
	 * adds a Player to the rankings
	 * @param player
	 */
	public void add(String playerName, Integer playerPoints) {
		Player player = new Player(playerName, playerPoints);
		_playerList.add(player);
		savePlayerRankings();
	}
	
	/**
	 * getter for the list of players in the rankings
	 * @return
	 */
	public ObservableList<Player> getPlayerList() {
		return _playerList;
	}
	
	
	/**
	 * saves the data of the player rankings to an external file
	 * in the data folder with the file name 'player_rankings'
	 */
	public void savePlayerRankings() {
		BashCmdUtil.bashCmdNoOutput("touch data/player_rankings");
		BashCmdUtil.bashCmdNoOutput("> data/player_rankings");
		for (Player player: _playerList) {
			BashCmdUtil.bashCmdNoOutput(String.format("echo \"%s / %d\" >> data/player_rankings", player.getName(), player.getPoints()));
		}
	}
	
	/**
	 * loads the data of the player rankings from an external file
	 * in the data folder with the file name 'player_rankings'
	 */
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
	
	
	/**
	 * resets the player rankings by removing all players
	 */
	public void reset() {
		_playerList.clear();
		BashCmdUtil.bashCmdNoOutput("> data/player_rankings");
	}
	
	
	/**
	 * this method creates only a single instance of the PlayerRankings class
	 * making it a singleton class
	 * @return
	 */
	public static PlayerRankings getInstance() {
		if (single_instance == null) {
			single_instance = new PlayerRankings();
		}
		return single_instance;
	}
}
