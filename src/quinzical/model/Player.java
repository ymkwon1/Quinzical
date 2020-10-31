package quinzical.model;

public class Player {
	private String _name;
	private Integer _points;
	
	public Player(String name, Integer points) {
		_name = name;
		_points = points;
	}
	
	public String getName() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	public Integer getPoints() {
		return _points;
	}
	
	public void setPoints(Integer points) {
		_points = points;
	}
}
