package edu.sentise.test.elimination;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import edu.sentise.WekaClassifierBuilder;
import edu.sentise.model.SentimentData;
import edu.sentise.preprocessing.ContractionLoader;
import edu.sentise.preprocessing.EmoticonLoader;
import edu.sentise.preprocessing.MyStopWordsHandler;
import edu.sentise.preprocessing.POSUtility;
import edu.sentise.preprocessing.URLRemover;
import edu.sentise.test.ARFFTestGenerator;
import edu.sentise.util.Constants;
import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.stemmers.SnowballStemmer;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class RedundantElimination {

	private static String algo="RF";
	private static int folds=10;
	
	private static String emoticonDictionary = Constants.EMOTICONS_FILE_NAME;
	private static String contractionDictionary = Constants.CONTRACTION_TEXT_FILE_NAME;
	public static void main(String[] args) {
		Classifier[] classifiers=new Classifier[10];
		
		
		for(int i=0;i<folds;i++)
		{
			System.out.println("loading classifiers "+i+".....");
			classifiers[i]=WekaClassifierBuilder.getSavedClassfier("RF_fold_"+i);
		}
		ArrayList<SentimentData> sentiData=readNegativeInstances();
		ArrayList<SentimentData> copyData=new ArrayList<>();
		int size=sentiData.size();
		for(int i=0;i<size;i++)
		{
			copyData.add(sentiData.get(i));
		}
		//Collections.copy(copyData,sentiData);
		Instances instances=PreprocessTexts(copyData);
	
		try
		{
			FileWriter fw = new FileWriter(Constants.NEG_DATA_FILE_NAME,true); //the true will append the new data
		   ArrayList<SentimentResults> sentimentResults=new ArrayList<>();
			for(int i=0;i<size;i++)
			{
				int neg_senti=0;
				int pos_senti=0;
				int neutral_senti=0;
				for(int j=0;j<folds;j++)
				{
					int score=(int)classifiers[j].classifyInstance(instances.get(i));
					//System.out.println(score);
					if(score == 1 )     //1 for negative
						neg_senti++;
					else if(score == 0)
						neutral_senti++;
					else if(score == 2)
						pos_senti++;
				}
				sentimentResults.add(new SentimentResults(sentiData.get(i).getText(), pos_senti, neg_senti, neutral_senti));
				String s="instance: "+ i+" "+sentiData.get(i).getText()+" negative: "+neg_senti+" neutral: "+neutral_senti+" positive: "+pos_senti+"\n\n";
				System.out.println(s);
				fw.write(s);
			}
			ExcelWriter.writeInExcel(sentimentResults);
			fw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	private static ArrayList<SentimentData> readNegativeInstances()
	{
		ArrayList<SentimentData> tempList = SentimentData.parseSentimentData(Constants.ORACLE_FILE_NAME);
		ArrayList<SentimentData> sentimentDataList=new ArrayList<>();
		//loading only negative instanec
		int size=tempList.size();
		for(int i=0;i<size;i++)
		{
			if(tempList.get(i).getRating()== -1)
				sentimentDataList.add(tempList.get(i));
			if(sentimentDataList.size()>200)
				break;
		}
		return sentimentDataList;
	}
	private static Instances PreprocessTexts(ArrayList<SentimentData> sentimentDataList)
	{
	
		ContractionLoader contractionLoader=new ContractionLoader(contractionDictionary);
		EmoticonLoader emoticonLoader=new EmoticonLoader(emoticonDictionary);
		System.out.println("Reading oracle file...");
		
		System.out.println("Preprocessing text ..");
		sentimentDataList = contractionLoader.preprocessContractions(sentimentDataList);
		sentimentDataList = URLRemover.removeURL(sentimentDataList);
		sentimentDataList = emoticonLoader.preprocessEmoticons(sentimentDataList);
		
		sentimentDataList = POSUtility.preprocessPOStags(sentimentDataList);

		System.out.println("Converting to WEKA format ..");
		Instances rawInstance = ARFFTestGenerator.generateTestData(sentimentDataList);
		ARFFTestGenerator.writeInFile(rawInstance, Constants.ARFF_ORACLE_FILE_NAME);
		Instances filteredInstance=null;
		try
		{
			System.out.println("Converting string to vector..");
			filteredInstance = generateFilteredInstance(rawInstance, true);
			filteredInstance.setClassIndex(0);	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return filteredInstance;
	}
	private static Instances generateFilteredInstance(Instances instance, boolean disardLowFreqTerms) throws Exception {
		StringToWordVector filter = new StringToWordVector();
		filter.setInputFormat(instance);
		WordTokenizer customTokenizer = new WordTokenizer();
		customTokenizer.setDelimiters(Constants.DELIMITERS);
		filter.setTokenizer(customTokenizer);
		filter.setStopwordsHandler(new MyStopWordsHandler());
		SnowballStemmer stemmer = new SnowballStemmer();
		filter.setStemmer(stemmer);
		filter.setLowerCaseTokens(true);
		filter.setTFTransform(true);
		filter.setIDFTransform(true);
		//if (disardLowFreqTerms) {
			filter.setMinTermFreq(3);
			filter.setWordsToKeep(2500);
	//	}

		return Filter.useFilter(instance, filter);

	}
}
