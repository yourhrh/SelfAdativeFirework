package Environment;

public class Weather {
	
	private WeatherCondition condition;
	private Wind wind;
	private  double visibility;
	
	public Weather(String condition, double windVelocity, String windDirection, double visibility) {
		this.condition = WeatherCondition.valueOf(condition);
		this.wind = new Wind(windVelocity, windDirection);
		this.visibility = visibility;
	}

	public WeatherCondition getCondition() {
		return condition;
	}

	public void setCondition(WeatherCondition condition) {
		this.condition = condition;
	}

	public Wind getWind() {
		return wind;
	}

	public void setWind(Wind wind) {
		this.wind = wind;
	}

	public double getVisibility() {
		return visibility;
	}

	public void setVisibility(double visibility) {
		this.visibility = visibility;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Velocity : " + wind.getVelocity() + " Direction : " + wind.getDirection().toString());
		
		return sb.toString();
	}
	
	public enum WeatherCondition {
		Sunny, Rainy, Cloudy, Snowy
	}
}


