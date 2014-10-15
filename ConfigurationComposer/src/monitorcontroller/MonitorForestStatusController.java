package monitorcontroller;

import globalvariable.services.AgentInstances;
import globalvariable.services.CellInstances;
import globalvariable.services.CurrentScore;
import globalvariable.services.MonitorForestStatusPolicy;
import hostactivator.HostActivator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

import Environment.ForestCell;
import Environment.ForestCell.ForestDensity;
import Environment.ForestCell.GeoFeature;
import Environment.Weather.WeatherCondition;
import agent.BaseAgent;
import agent.BaseAgent.AgentType;
import controller.ScoreSet;

public class MonitorForestStatusController  {
	
	private static final String MIN = "Min:";
	private HashMap<ArrayList<String>, ScoreSet> valueSet = new HashMap<ArrayList<String>, ScoreSet>();
	private HashMap<String, Double> minScoreSet = new  HashMap<String, Double>();
	private Vector<Thread> threadList = new Vector<Thread>();
	private int Threadcount;
	private static int cellSize;
	//private ArrayList<BaseAgent> optimalDeploy;
	//private ArrayList<BaseAgent> huristicDeploy;
	public ArrayList<Double> minList = new ArrayList<Double>();
	public ArrayList<Double> maxList = new ArrayList<Double>();
	AgentInstances agentInstances;
	CellInstances cellInstances; 
	CurrentScore currentScore;
	MonitorForestStatusPolicy monitorForestStatusPolicy;
	public MonitorForestStatusController()
	{
		agentInstances = (AgentInstances) HostActivator.getService("AgentInstances");
		cellInstances = (CellInstances) HostActivator.getService("CellInstances");
		currentScore = (CurrentScore) HostActivator.getService("CurrentScore");
		monitorForestStatusPolicy = (MonitorForestStatusPolicy)HostActivator.getService("MonitorForestStatusPolicy");
	}


