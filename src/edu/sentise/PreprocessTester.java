package edu.sentise;

import java.util.ArrayList;

import edu.sentise.factory.BasicFactory;
import edu.sentise.model.SentimentData;
import edu.sentise.preprocessing.*;

public class PreprocessTester {

	public static void main(String[] args) {
		
		String text="it looks like this can be closed since the Python UI is gone.";
		SentimentData d=new SentimentData(text,0);
		ArrayList<SentimentData> dt=new ArrayList<SentimentData>();
		dt.add(d);
		
		
		TextPreprocessor pipeline =new POSTagProcessor(BasicFactory.getPOSUtility(false, false, false, 
				new MyStopWordsHandler(Configuration.STOPWORDS_FILE_NAME)), true, 2, true);
		
		dt=pipeline.apply(dt);
		System.out.println(dt.get(0).getText());
		

	}

}
