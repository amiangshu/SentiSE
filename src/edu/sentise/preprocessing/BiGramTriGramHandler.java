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

public class BiGramTriGramHandler {

	private static HashSet<String> bigram_set= null;
	private static HashSet<String> trigram_set= null;
	public static void main(String[] args) {
		System.out.println(replaceShortWords(("stupid codeing style lol wtf")));
	}
	public static ArrayList<SentimentData> preprocessBiGramTriGram(ArrayList<SentimentData> sentiList) {
		for (int i = 0; i < sentiList.size(); i++)
			sentiList.get(i).setText(replaceShortWords((sentiList.get(i).getText())));
		return sentiList;
	}

	private static  String replaceShortWords(String text) {

		if(bigram_set == null || bigram_set.size() == 0)
			createBiGram();
		if(trigram_set == null || trigram_set.size() == 0)
			createTriGram();
		StringTokenizer st= new StringTokenizer(text, " ");
		
		while(st.hasMoreTokens())
		{
			String token=st.nextToken();
			if(trigram_set.contains(token))
			{
				text=text.replaceAll(token,token.replaceAll(" ","|"));
			}
			else if(bigram_set.contains(token))
			{
				text=text.replaceAll(token,token.replaceAll(" ","|"));
			}
			
		}
		return text;
	}
	private static void createBiGram()
	{
		bigram_set= new HashSet<>();
		BufferedReader bufferedReader=Util.getBufferedreaderByFileName(Constants.BIGRAM_FILE);
		String line=null;
		try
		{
			while((line = bufferedReader.readLine())!= null)
			{
				//String[] parse=line.split(" ");
				bigram_set.add(line);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
	}
	private static void createTriGram()
	{
		trigram_set= new HashSet<>();
		BufferedReader bufferedReader=Util.getBufferedreaderByFileName(Constants.TRIGRAM_FILE);
		String line=null;
		try
		{
			while((line = bufferedReader.readLine())!= null)
			{
				//String[] parse=line.split(" ");
				trigram_set.add(line);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
	}
}
