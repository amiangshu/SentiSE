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



public class ContractionLoader {

		private static 	HashMap<String, String> getContractionData(BufferedReader bufferedReader) {
		HashMap<String, String> contractionMap = new HashMap<>();
		try {

			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				//String[] splits=line.split(" ",2);
				int index=line.indexOf('	');
				String key=line.substring(0,index).trim();
				String value=line.substring(index+1,line.length()).trim();
				//System.out.println(index+" "+ key+": "+ value+" ::"+ line);
				contractionMap.put(key,value);
			}

			// Always close files.
			//bufferedReader.close();
		}

		catch (IOException ex) {
			ex.printStackTrace();
		}
		return contractionMap;

	}
	public static ArrayList<SentimentData> preprocessContractions( ArrayList<SentimentData> sentiList)
	{
		BufferedReader bufferedReader = Util.getBufferedreaderByFileName(Constants.CONTRACTION_TEXT_FILE_NAME);	
		HashMap<String, String> contractionMap=getContractionData(bufferedReader);
		return applyContraction(sentiList, contractionMap);
	}
	private static ArrayList<SentimentData> applyContraction(ArrayList<SentimentData> sentiList, HashMap<String, String> contractionMap)
	{
		int length=sentiList.size();
		HashSet<String> keySet= new HashSet<String>(contractionMap.keySet());
		for( int i=0;i<length;i++)
		{
			for(String key: keySet)
			{
				if(sentiList.get(i).getText().toLowerCase().contains(key))
				{
					//System.out.println(sentiList.get(i).getText());
					sentiList.get(i).setText(sentiList.get(i).getText().toLowerCase().replaceAll(key, contractionMap.get(key)));
					//System.out.println(sentiList.get(i).getText());
				}
			}
		}
		return sentiList;
	}
}
