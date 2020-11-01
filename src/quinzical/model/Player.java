package quinzical.model;


/**
 * The Player class models the user playing the game
 * it keeps track of their name and the number of points they have
 * @author Dylan Xin, Young Min Kwon
 *
 */

public class Player {
	private String _name;
	private Integer _points;
	
	/**
	 * constructor for player class
	 * @param name, the name of the player as a string
	 * @param points, the amount of points the user had as an integer
	 */
	public Player(String name, Integer points) {
		_name = name;
		_points = points;
	}
	
	/**
	 * getter method for players name
	 * @return player name
	 */
	public String getName() {
		return _name;
	}
	
	
	/**
	 * sets the players name
	 * @param name, the players name as a String
	 */
	public void setName(String name) {
		_name = name;
	}
	
	/**
	 * getter method for the players points
	 * @return
	 */
	public Integer getPoints() {
		return _points;
	}
	
	/**
	 * sets the players points
	 * @param points, the points the user had as an Integer
	 */
	public void setPoints(Integer points) {
		_points = points;
	}
}
