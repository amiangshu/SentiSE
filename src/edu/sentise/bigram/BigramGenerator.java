package edu.sentise.bigram;

import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

import edu.sentise.model.SentimentData;
import edu.sentise.preprocessing.ContractionLoader;
import edu.sentise.preprocessing.URLRemover;
import edu.sentise.util.Constants;
import edu.sentise.util.DataLists;

public class BigramGenerator {

	static class BiGram{
		String bigram;
		int count;
	}
	private static HashMap<String,Integer> stringMap= new HashMap<>();
	private static HashSet<String> stop_words = new HashSet<String>(Arrays.asList(DataLists.stop_words));

	public static void main(String[] args) {
		ArrayList<SentimentData> sentimentDataList = SentimentData.parseSentimentData(Constants.ORACLE_FILE_NAME);

		/*System.out.println("Preprocessing text ..");
		ContractionLoader contractionLoader=new ContractionLoader(Constants.CONTRACTION_TEXT_FILE_NAME);
		sentimentDataList = contractionLoader.preprocessContractions(sentimentDataList);
		sentimentDataList = URLRemover.removeURL(sentimentDataList);
		EmoticonLoader emoticonHandler = new EmoticonLoader(Constants.EMOTICONS_FILE_NAME);
		sentimentDataList = emoticonHandler.preprocessEmoticons(sentimentDataList);
		lematizeSentimentData(sentimentDataList);*/
	}
	public static void lematizeSentimentData(ArrayList<SentimentData> sentimentData) {
	     
		
		int length = sentimentData.size();
		for (int i = 0; i < length; i++) {

			//System.out.println(sentimentData.get(i).getText());
			tokenize(sentimentData.get(i).getText());
			if((i%100) ==0)
			{
				System.out.println("bigram processed:"+i +" of "+length);
			}
		}
		createListFromMap();
		ArrayList<BiGram> mList=createListFromMap();
		sortBigrams(mList);
		
		
		
	}
	private static ArrayList<BiGram> createListFromMap()
	{
		ArrayList<BiGram> mList=new ArrayList<>();
		for(String key: stringMap.keySet())
		{
			BiGram biGram= new BiGram();
			biGram.bigram=key;
			biGram.count=stringMap.get(key);
			mList.add(biGram);
		}
		return mList;

	}
	private static void sortBigrams(ArrayList<BiGram> mList)
	{
		int SELECT=500;
		Collections.sort(mList, new Comparator<BiGram>() {
			public int compare(BiGram o1, BiGram o2) {
				return  o2.count - o1.count;
			};
		});
		try
		{
		String fileName=Constants.BIGRAM_FILE;

		File file= new File(fileName);
		file.createNewFile();
		BufferedWriter bufferedWriter=edu.sentise.util.Util.getBufferedWriterByFileName(fileName);
		for(int i=0;i<SELECT && i<mList.size();i++)
		{
			System.out.println(mList.get(i).bigram+ " "+mList.get(i).count);
			bufferedWriter.write(mList.get(i).bigram+ " "+mList.get(i).count+"\n");
		}
		
		bufferedWriter.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println(" file writing done.........");
		
	}
	private static void tokenize(String text)
	{
		StringTokenizer st= new StringTokenizer(text, " ");
		String prevToken=null;
		while(st.hasMoreTokens())
		{
			String token=st.nextToken();
			if(prevToken != null)
			{
				String key=prevToken+" "+token;
				if(stop_words.contains(prevToken) && stop_words.contains(token))
				{
					//both are stop words. choose to ignore it
				}
				else
				{
				if(stringMap.containsKey(key))
		
					stringMap.put(key, stringMap.get(key)+1);
				else
					stringMap.put(key, 1);
				}
			}
			prevToken=token;
		}
	}
}
