package edu.sentise.preprocessing;

import java.util.ArrayList;

import edu.sentise.model.SentimentData;

public interface TextPreprocessor {
	public ArrayList<SentimentData> apply(ArrayList<SentimentData> sentimentData);

}
