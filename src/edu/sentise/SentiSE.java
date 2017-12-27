package edu.sentise;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;

import edu.sentise.model.SentimentData;
import edu.sentise.preprocessing.ContractionLoader;
import edu.sentise.util.Constants;
import edu.sentise.util.Util;


public class SentiSE {

	private static ArrayList<SentimentData> sentimentDataList=new ArrayList<>();
	
	public static void main(String[] args) {
		
		BufferedReader bufferedReader=Util.getBufferedreaderByFileName(Constants.ORACLE_FILE_NAME);
		//Util.printFile(bufferedReader);
		sentimentDataList= SentimentData.parseSentimentData(bufferedReader);
		System.out.println(sentimentDataList.size());
		sentimentDataList=ContractionLoader.preprocessContractions(sentimentDataList);
		for(int i = 0;i < sentimentDataList.size();i++)
		{
			System.out.println(sentimentDataList.get(i).getText());
		}
		
	}

}
