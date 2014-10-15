package capability;

import capability.BaseDevice;

public class Sensor extends BaseDevice{
	private double sensitivity;
	
	public Sensor(String deviceID, String deviceType, double deprecationCost,
			double sensitivity) {
		super(deviceID, deviceType, deprecationCost);
		
		this.sensitivity = sensitivity;
	}

	public double getSensitivity() {
		return sensitivity;
	}

	public void setSensitivity(double sensitivity) {
		this.sensitivity = sensitivity;
	}
	
	
	
	
}
