package edu.sentise.preprocessing;

import java.io.BufferedReader;
import java.util.HashMap;

import org.apache.log4j.chainsaw.Main;

import edu.sentise.util.Constants;
import edu.sentise.util.Util;


public class AddSentiWord {

	public static HashMap<String, Double> positiveSentiMap=  new HashMap<>();
	public static HashMap<String, Double> negtiveSentiMap=  new HashMap<>();
	
	/**
	 * converts sentiword.net tags to stanfordparser tags
	 * @param pos
	 * @return 
	 */
	public static String getConvertedPOS(String pos)
	{
		if(pos.equals("n"))
			return "NN";
		else if(pos.equals("v"))
			return "VB";
		else if(pos.equals("a"))
			return "JJ";
		else return "";
			
	}
	public static void createPositiveSentiMap()
	{
		BufferedReader bufferedReader=Util.getBufferedreaderByFileName(Constants.POSITIVE_SENTI_WORD_FILE);
		String line=null;
		try
		{
			while((line = bufferedReader.readLine())!= null)
			{
				String[] parse=line.split("#");
				positiveSentiMap.put(parse[0]+"#"+getConvertedPOS(parse[1]),Double.parseDouble(parse[2]));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
	}
	
	public static void createNegativeSentiMap()
	{
		BufferedReader bufferedReader=Util.getBufferedreaderByFileName(Constants.NEGATIVE_SENTI_WORD_FILE);
		String line=null;
		try
		{
			while((line = bufferedReader.readLine())!= null)
			{
				String[] parse=line.split("#");
				negtiveSentiMap.put(parse[0]+"#"+getConvertedPOS(parse[1]),Double.parseDouble(parse[2]));
				System.out.println(line);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		
	}
	public static void main(String[] args) {
		createPositiveSentiMap();
		createNegativeSentiMap();
		
	}
	
	
}
