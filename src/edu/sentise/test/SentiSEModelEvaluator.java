package edu.sentise.test;

import edu.sentise.preprocessing.ARFFGenerator;
import edu.sentise.preprocessing.EvaluateModels;
import edu.sentise.preprocessing.MyStopWordsHandler;
import edu.sentise.util.Constants;
import edu.sentise.util.Util;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.TestInstances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.stemmers.SnowballStemmer;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.supervised.instance.SMOTE;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class SentiSEModelEvaluator {

	public static void evaluateSentiSEModel()
	{
		Instances testInstances=generatetestInstances();
		Instances trainInstances=readTrainedInstances();
		evaluateByWeka(trainInstances, testInstances);
		
	}
	public static void evaluateSentiSEModelFromARFF()
	{
		Instances testInstances=readTestInstances();
		Instances trainInstances=readTrainedInstances();
		System.out.println("evaluating test data..........");
		evaluateByWekaBySingleInstances(trainInstances, testInstances);
		//evaluateByWeka(trainInstances, testInstances);
		
	}
	public static Instances readTrainedInstances() {
		DataSource dataSource;
		try {
			dataSource = new DataSource(Constants.ARFF_ORACLE_FILE_NAME_TEST);
			Instances trainInstances=dataSource.getDataSet();
			System.out.println(trainInstances.size());
			trainInstances.setClassIndex(0);
			//EvaluateModels.evaluateModels(trainInstances);
			return trainInstances;
			
		} catch (Exception e) {
		 e.printStackTrace();	
		}
		return null;
		
	}
	public static Instances readTestInstances() {
		DataSource dataSource;
		try {
			dataSource = new DataSource(TestUtils.TEST_DATA_PROCESSED_ARFF_FILE);
			Instances testInstances=dataSource.getDataSet();
			System.out.println(testInstances.size());
			testInstances.setClassIndex(0);
			//EvaluateModels.evaluateModels(trainInstances);
			return testInstances;
			
		} catch (Exception e) {
		 e.printStackTrace();	
		}
		return null;
		
	}
	private static Instances generatetestInstances() {
		try {
			DataSource dataSource = new DataSource(TestUtils.TEST_DATA_ARFF_FILE);
			Instances testInstances = dataSource.getDataSet();
			System.out.println(testInstances.size());

			StringToWordVector filter = new StringToWordVector();
			filter.setInputFormat(testInstances);

			WordTokenizer customTokenizer = new WordTokenizer();
			String delimiters = " \r\t\n.,;:\'\"()?!-><#$\\%&*+/@^=[]{}|`~0123456789\'я┐╜т┤╛я┐╜я┐╜тВДум╕я┐╜я┐╜я┐╜мту";
			customTokenizer.setDelimiters(delimiters);
			filter.setTokenizer(customTokenizer);
			filter.setStopwordsHandler(new MyStopWordsHandler());
			SnowballStemmer stemmer = new SnowballStemmer();
			filter.setStemmer(stemmer);
			filter.setLowerCaseTokens(true);
			filter.setTFTransform(true);
			filter.setIDFTransform(true);
			filter.setMinTermFreq(3);
			filter.setWordsToKeep(2500);

			// filter.setOutputWordCounts(true);

			System.out.println("Creating TF-IDF..");
			testInstances = Filter.useFilter(testInstances, filter);
			testInstances.setClassIndex(0);
			ARFFTestGenerator.writeInProcessedTestData(testInstances);

		
			return testInstances;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static void evaluateByWeka(Instances trainDataInstances,Instances testDataInstances)
	{
		 Classifier classifier = Util.getClassifierByName("RF");
		 evaluateClassifier(classifier, trainDataInstances,testDataInstances,"RF");
	}
	private static void evaluateClassifier(Classifier classifier,Instances trainDataInstances, Instances testDataInstances,String clsName) {
		try {
			classifier.buildClassifier(trainDataInstances);
			Evaluation eval = new Evaluation(trainDataInstances);
			eval.evaluateModel(classifier, testDataInstances);
			System.out.println(eval.toSummaryString("\nResults\n======\n", false));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static void evaluateByWekaBySingleInstances(Instances trainDataInstances,Instances testDataInstances)
	{
		 Classifier classifier = Util.getClassifierByName("RF");
		
		 int len=testDataInstances.size();
		 try
		 {
			 classifier.buildClassifier(trainDataInstances);
			 for(int j= 0 ;j <len;j++) 
			 {
				 double[] prediction=classifier.distributionForInstance(testDataInstances.get(j));

			        //output predictions
				 System.out.println("instance: "+j);
			        for(int i=0; i<prediction.length; i=i+1)
			        {
			            System.out.println("Probability of class "+
			                                testDataInstances.classAttribute().value(i)+
			                               " : "+Double.toString(prediction[i]));
			        }
			 }
			 
			Evaluation eval = new Evaluation(trainDataInstances);
			eval.evaluateModel(classifier, testDataInstances);
			System.out.println(eval.toSummaryString("\nResults\n======\n", false));
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
		 }
		 //evaluateClassifier(classifier, trainDataInstances,testDataInstances,"RF");
	}

}
