package globalvariable.services;

import java.util.ArrayList;

import agent.BaseAgent;
//import Util.LoggerUtil;

public class AgentInstances {
	private static ArrayList<BaseAgent> agentList = new ArrayList<BaseAgent>();
	private static double maxCost = 0.0;
	private static double minCost = 0.0;

	public static ArrayList<BaseAgent> getAgentList() {
		return agentList;
	}

	public static void setAgentList(ArrayList<BaseAgent> agentList) {
		AgentInstances.agentList = agentList;
	}

	public static void printAgents() {
		for(BaseAgent agent : agentList)
		{
		//	LoggerUtil.write(LoggerUtil.LOGPANE, agent.getAgentType().name());
		}
	}

	public static ArrayList<String> getAgentIDList() {
		ArrayList<String> idList = new ArrayList<String>();
		for(BaseAgent a : agentList)
		{
			idList.add(a.getAgentID());
		}
		
		//idList.add(null);
		
		return idList;
	}
	
	public static BaseAgent getAgent(String id)
	{
		for(BaseAgent a : agentList)
		{
			if(a.getAgentID().equals(id))
			{
				return a;
			}
		}
		
		return null;
	}
	
	public static double getMaxFuelCost()
	{
		double retVal = 0.0;
		for(BaseAgent a : agentList)
		{
			if(retVal < a.getFuelCost())
				retVal = a.getFuelCost();
		}
		
		return retVal;
	}
	
	public static double getMinFuelCost()
	{
		double retVal = 1000.0;
		for(BaseAgent a : agentList)
		{
			if(retVal > a.getFuelCost())
				retVal = a.getFuelCost();
		}
		
		return retVal;
	}
	
	public static double getMaxDeprecationCost()
	{
		double retVal = 0.0;
		for(BaseAgent a : agentList)
		{
			if(retVal < a.getDeprecationCost())
				retVal = a.getDeprecationCost();
		}
		
		return retVal;
	}
	
	public static double getMinDeprecationCost()
	{
		double retVal = 1000.0;
		for(BaseAgent a : agentList)
		{
			if(retVal > a.getDeprecationCost())
				retVal = a.getDeprecationCost();
		}
		
		return retVal;
	}
	
	public static double getMaxDeviceDeprecationCost()
	{
		double retVal = 0.0;
		for(BaseAgent a : agentList)
		{
			if(retVal < a.getSumOfDeviceDeprecation())
				retVal = a.getSumOfDeviceDeprecation();
		}
		
		return retVal;
	}
	
	public static double getMinDeviceDeprecationCost()
	{
		double retVal = 1000.0;
		for(BaseAgent a : agentList)
		{
			if(retVal > a.getDeprecationCost())
				retVal = a.getDeprecationCost();
		}
		
		return retVal;
	}
	
	public static double getMaxOperatorCost()
	{
		double retVal = 0.0;
		for(BaseAgent a : agentList)
		{
			if(retVal < a.getOperatorCost())
				retVal = a.getOperatorCost();
		}
		
		return retVal;
	}
	
	public static double getMinOperatorCost()
	{
		double retVal = 1000.0;
		for(BaseAgent a : agentList)
		{
			if(retVal > a.getOperatorCost())
				retVal = a.getOperatorCost();
		}
		
		return retVal;
	}
	
	public static double getMaxCost()
	{
		if(maxCost == 0.0)
			return getMaxFuelCost()+getMaxOperatorCost()+getMaxDeviceDeprecationCost()+getMaxDeprecationCost();
		else
			return maxCost;
	}
	
	public static double getMinCost()
	{
		if(minCost == 0.0)
			return getMinFuelCost()+getMinOperatorCost()+getMinDeviceDeprecationCost()+getMinDeprecationCost();
		else
			return minCost;
	}
	
	

}
