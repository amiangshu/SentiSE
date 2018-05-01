package edu.sentise.factory;

import java.util.ArrayList;

import edu.sentise.preprocessing.MyStopWordsHandler;

abstract public class BasePOSUtility implements WordChooser{

	private  ArrayList<String> stop_words;
	
	public BasePOSUtility(MyStopWordsHandler handler) {
		stop_words=handler.getStopWordList();
	}
	
	public boolean isStopWord(String word) {
		return stop_words.contains(word);
	}
	
		
	
}
