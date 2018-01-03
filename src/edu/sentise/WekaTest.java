package edu.sentise;

import javax.xml.transform.Source;

import org.apache.poi.hslf.record.Sound;

import edu.sentise.util.Constants;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class WekaTest {

	public static void main(String[] args) {
		try {
			DataSource dataSource = new DataSource(Constants.ARFF_ORACLE_FILE_NAME);
			Instances trainInstances=dataSource.getDataSet();
			System.out.println(trainInstances.size());
			
			
			StringToWordVector filter = new StringToWordVector();
			filter.setInputFormat(trainInstances);
			
			Instances trainedFilteredInstances= Filter.useFilter(trainInstances, filter);
			if (trainedFilteredInstances.classIndex() == -1)
				trainedFilteredInstances.setClassIndex(0);
			J48 classifier=new J48();
			classifier.buildClassifier(trainedFilteredInstances);
			//System.out.println("\n\nClassifier model:\n\n" + classifier);
			
			//evaluation
			dataSource = new DataSource(Constants.ARFF_ORACLE_FILE_NAME_TEST);

			Instances testInstances=dataSource.getDataSet();
			System.out.println(testInstances.size());
			
			filter = new StringToWordVector();
			filter.setInputFormat(testInstances);
			
			Instances testFilteredInstances= Filter.useFilter(testInstances, filter);
			if (testFilteredInstances.classIndex() == -1)
				testFilteredInstances.setClassIndex(0);
			Evaluation eval = new Evaluation(trainedFilteredInstances);
			 eval.evaluateModel(classifier, testFilteredInstances);
			 System.out.println(eval.toSummaryString("\nResults\n======\n", false));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
