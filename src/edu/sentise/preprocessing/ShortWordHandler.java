package edu.sentise.preprocessing;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.mysql.jdbc.ReplicationConnectionProxy;

import edu.sentise.model.SentimentData;
import edu.sentise.util.Constants;
import edu.sentise.util.Util;

public class ShortWordHandler {

	private static HashMap<String,String> shortWordMap= null;
	public static void main(String[] args) {
		System.out.println(replaceShortWords(("stupid codeing style lol wtf")));
	}
	public static ArrayList<SentimentData> preprocessShortWord(ArrayList<SentimentData> sentiList) {
		for (int i = 0; i < sentiList.size(); i++)
			sentiList.get(i).setText(replaceShortWords((sentiList.get(i).getText())));
		return sentiList;
	}

	private static  String replaceShortWords(String text) {

		if(shortWordMap == null || shortWordMap.size() == 0)
			createShortWordMap();
		StringTokenizer st= new StringTokenizer(text, " ");
		
		while(st.hasMoreTokens())
		{
			String token=st.nextToken();
			if(shortWordMap.containsKey(token))
			{
				text=text.replaceAll(token,shortWordMap.get(token));
			}
		}
		return text;
	}
	private static void createShortWordMap()
	{
		shortWordMap= new HashMap<>();
		BufferedReader bufferedReader=Util.getBufferedreaderByFileName(Constants.SHORT_WORD_FILE);
		String line=null;
		try
		{
			while((line = bufferedReader.readLine())!= null)
			{
				String[] parse=line.split(" ");
				shortWordMap.put(parse[0],parse[1]);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
	}
}
