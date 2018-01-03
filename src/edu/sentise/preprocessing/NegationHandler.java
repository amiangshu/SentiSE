package edu.sentise.preprocessing;

import java.util.ArrayList;

import edu.sentise.model.SentimentData;

public class NegationHandler {
	
	
	public static ArrayList<SentimentData> handleNegation(ArrayList<SentimentData> sentiList)
	{
		int length=sentiList.size();
		for(int i = 0; i< length;i++)
		{
			ArrayList<String> sentenceList=Parser.sentenceTokenizer(sentiList.get(i).getText());
		}
		
		return sentiList;
	}

}
