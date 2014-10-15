package controller;

public class ScoreSet {
	private double cost;
	private double benefit;
	private double score;
	
	public ScoreSet(double cost, double benefit, double score) {
		super();
		this.cost = cost;
		this.benefit = benefit;
		this.score = score;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public double getBenefit() {
		return benefit;
	}
	public void setBenefit(double benefit) {
		this.benefit = benefit;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	
	
}
