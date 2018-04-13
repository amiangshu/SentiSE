package edu.sentise.preprocessing;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

import com.mysql.jdbc.ReplicationConnectionProxy;

import edu.sentise.model.SentimentData;
import edu.sentise.util.Constants;
import edu.sentise.util.Util;

public class BiGramTriGramHandler implements TextPreprocessor {

	private static HashMap<String,String> bigram_map= null;
	private static HashMap<String,String> trigram_map= null;
	public static void main(String[] args) {
		System.out.println(new BiGramTriGramHandler().replacenGrams(" i would like to tell you that"));
		
	}
	@Override
	public ArrayList<SentimentData> apply(ArrayList<SentimentData> sentimentData) {
		for (int i = 0; i < sentimentData.size(); i++)
			sentimentData.get(i).setText(replacenGrams((sentimentData.get(i).getText())));
		return sentimentData;
	}
	

	private   String replacenGrams(String text) {

		if(bigram_map == null || bigram_map.size() == 0)
			createBiGram();
		if(trigram_map == null || trigram_map.size() == 0)
			createTriGram();
		
		for(String key : bigram_map.keySet())
			if(text.contains(key))
				text=text.replaceAll(key, bigram_map.get(key));
		
		for(String key : trigram_map.keySet())
			if(text.contains(key))
				text=text.replaceAll(key, trigram_map.get(key));
		
		return text;
	}
	private static void createBiGram()
	{
		bigram_map= new HashMap<>();
		BufferedReader bufferedReader=Util.getBufferedreaderByFileName(Constants.BIGRAM_FILE);
		String line=null;
		try
		{
			while((line = bufferedReader.readLine())!= null)
			{
				//String[] parse=line.split(" ");
				String str=new StringBuilder(line).reverse().toString();
				String[] splits=str.split(" ",2);
				
				String key=new StringBuilder(splits[1]).reverse().toString();
				String val=" "+key.replaceAll(" ", "|")+" ";
				bigram_map.put(key,val);
				//System.out.println(key+ " "+val);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
	}
	private static void createTriGram()
	{
		trigram_map= new HashMap<>();
		BufferedReader bufferedReader=Util.getBufferedreaderByFileName(Constants.TRIGRAM_FILE);
		String line=null;
		try
		{
			while((line = bufferedReader.readLine())!= null)
			{
				//String[] parse=line.split(" ");
				String str=new StringBuilder(line).reverse().toString();
				String[] splits=str.split(" ",2);
				String key=new StringBuilder(splits[1]).reverse().toString();
				String val=" "+key.replaceAll(" ", "|")+" ";
				trigram_map.put(key,val);
				//System.out.println(key+ " "+val);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
	}
}
