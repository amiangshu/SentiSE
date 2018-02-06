package edu.sentise.test.elimination;

public class SentimentResults {

	String text;
	int positive=0;
	int negative=0;
	int neutral=0;
	public SentimentResults(String text, int positive, int negative, int neutra) {
		super();
		this.text = text;
		this.positive = positive;
		this.negative = negative;
		this.neutral = neutra;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getPositive() {
		return positive;
	}
	public void setPositive(int positive) {
		this.positive = positive;
	}
	public int getNegative() {
		return negative;
	}
	public void setNegative(int negative) {
		this.negative = negative;
	}
	public int getNeutra() {
		return neutral;
	}
	public void setNeutra(int neutra) {
		this.neutral = neutra;
	}
	
}
