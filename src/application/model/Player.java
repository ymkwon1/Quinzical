package application.model;

public class Player {
	private String _name;
	private String _points;
	
	public Player(String name, String points) {
		_name = name;
		_points = points;
	}
	
	public String getName() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	public String getPoints() {
		return _points;
	}
	
	public void setPoints(String score) {
		_name = _points;
	}
}
