package edu.sentise.db;

import java.util.ArrayList;
import java.util.List;

import edu.sentise.SentiSE;

public class ParallelEvaluator implements Runnable {
	
	private DbConnector connection;
	private List<ReviewComment> reviewList;
	private int serial;
	private SentiSE analyzer;

	public ParallelEvaluator(DbConnector connection, List<ReviewComment> reviewList, SentiSE analyzer, int serial) {
	
		this.connection=connection;
		this.reviewList=reviewList;
		this.serial=serial;
		this.analyzer=analyzer;
		
	}
	
	@Override
	public void run() {
		System.out.println("Starting task:" +serial);
		
		try {
			int[] scores = analyzer.getSentimentScore(getComments(reviewList));
			
			for (int i = 0; i < reviewList.size(); i++) {
				ReviewComment comment = reviewList.get(i);
				comment.setSentiment_score(scores[i]);
				connection.saveInlineSentiment(comment);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Finished score computation:" +serial);
		this.reviewList=null;
		this.analyzer=null;
		this.connection=null;
	}

	private  ArrayList<String> getComments(List<ReviewComment> reviewList) {

		ArrayList<String> commentList = new ArrayList<String>();
		for (int i = 0; i < reviewList.size(); i++) {
			commentList.add(reviewList.get(i).getMessage());

		}
		return commentList;

	}
}
