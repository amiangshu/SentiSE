package edu.sentise.test2;

import edu.sentise.test.CustomImputMappedClassifer;
import edu.sentise.util.Constants;
import edu.sentise.util.Util;
import edu.stanford.nlp.pipeline.CustomAnnotationSerializer;
import weka.classifiers.Classifier;
import weka.classifiers.misc.InputMappedClassifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class WekaClassifierBuilder {

	private static Instances readTrainedInstances() {
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
	public static InputMappedClassifier getClassfier()
	{
		try
		{
			InputMappedClassifier classifier = new InputMappedClassifier();
			classifier.setClassifier(Util.getClassifierByName("RF"));
			classifier.setSuppressMappingReport(true);
		    classifier.buildClassifier(readTrainedInstances());
		    
		 return classifier;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public static CustomImputMappedClassifer getTestClassfier()
	{
		try
		{
			CustomImputMappedClassifer classifier = new CustomImputMappedClassifer();
			classifier.setClassifier(Util.getClassifierByName("RF"));
		    classifier.buildClassifier(readTrainedInstances());
		 return classifier;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
