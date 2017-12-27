package edu.sentise.preprocessing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.sentise.model.SentimentData;
import edu.sentise.util.Constants;

public class URLRemover {

	public static ArrayList<SentimentData> removeURL(ArrayList<SentimentData> sentiData)
	{
		int length=sentiData.size();
		Pattern p = Pattern.compile("((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*/)");		
		for( int i=0;i<length;i++)
		{
			String text=sentiData.get(i).getText();
			Matcher m = p.matcher(text);
			while(m.find()) {
				String urlStr = m.group();
				System.out.println(":::"+urlStr);
				sentiData.get(i).setText(sentiData.get(i).getText().replaceAll(urlStr,""));
			}
		}
		return sentiData;
	}
}
