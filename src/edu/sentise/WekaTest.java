package edu.sentise;

import javax.xml.transform.Source;

import org.apache.poi.hslf.record.Sound;

import edu.sentise.preprocessing.ARFFGenerator;
import edu.sentise.preprocessing.EvaluateModels;
import edu.sentise.preprocessing.MyStopWordsHandler;
import edu.sentise.util.Constants;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.stemmers.SnowballStemmer;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.supervised.instance.SMOTE;

public class WekaTest {

	public static void main(String[] args) {
		/*try {
			DataSource dataSource = new DataSource(Constants.ARFF_ORACLE_FILE_NAME);
			Instances trainInstances=dataSource.getDataSet();
			System.out.println(trainInstances.size());
			
			
			StringToWordVector filter = new StringToWordVector();
			filter.setInputFormat(trainInstances);
			filter.setStopwordsHandler(new MyStopWordsHandler());
			SnowballStemmer stemmer = new SnowballStemmer();
			filter.setStemmer(stemmer);
			filter.setLowerCaseTokens(true);
			
			Instances trainedFilteredInstances= Filter.useFilter(trainInstances, filter);
			if (trainedFilteredInstances.classIndex() == -1)
				trainedFilteredInstances.setClassIndex(0);
			//J48 classifier=new J48();
			Classifier classifier=new NaiveBayes();
			MultilayerPerceptron classifier = new MultilayerPerceptron();
			//Setting Parameters
			classifier.setLearningRate(0.1);
			classifier.setMomentum(0.2);
			classifier.setTrainingTime(2000);
			classifier.setHiddenLayers("1");
			classifier.buildClassifier(trainedFilteredInstances);
			
			//System.out.println("\n\nClassifier model:\n\n" + classifier);
			
			//evaluation
			dataSource = new DataSource(Constants.ARFF_ORACLE_FILE_NAME_TEST);

			Instances testInstances=dataSource.getDataSet();
			System.out.println(testInstances.size());
			
			filter = new StringToWordVector();
			filter.setInputFormat(testInstances);
			filter.setStopwordsHandler(new MyStopWordsHandler());
			stemmer = new SnowballStemmer();
			filter.setStemmer(stemmer);
			filter.setLowerCaseTokens(true);
			
			Instances testFilteredInstances= Filter.useFilter(testInstances, filter);
			if (testFilteredInstances.classIndex() == -1)
				testFilteredInstances.setClassIndex(0);
			
			 for(int i=0; i<testFilteredInstances.numInstances(); i++) {
				//System.out.println( testFilteredInstances.instance(i).attribute(0));
		            double index = classifier.classifyInstance(testFilteredInstances.instance(i));
		            System.out.println(index);
			 }
			
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		wekaTestRuns();
	}
	
	public static void EvaluateInstancefromARFF() {
		DataSource dataSource;
		try {
			dataSource = new DataSource(Constants.ARFF_ORACLE_FILE_NAME_TEST);
			Instances trainInstances=dataSource.getDataSet();
			System.out.println(trainInstances.size());
			trainInstances.setClassIndex(0);
			EvaluateModels.evaluateModels(trainInstances);
			
		} catch (Exception e) {
			
		}
		
	}
	public static void wekaTestRuns()
	{
		try {
			DataSource dataSource = new DataSource(Constants.ARFF_ORACLE_FILE_NAME);
			Instances trainInstances=dataSource.getDataSet();
			System.out.println(trainInstances.size());
			
			
			StringToWordVector filter = new StringToWordVector();
			filter.setInputFormat(trainInstances);
			
			WordTokenizer customTokenizer = new WordTokenizer();
			String delimiters = " \r\t\n.,;:\'\"()?!-><#$\\%&*+/@^=[]{}|`~0123456789\'ï¿½â´¾ï¿½ï¿½â‚„ã¬¸ï¿½ï¿½ï¿½¬âã";
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
		
			//filter.setOutputWordCounts(true);
			
			
			System.out.println("Creating TF-IDF..");			
			Instances trainedFilteredInstances= Filter.useFilter(trainInstances, filter);
			trainedFilteredInstances.setClassIndex(0);
			
			System.out.println("Applying SMOTE oversampling..");
			SMOTE oversampler=new SMOTE();
			oversampler.setNearestNeighbors(15);
			oversampler.setClassValue("3");
			//oversampler.setPercentage(90.0);
			oversampler.setInputFormat(trainedFilteredInstances);
			trainedFilteredInstances=Filter.useFilter(trainedFilteredInstances, oversampler);
			
			SMOTE oversampler2=new SMOTE();
			oversampler2.setClassValue("2");
			oversampler2.setNearestNeighbors(15);
			oversampler2.setPercentage(33.0);
			oversampler2.setInputFormat(trainedFilteredInstances);
			
			trainedFilteredInstances=Filter.useFilter(trainedFilteredInstances, oversampler2);
			//trainedFilteredInstances.
			
			ARFFGenerator.writeInFileTest(trainedFilteredInstances);
						
			EvaluateModels.evaluateModels(trainedFilteredInstances);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
