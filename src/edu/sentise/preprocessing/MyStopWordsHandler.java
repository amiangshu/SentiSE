package edu.sentise.preprocessing;

import java.util.Arrays;
import java.util.HashSet;

import edu.sentise.util.DataLists;
import weka.core.stopwords.StopwordsHandler;

public class MyStopWordsHandler implements StopwordsHandler {
	public static HashSet<String> stop_words=new HashSet<String>(Arrays.asList(DataLists.stop_words));
	public MyStopWordsHandler() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean isStopword(String word) {
		return stop_words.contains(word);
			
	}

}
