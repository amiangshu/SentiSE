package edu.sentise;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.javafx.scene.paint.GradientUtils.Parser;

import edu.sentise.model.SentimentData;
import edu.sentise.preprocessing.ARFFGenerator;
import edu.sentise.preprocessing.ContractionLoader;
import edu.sentise.preprocessing.EmoticonLoader;
import edu.sentise.preprocessing.POSTagger;
import edu.sentise.preprocessing.URLRemover;
import edu.sentise.util.Constants;
import edu.sentise.util.Util;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;


public class SentiSE {

	private static ArrayList<SentimentData> sentimentDataList=new ArrayList<>();
	
	public static void main(String[] args) {
		
		
		sentimentDataList= SentimentData.parseSentimentData();
		System.out.println(sentimentDataList.size());
		sentimentDataList=ContractionLoader.preprocessContractions(sentimentDataList);
		sentimentDataList=URLRemover.removeURL(sentimentDataList);
		sentimentDataList=EmoticonLoader.preprocessEmoticons(sentimentDataList);
		/*for(int i = 0;i < sentimentDataList.size();i++)
		{
			System.out.println(sentimentDataList.get(i).getText());
		}*/
		
		ARFFGenerator.generateARIFForWeka(sentimentDataList);
		WekaTest.wekaTestRuns();
		 
	
		
	}

}
