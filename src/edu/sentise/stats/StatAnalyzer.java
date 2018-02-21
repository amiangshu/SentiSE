package edu.sentise.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import edu.sentise.model.SentimentData;
import edu.sentise.preprocessing.ContractionLoader;
import edu.sentise.preprocessing.EmoticonLoader;
import edu.sentise.preprocessing.NegationHandler;
import edu.sentise.preprocessing.URLRemover;
import edu.sentise.test.ARFFTestGenerator;
import edu.sentise.util.Constants;
import weka.core.Instances;

public class StatAnalyzer {

	private static String emoticonDictionary = Constants.EMOTICONS_FILE_NAME;
	private static String contractionDictionary = Constants.CONTRACTION_TEXT_FILE_NAME;
	public static void main(String[] args) {
		ArrayList<SentimentData> tempList = SentimentData.parseSentimentData(Constants.ORACLE_FILE_NAME);
		PreprocessTexts(tempList);
	}
	private static void PreprocessTexts(ArrayList<SentimentData> sentimentDataList)
	{
		String[] strong_neg_words= {"damn","shit","idiot","stupid","hell"};
	
		ContractionLoader contractionLoader=new ContractionLoader(contractionDictionary);
		EmoticonLoader emoticonLoader=new EmoticonLoader(emoticonDictionary);
		System.out.println("Reading oracle file...");
		
		System.out.println("Preprocessing text ..");
		sentimentDataList = contractionLoader.preprocessContractions(sentimentDataList);
		sentimentDataList = URLRemover.removeURL(sentimentDataList);
		sentimentDataList = emoticonLoader.preprocessEmoticons(sentimentDataList);
		
		//HashSet<String> hashString=new HashSet<>(Arrays.asList(strong_neg_words));
		int size=sentimentDataList.size();
		
		int neg_count=0;
		int pos_count=0;
		int neutral_count=0;
		for(int i=0;i<size;i++)
		{
			for(int j=0;j<strong_neg_words.length;j++)
			{
				if(sentimentDataList.get(i).getRating() == 0 && sentimentDataList.get(i).getText().contains(strong_neg_words[j])) {
					neutral_count++;
				}
				if(sentimentDataList.get(i).getRating() == 1 && sentimentDataList.get(i).getText().contains(strong_neg_words[j])) {
					pos_count++;
				}
				if(sentimentDataList.get(i).getRating() == -1 && sentimentDataList.get(i).getText().contains(strong_neg_words[j])) {
					neg_count++;
				}
			}
		}
		System.out.println("pos: "+pos_count+" neg: "+neg_count+" neutral: "+ neutral_count);


		
		
	}
}
