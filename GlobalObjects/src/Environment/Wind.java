package Environment;

public class Wind {
	private WindDirecion direction;
	private double velocity;
	
	public Wind(double windVelocity, String windDirection) {
		this.direction = WindDirecion.valueOf(windDirection);
		this.velocity = windVelocity;
	}

	public WindDirecion getDirection() {
		return direction;
	}

	public void setDirection(WindDirecion direction) {
		this.direction = direction;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

}

enum WindDirecion{
	N,NW,NE, S, SW, SE, E, W;
}
