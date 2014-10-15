package agent;

import java.util.ArrayList;

import capability.BaseDevice;
import capability.GPS;
import capability.InfraredSensor;
import capability.Sensor;
import capability.VisionSensor;

public class BaseAgent {
	
	private String agentID;
	private AgentType agentType;
	private double operatorCost;
	private double deprecationCost;
	private double fuelCost;
	private ArrayList<BaseDevice> deviceList;
	private double normalMovingDistance;
	private double rainMovingDistance;
	private double mountainMovingDistance;
	private double mountainRainMovingDistance;
	private int currentOptFuel;
	private int currentHurFuel;
	private int fuelEfficiency;
	
	public BaseAgent(String agentID, String agentType, double operatorCost,
			double deprecationCost, double fuelCost,
			double visionSensitivity, double infraredSensitivity, double GPSSensitibity, 
			double visionDeprecation, double infraredDeprecation, double GPSDeprecation,
			double normalMovingDistance,
			double rainMovingDistance, double mountainMovingDistance,
			double mountainRainMovingDistance, int initialFuel, int fuelEfficiency) {
		super();
		deviceList = new ArrayList<BaseDevice>();
		this.agentID = agentID;
		this.agentType = AgentType.valueOf(agentType);
		this.operatorCost = operatorCost;
		this.deprecationCost = deprecationCost;
		this.fuelCost = fuelCost;
		deviceList.add(new VisionSensor("VS"+deviceList.size(),VisionSensor.class.getName(),visionDeprecation,visionSensitivity ));
		deviceList.add(new InfraredSensor("IS"+deviceList.size(),InfraredSensor.class.getName(),infraredDeprecation,infraredSensitivity ));
		deviceList.add(new GPS("GPS"+deviceList.size(),GPS.class.getName(),GPSDeprecation,GPSSensitibity ));
		//this.deviceList = deviceList;
		this.normalMovingDistance = normalMovingDistance;
		this.rainMovingDistance = rainMovingDistance;
		this.mountainMovingDistance = mountainMovingDistance;
		this.mountainRainMovingDistance = mountainRainMovingDistance;
		this.currentHurFuel = initialFuel;
		this.currentOptFuel = initialFuel;
		this.fuelEfficiency = fuelEfficiency;
		
	}

	public String getAgentID() {
		return agentID;
	}

	public void setAgentID(String agentID) {
		this.agentID = agentID;
	}

	public AgentType getAgentType() {
		return agentType;
	}

	public void setAgentType(String agentType) {
		this.agentType = AgentType.valueOf(agentType);
	}

	public double getOperatorCost() {
		return operatorCost;
	}

	public void setOperatorCost(double operatorCost) {
		this.operatorCost = operatorCost;
	}

	public double getDeprecationCost() {
		return deprecationCost;
	}

	public void setDeprecationCost(double deprecationCost) {
		this.deprecationCost = deprecationCost;
	}

	public double getFuelCost() {
		return fuelCost;
	}

	public void setFuelCost(double fuelCost) {
		this.fuelCost = fuelCost;
	}

	public ArrayList<BaseDevice> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(ArrayList<BaseDevice> deviceList) {
		this.deviceList = deviceList;
	}

	public double getNormalMovingDistance() {
		return normalMovingDistance;
	}

	public void setNormalMovingDistance(double normalMovingDistance) {
		this.normalMovingDistance = normalMovingDistance;
	}

	public double getRainMovingDistance() {
		return rainMovingDistance;
	}

	public void setRainMovingDistance(double rainMovingDistance) {
		this.rainMovingDistance = rainMovingDistance;
	}

	public double getMountainMovingDistance() {
		return mountainMovingDistance;
	}

	public void setMountainMovingDistance(double mountainMovingDistance) {
		this.mountainMovingDistance = mountainMovingDistance;
	}

	public double getMountainRainMovingDistance() {
		return mountainRainMovingDistance;
	}

	public void setMountainRainMovingDistance(double mountainRainMovingDistance) {
		this.mountainRainMovingDistance = mountainRainMovingDistance;
	}

	public int getCurrentOptFuel() {
		return currentOptFuel;
	}

	public void setCurrentOptFuel(int currentOptFuel) {
		this.currentOptFuel = currentOptFuel;
	}

	public int getCurrentHurFuel() {
		return currentHurFuel;
	}

	public void setCurrentHurFuel(int currentHurFuel) {
		this.currentHurFuel = currentHurFuel;
	}

	public int getFuelEfficiency() {
		return fuelEfficiency;
	}

	public void setFuelEfficiency(int fuelEfficiency) {
		this.fuelEfficiency = fuelEfficiency;
	}

	public double getSumOfDeviceDeprecation()
	{
		double deprecation = 0.0;
		for(BaseDevice d : this.getDeviceList())
		{
			deprecation = deprecation + d.getDeprecationCost();
		}
		
		return deprecation;
	}
	
	public double getAvgOfSensor()
	{
		double retVal = 0.0;
		int count = 0;
		
		for(BaseDevice d : this.getDeviceList())
		{
			try{
				Sensor sensor = (Sensor) d;
				retVal = retVal + sensor.getSensitivity();
				count = count + 1;
			}catch(Exception e){continue;}
		}
		
		return retVal / count;
		
	}
	
	public enum AgentType{
		Jeep, UAV, Helicopter, AirPlane
	}
	
	public String toString()
	{
		return this.agentID;
	}
	
	
}
