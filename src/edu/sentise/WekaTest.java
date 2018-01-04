package edu.sentise;

import javax.xml.transform.Source;

import org.apache.poi.hslf.record.Sound;

import edu.sentise.preprocessing.EvaluateModels;
import edu.sentise.preprocessing.MyStopWordsHandler;
import edu.sentise.util.Constants;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.stemmers.SnowballStemmer;
import weka.core.stopwords.StopwordsHandler;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

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
	public static void wekaTestRuns()
	{
		try {
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
			
			dataSource = new DataSource(Constants.ARFF_ORACLE_FILE_NAME_TEST);

			Instances testInstances=dataSource.getDataSet();
			System.out.println(testInstances.size());
		
			Instances testFilteredInstances= Filter.useFilter(testInstances, filter);
			if (testFilteredInstances.classIndex() == -1)
				testFilteredInstances.setClassIndex(0);
			
			 /*for(int i=0; i<testFilteredInstances.numInstances(); i++) {
				//System.out.println( testFilteredInstances.instance(i).attribute(0));
		            double index = classifier.classifyInstance(testFilteredInstances.instance(i));
		            System.out.println(index);
			 }*/
			EvaluateModels.evaluateModels(trainedFilteredInstances, testFilteredInstances);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
