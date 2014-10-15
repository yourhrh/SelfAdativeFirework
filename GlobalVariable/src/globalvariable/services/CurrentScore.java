package globalvariable.services;

import java.util.ArrayList;

import controller.ScoreSet;
import agent.BaseAgent;


public class CurrentScore {
	
	public static ArrayList<BaseAgent> optimalAgentList = new ArrayList<BaseAgent>();
	public static double OptimalScore = 0.0;
	public static ScoreSet OptimalSet;
	//public static double OptimalBenefit;
	
	public static ArrayList<BaseAgent> huristicAgentList = new ArrayList<BaseAgent>();
	public static double HuristicScore = 0.0;
	public static ScoreSet HuristicSet;
	//public static double HuristicBenefit;
}
