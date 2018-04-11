package edu.sentise.preprocessing;

import java.util.ArrayList;
import java.util.HashSet;

import edu.sentise.model.SentimentData;

public class PunctuationHandler {
	public static ArrayList<SentimentData> preprocessEmoticons(ArrayList<SentimentData> sentiList) {
		for (int i = 0; i < sentiList.size(); i++)
			sentiList.get(i).setText(replacePunctuations(sentiList.get(i).getText()));
		return sentiList;
	}

	private static  String replacePunctuations(String text) {

		/*if(text.contains("?"))*/
		
		text=text.replaceAll("\\?", " questionmark ");
		
		text=text.replaceAll("!", " exclamationmark ");
		return text;
	}
	public static void main(String[] args) {
		System.out.println(replacePunctuations("whats ur name??"));
		System.out.println(replacePunctuations("I wish!"));
	}
	
}
