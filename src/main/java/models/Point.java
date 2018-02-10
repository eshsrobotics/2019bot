package models;

public class Point {
	
	public double x;
	public double y;
	
	public Point(double x,  double y) {
		this.x = x;
		this.y = y;
	}
	
	public Point add(Vector2 vector) {
		return new Point(x + vector.x, y + vector.y);
	}
	
	public Vector2 toVector() {
		return new Vector2(x, y);
	}
}
