package edu.sentise.preprocessing;

import java.util.ArrayList;
import java.util.HashSet;

import edu.sentise.model.SentimentData;

public class ExclamationHandler implements TextPreprocessor {
	
	public  ArrayList<SentimentData> apply(ArrayList<SentimentData> sentiList) {
		for (int i = 0; i < sentiList.size(); i++)
			sentiList.get(i).setText(replacePunctuations(sentiList.get(i).getText()));
		return sentiList;
	}

	private  String replacePunctuations(String text) {

		text=text.replaceAll("!", " exclamationmark ");
		return text;
	}
		
}