	public ArrayList<BaseAgent> getOptimalConfiguration(){
		Threadcount = 0;
		currentScore.optimalAgentList.clear();
		threadList.clear();
		valueSet.clear();
		minScoreSet.clear();
		cellSize = cellInstances.getCellList().size();
		//cellSize = 9;

		final ICombinatoricsVector<String> AgentCombination = Factory.createVector(agentInstances.getAgentIDList());
		final Generator<String> CombinationGen = Factory.createSimpleCombinationGenerator(AgentCombination, cellSize);

		//final BufferedWriter out = new BufferedWriter(new FileWriter("./Data/out.txt",true));
		
		//private static double OptimalValue = 0.0; 

		for (final ICombinatoricsVector<String> perm : CombinationGen) {
			Threadcount++;
		}
		
		for (final ICombinatoricsVector<String> perm : CombinationGen) {
			final Generator<String> PermutationGen = Factory.createPermutationGenerator(perm);
			PermutationThread th = new PermutationThread(PermutationGen);
			threadList.add(th);
			th.start();
		}
		
		while(true)
		{
			if (threadList.size() == Threadcount) {
				
				for(int i = 0; i < threadList.size(); ++i) {
			
					Thread t = threadList.get(i);
							
					if (t.isAlive()) {
						synchronized (t) {
							try {
								t.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
				return getOptimal();
			}
		}
	}
	
	private ArrayList<BaseAgent> getOptimal()
	{
		double maxVal = -1;
		double minVal = 1;
		ScoreSet maxSet = null;
		ArrayList<BaseAgent> retStr = new ArrayList<BaseAgent>();
		String minStr = null;
		
		Iterator<ArrayList<String>> it = this.valueSet.keySet().iterator();
		
		while(it.hasNext())
		{
			ArrayList<String> str = it.next();
			ScoreSet sc = valueSet.get(str);
			//LoggerUtil.write(LoggerUtil.LOGPANE,str.toString());
			//LoggerUtil.write(LoggerUtil.LOGPANE,"Cost:"+ String.format("%.3f",sc.getCost()) +"; Benefit:"+String.format("%.3f",sc.getBenefit())+"; Col.Score:"+String.format("%.3f",sc.getScore()));
			
			
			if(maxVal < this.valueSet.get(str).getScore())
			{
				retStr.clear();
				maxVal = this.valueSet.get(str).getScore();
				maxSet = this.valueSet.get(str);
				for(String s :str)
					retStr.add(agentInstances.getAgent(s));
			}
		}
		
		Iterator<String> minIt = this.minScoreSet.keySet().iterator();
		while(minIt.hasNext())
		{
			String l = minIt.next();
			double val = this.minScoreSet.get(l);
			if(minVal > val)
			{
				minVal = val;
				minStr = l.toString();
			}
		}
		
		currentScore.OptimalScore = maxVal;
		currentScore.OptimalSet = maxSet;
		
		currentScore.optimalAgentList = retStr;
		
		/*LoggerUtil.write("Dynamic Cost:"+currentScore.OptimalSet.getCost());
		LoggerUtil.write("Dynamic Benefit:"+CurrentScore.OptimalSet.getBenefit());
		LoggerUtil.write("Dynamic Score:"+CurrentScore.OptimalScore);
		LoggerUtil.write("Dynamic Conf:"+CurrentScore.optimalAgentList);*/
		
		System.out.println("Max:"+currentScore.OptimalScore);
		maxList.add(currentScore.OptimalScore);
		System.out.println(MIN+minVal);
		minList.add(minVal);
		//LoggerUtil.write(minStr);
		
		//LoggerUtil.write("OPT:"+CurrentScore.optimalAgentList);
		
		return retStr;
	}

	protected static double getConfValue(double cost, double benefit) {
		cost = cost / cellSize;//CellInstances.getCellList().size();
		benefit = benefit / cellSize;//CellInstances.getCellList().size();
		double colVal = benefit - cost;
		
		//Logger.write(cost +":"+benefit+":"+colVal);
		
		return colVal;
	}
	
	private class PermutationThread extends Thread implements Runnable
	{
		Generator<String> PermutationGen;
		
		public PermutationThread(Generator<String> PermutationGen)
		{
			this.PermutationGen = PermutationGen;
		}

		@Override
		public void run() {
			
			double maxOptimalValue = 0.0;
			double maxCost = 0.0;
			double maxBenefit = 0.0;
			double minCost = 0.0;
			double minBenefit = 0.0;
			double minScore = 1;
			String optimalConf = null;
			ArrayList<String> agentList = new ArrayList<String>();
			ArrayList<String> minAgentList = new ArrayList<String>();

			for (ICombinatoricsVector<String> Cperm : PermutationGen) {

				//Logger.write(Cperm);
				/*if(Cperm.toString().equals("CombinatoricsVector=([JE5, JE1, UAV2, JE3, UAV1, JE2, HE2, HE1, JE6], size=9)"))
				{
					Logger.write("here");
				}*/
				boolean isValidConfiguration = false;

				double cost = 0;
				double benefit = 0;

				for( int i = 0 ; i< cellSize; i++ )
				{
					ForestCell cell = cellInstances.getCell(i);
					BaseAgent agent = agentInstances.getAgent(Cperm.getValue(i));

					if(monitorForestStatusPolicy.checkValidation(cell, agent,"OPT"))
					{
						isValidConfiguration = true;
					}
					else
					{
						isValidConfiguration = false;
						break;
					}
					
					//COST 占쏙옙占�
					cost = cost + getCost(cell, agent);
					
					//BENEFIT 占쏙옙占�
					benefit = benefit + getBenefit(cell, agent);
				}
				
				double confValue = getConfValue(cost, benefit);
				if(confValue > maxOptimalValue && confValue != 0.0)
				{
					agentList.clear();
					/*String outStr = "0:["+Cperm.getValue(0)+","+Cperm.getValue(1)+","+Cperm.getValue(2)+"]"+"\t"+ 
							"1:["+Cperm.getValue(3)+","+Cperm.getValue(4)+","+Cperm.getValue(5)+"]"+"\t"+
							"2:["+Cperm.getValue(6)+","+Cperm.getValue(7)+","+Cperm.getValue(8)+"]"+"\t"
							+ " Cost:" + cost + "\t"+" Benefit:" + benefit + "\n";*/
					String outStr = Cperm.toString();
					maxOptimalValue = confValue;
					maxCost = cost;
					maxBenefit = benefit;
					optimalConf = outStr;
					for(String s: Cperm)
					{
						agentList.add(s);
					}
					
				}
				if(confValue < minScore)
				{
					minCost = cost;
					minBenefit = benefit;
					minAgentList.clear();
					minScore = confValue;
					for(String s: Cperm)
					{
						minAgentList.add(s);
					}
				}
			}
			valueSet.put(agentList, new ScoreSet(maxCost/cellSize, maxBenefit/cellSize, maxOptimalValue));
			/*System.out.println(minCost+","+minBenefit);
			System.out.println(minAgentList.toString()+","+ minScore);*/
			minScoreSet.put(minAgentList.toString(), minScore);
			
		}
	}
	
	private double getCost(ForestCell cell, BaseAgent agent)
	{
		if(agent == null)
		{
			return 1;
		}
		double cost = 0.0;
		
		cost = cost + agent.getFuelCost() + agent.getDeprecationCost() + agent.getSumOfDeviceDeprecation();
		
		if(cell.getDensity().equals(ForestDensity.High) && agent.getAgentType().equals(AgentType.Jeep))
		{
			cost = cost + agent.getOperatorCost();
		}
		
		//there must be operator in airplane
		if(agent.getAgentType().equals(AgentType.AirPlane))
		{
			cost = cost + agent.getOperatorCost();
		}
		
		
		double nomalizedCost = (cost - agentInstances.getMinCost()) / (agentInstances.getMaxCost() - agentInstances.getMinCost());
		
		//Logger.write("c:"+cost+" / nc:"+ nomalizedCost);
		
		return nomalizedCost;
	}
	
	private double getBenefit(ForestCell cell, BaseAgent agent)
	{
		if(agent == null)
		{
			return 0;
		}
		double correctness = agent.getAvgOfSensor();
		double completeness = 0.0;
		
		//占쏙옙占싹띰옙
		if(cell.getFeatureList().contains(GeoFeature.Mountain))
		{
			//占쏙옙철占�
			if(cell.getWeather().getCondition().equals(WeatherCondition.Rainy))
			{
				completeness = agent.getMountainRainMovingDistance()/cellInstances.monitoringDistance;
			}

			else
			{
				completeness = agent.getMountainMovingDistance()/cellInstances.monitoringDistance;
			}
		}

		else
		{
			//占쏙옙철占�
			if(cell.getWeather().getCondition().equals(WeatherCondition.Rainy))
			{
				completeness = agent.getRainMovingDistance()/cellInstances.monitoringDistance;
			}

			else
			{
				completeness = agent.getNormalMovingDistance()/cellInstances.monitoringDistance;
			}
		}
		
		double normalizedBenefit = 0.3 * correctness + 0.7 * completeness;
		//Logger.write(correctness+":"+completeness+":"+normalizedBenefit);
		
		return normalizedBenefit;
	}

	public ArrayList<BaseAgent> getHuristicConfiguration() {
		
		//huristicDeploy.clear();
		
		if(currentScore.huristicAgentList.size() == 0)
		{
			//for(BaseAgent a : currentScore.optimalAgentList)
			currentScore.huristicAgentList.addAll(currentScore.optimalAgentList);
			currentScore.HuristicScore = currentScore.OptimalScore;
			currentScore.HuristicSet=(new ScoreSet(currentScore.OptimalSet.getCost(), currentScore.OptimalSet.getBenefit(),currentScore.OptimalSet.getScore()));
		}
		else
		{
			for(int i=0;i<cellSize;i++)
			{
				ForestCell c = cellInstances.getCell(i);
				BaseAgent a = currentScore.huristicAgentList.get(i);
				
				if(monitorForestStatusPolicy.checkValidation(c, a, "HUR"))
				{
					continue;
				}
				else //unallocate agent by policy
				{
					currentScore.huristicAgentList.remove(i);
					currentScore.huristicAgentList.add(i, null);
				}
			}
			
			for(int j=0;j<currentScore.huristicAgentList.size(); j++)
			{
				BaseAgent a = currentScore.huristicAgentList.get(j);
			
				if(a == null)
				{
					currentScore.huristicAgentList.remove(j);
					//currentScore.huristicAgentList.add(j, getAlternativeAgent(cellInstances.getCellList().get(j)));
					currentScore.huristicAgentList.add(j, null);
				}
					
			}
			
			double cost = 0.0;
			double benefit = 0.0;
			
			for(int i=0;i<cellSize;i++)
			{
				cost = cost + getCost(cellInstances.getCell(i), currentScore.huristicAgentList.get(i));
				benefit = benefit + getBenefit(cellInstances.getCell(i), currentScore.huristicAgentList.get(i));
			}
			
			currentScore.HuristicScore = getConfValue(cost,benefit);
			currentScore.HuristicSet = new ScoreSet(cost/cellSize,benefit/cellSize,currentScore.HuristicScore);
		}

		return currentScore.huristicAgentList;
		
	
	}
	
	private BaseAgent getAlternativeAgent(ForestCell c) {
		
		for(BaseAgent ba : agentInstances.getAgentList())
		{
			if(!currentScore.huristicAgentList.contains(ba))
			{
				if(monitorForestStatusPolicy.checkValidation(c, ba,"HUR"))
				{
					return ba;
				}
				else
					continue;
			}
		}
		return null;
	}

	public double getScore(ArrayList<ForestCell> cellList, ArrayList<BaseAgent> agentList) {
		double cost = 0.0;
		double benefit = 0.0;
		
		for(int i=0;i<cellSize;i++)
		{
			cost = cost + getCost(cellList.get(i), agentList.get(i));
			benefit = benefit + getBenefit(cellList.get(i), agentList.get(i));
		}
		
		return getConfValue(cost,benefit);
		
	}

	public void changeValue(ArrayList<ForestCell> cellList, ArrayList<BaseAgent> agentList) 
	{
		double cost = 0.0;
		double benefit = 0.0;
		
		for(int i=0;i<cellSize;i++)
		{
			cost = cost + getCost(cellList.get(i), agentList.get(i));
			benefit = benefit + getBenefit(cellList.get(i), agentList.get(i));
		}
		
		currentScore.OptimalScore = getConfValue(cost,benefit);
		currentScore.OptimalSet=new ScoreSet(cost,benefit,currentScore.OptimalScore);
		
		//LoggerUtil.write(LoggerUtil.LOGPANE, "Cost :"+(cost/cellSize));
		//LoggerUtil.write(LoggerUtil.LOGPANE, "Benefit :"+(benefit/cellSize));
		//LoggerUtil.write(LoggerUtil.LOGPANE, "Score :"+CurrentScore.OptimalScore);
		
	}
	
}
