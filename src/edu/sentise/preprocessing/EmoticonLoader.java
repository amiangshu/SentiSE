package edu.sentise.preprocessing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.sun.org.apache.regexp.internal.recompile;

import edu.sentise.model.SentimentData;
import edu.sentise.util.Constants;
import edu.sentise.util.Util;



public class EmoticonLoader {

		private static 	HashMap<String, String> getEmoticonata(BufferedReader bufferedReader) {
		HashMap<String, String> emoticonMap = new HashMap<>();
		try {

			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				//String[] splits=line.split(" ",2);
				int index=line.indexOf('	');
				String key=line.substring(0,index).trim();
				String value=line.substring(index+1,line.length()).trim();
				//System.out.println(index+" "+ key+": "+ value+" ::"+ line);
				emoticonMap.put(" "+key+" "," "+value);
			}

			// Always close files.
			//bufferedReader.close();
		}

		catch (IOException ex) {
			ex.printStackTrace();
		}
		return emoticonMap;

	}
	public static ArrayList<SentimentData> preprocessEmoticons( ArrayList<SentimentData> sentiList)
	{
		BufferedReader bufferedReader = Util.getBufferedreaderByFileName(Constants.EMOTICONS_FILE_NAME);	
		HashMap<String, String> emoticonMap=getEmoticonata(bufferedReader);
		return applyEmoticon(sentiList, emoticonMap);
	}
	private static ArrayList<SentimentData> applyEmoticon(ArrayList<SentimentData> sentiList, HashMap<String, String> emoticonMap)
	{
		int length=sentiList.size();
		HashSet<String> keySet= new HashSet<String>(emoticonMap.keySet());
		for( int i=0;i<length;i++)
		{
			for(String key: keySet)
			{
				// previously used if. and replace all. problem in regex compelled to use while
				while(sentiList.get(i).getText().contains(key))
				{
					//System.out.println(sentiList.get(i).getText()+"  "+ key);
					sentiList.get(i).setText(sentiList.get(i).getText().replace(key," "+ emoticonMap.get(key)+" "));
					//System.out.println(sentiList.get(i).getText());
				}
			}
		}
		return sentiList;
	}
}
