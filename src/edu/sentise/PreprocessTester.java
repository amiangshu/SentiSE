package edu.sentise;

import java.util.ArrayList;

import edu.sentise.factory.BasicFactory;
import edu.sentise.model.SentimentData;
import edu.sentise.preprocessing.*;

public class PreprocessTester {

	public static void main(String[] args) {
		
		String text="First of all I do not think you hate hate hate horrible suck fuck genius love awesome need all these.";
		SentimentData d=new SentimentData(text,0);
		ArrayList<SentimentData> dt=new ArrayList<SentimentData>();
		dt.add(d);
		
		
		TextPreprocessor pipeline =new POSTagProcessor(BasicFactory.getPOSUtility(false, false, false, 
				new MyStopWordsHandler(Configuration.STOPWORDS_FILE_NAME)), true, 2, true);
		
		dt=pipeline.apply(dt);
		System.out.println(dt.get(0).getText());
		

	}

}
