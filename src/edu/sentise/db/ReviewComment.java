package edu.sentise.db;

public class ReviewComment {
	
	private String comment_id;
	private String message;
	private int sentiment_score;
	public String getComment_id() {
		return comment_id;
	}
	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getSentiment_score() {
		return sentiment_score;
	}
	public void setSentiment_score(int sentiment_score) {
		this.sentiment_score = sentiment_score;
	}
	public ReviewComment(String id, String msg,int score) {
		
		this.comment_id=id;
		this.message=msg;
		this.sentiment_score=score;
	}
	
	public ReviewComment( ReviewComment orig) {
		this.comment_id=orig.getComment_id();
		this.message=orig.getMessage();
		this.sentiment_score=orig.getSentiment_score();
		
	}
	

}
