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
	private static HashMap<String, String> contractionMap=new HashMap<>();
	public static void main(String[] args) {
		
		BufferedReader bufferedReader=Util.getBufferedreaderByFileName(Constants.ORACLE_FILE_NAME);
		//Util.printFile(bufferedReader);
		sentimentDataList= SentimentData.parseSentimentData(bufferedReader);
		System.out.println(sentimentDataList.size());
		bufferedReader = Util.getBufferedreaderByFileName(Constants.CONTRACTION_TEXT_FILE_NAME);
		contractionMap = ContractionLoader.getContractionData(bufferedReader);
		for (String key : contractionMap.keySet())
			System.out.println(key);
	}

}
