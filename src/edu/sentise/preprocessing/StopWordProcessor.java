package edu.sentise.preprocessing;

import java.util.ArrayList;
import java.util.StringTokenizer;

import edu.sentise.model.SentimentData;

public class StopWordProcessor implements TextPreprocessor{
	
	@Override
	public ArrayList<SentimentData> apply(ArrayList<SentimentData> sentimentData) {
		for (int i = 0; i < sentimentData.size(); i++)
			sentimentData.get(i).setText(applyStopWords(sentimentData.get(i).getText()));
		return sentimentData;
	}
	private String applyStopWords(String text)
	{
		StringTokenizer stringTokenizer= new StringTokenizer(text);
		StringBuilder stringBuilder=new StringBuilder();
		while(stringTokenizer.hasMoreTokens())
		{
			String token=stringTokenizer.nextToken();
			if(!MyStopWordsHandler.stop_words.contains(token))
				stringBuilder.append(token+" ");
		}
		return stringBuilder.toString();
	}

}
