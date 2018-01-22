package edu.sentise.test2;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import edu.sentise.test.CustomImputMappedClassifer;
import edu.sentise.util.Constants;
import edu.sentise.util.Util;
import edu.stanford.nlp.pipeline.CustomAnnotationSerializer;
import weka.classifiers.Classifier;
import weka.classifiers.misc.InputMappedClassifier;
import weka.classifiers.misc.SerializedClassifier;
import weka.core.Debug;
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
		   // Debug.saveToFile("models/my_model.model", classifier);
		    
		 return classifier;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public static Classifier getSavedClassfier()
	{
		try
		{
			InputMappedClassifier classifier = new InputMappedClassifier();
			FileInputStream fis = new FileInputStream("models/my_model.model");
			ObjectInputStream ois = new ObjectInputStream(fis);

			Classifier savedClassifier = (Classifier) ois.readObject();
			    ois.close();
			//classifier.setClassifier(savedClassifier);
			//classifier.setSuppressMappingReport(true);
		  
		    
		 return savedClassifier;
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
