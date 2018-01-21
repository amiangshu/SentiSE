package edu.sentise.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.javafx.scene.paint.GradientUtils.Parser;

import edu.sentise.model.SentimentData;
import edu.sentise.preprocessing.ARFFGenerator;
import edu.sentise.preprocessing.ContractionLoader;
import edu.sentise.preprocessing.EmoticonLoader;
import edu.sentise.preprocessing.NegationHandler;
import edu.sentise.preprocessing.POSTagger;
import edu.sentise.preprocessing.URLRemover;
import edu.sentise.util.Constants;
import edu.sentise.util.Util;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;


public class SentiSETest {

	private static ArrayList<SentimentData> sentimentDataList=new ArrayList<>();
	
	public static void main(String[] args) {
		
		testFromARFFile();
		
		// testFromRawFile();
	
		
	}
	private static void testFromRawFile()
	{
		sentimentDataList= SentimentData.parseSentimentData(TestUtils.TEST_DATA_FILE);
		System.out.println(sentimentDataList.size());
		sentimentDataList=ContractionLoader.preprocessContractions(sentimentDataList);
		sentimentDataList=URLRemover.removeURL(sentimentDataList);
		sentimentDataList=EmoticonLoader.preprocessEmoticons(sentimentDataList);
		/*for(int i = 0;i < sentimentDataList.size();i++)
		{
			System.out.println(sentimentDataList.get(i).getText());
		}*/
		// ARFF is the default file format for weka. I converted our clean data to arff format
		// so that its easier to be compitable with weka. Shuffled the sentilist and divided 80% and 20% of the
		//data for train and test respectively
		sentimentDataList=NegationHandler.handleNegation(sentimentDataList);
		ARFFTestGenerator.generateARFForWeka(sentimentDataList);
		SentiSEModelEvaluator.evaluateSentiSEModel();
	}
	private static void testFromARFFile()
	{
		
		SentiSEModelEvaluator.evaluateSentiSEModelFromARFF();
	}

}
