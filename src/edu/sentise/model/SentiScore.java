package edu.sentise.model;

public class SentiScore {

	private String word;
	private String pos;
	private double score;
	public SentiScore(String word, String pos, double score) {
		super();
		this.word = word;
		this.pos = pos;
		this.score = score;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	
	
	
}
