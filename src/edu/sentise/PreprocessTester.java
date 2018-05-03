package edu.sentise;

import java.util.ArrayList;

import edu.sentise.model.SentimentData;
import edu.sentise.preprocessing.*;
import edu.sentise.util.Configuration;

public class PreprocessTester {

	public static void main(String[] args) {
		
		String text="First of all I do not think you need all these.\r\n" + 
				"";
		SentimentData d=new SentimentData(text,0);
		ArrayList<SentimentData> dt=new ArrayList<SentimentData>();
		dt.add(d);
		
		
		TextPreprocessor pipeline =new BiGramTriGramHandler();
		
		dt=pipeline.apply(dt);
		System.out.println(dt.get(0).getText());
		

	}

}
