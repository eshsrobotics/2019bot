package models;

public class Vector2 {
	
	public double x;
	public double y;
	
	public Vector2(Vector2 vector) {
		this.x = vector.x;
		this.y = vector.y;
	}
	
	public Vector2(double x, double y) {
		 this.x = x;
		 this.y = y;
	}
	
	public Vector2 add(Vector2 vector) {
		return new Vector2(x + vector.x, y + vector.y);
	}
	
	public Vector2 mult(double scalar) {
		return new Vector2(x * scalar, y * scalar);
	}
	
	public double angle(Vector2 dest) {
		return 360 - (Math.atan(x / y));
	}
	
	public double dist(Vector2 dest) {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public Point convert() {
		return new Point(x, y);
	}

}
