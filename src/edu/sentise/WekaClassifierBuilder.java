package edu.sentise;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.functions.SMO;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.misc.InputMappedClassifier;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.classifiers.functions.MultilayerPerceptron;

public class WekaClassifierBuilder {

	public static InputMappedClassifier createClassifierFromInstance(String algo, Instances instance) {
		try {
			InputMappedClassifier classifier = new InputMappedClassifier();
			classifier.setClassifier(getClassifierForAlgorithm(algo));
			classifier.setSuppressMappingReport(true);
			classifier.buildClassifier(instance);

			return classifier;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean storeClassfierModel(String fileName, Classifier classifier) {
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(classifier);
			oos.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Classifier getSavedClassfier(String fileName) {
		try {
			FileInputStream fis = new FileInputStream(fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);

			Classifier savedClassifier = (Classifier) ois.readObject();
			System.out.println("Loaded classifier model from: "+fileName);
			ois.close();

			return savedClassifier;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public static Classifier getClassifierForAlgorithm(String algo) {
		if (algo.equals("NB")) {
			
			System.out.println("Algorithm: Multinomial Naive Bayes." );
			return new NaiveBayesMultinomial();
		} else if (algo.equals("DT")) {
			System.out.println("Algorithm: Decision tree" );
			return new J48();
			
		}
		else if (algo.equals("ADB")) {
			return new AdaBoostM1();
		}
		else if (algo.equals("SVM")) {
			return new SMO();
		}
		
		else if (algo.equals("MLPC")) {
			return new MultilayerPerceptron();
		}
		
		else if(algo.equals("SL")) {
			return new weka.classifiers.functions.SimpleLogistic();
		}
		
		else if (algo.equals("KNN")) {
			return new weka.classifiers.lazy.IBk();
		}
		else if (algo.equals("RF")) {
			System.out.println("Algorithm: Random Forest" );
			RandomForest classifier = new RandomForest();
			try {
				classifier.setOptions(
						weka.core.Utils.splitOptions("-P 100 -I 100 -num-slots 1 -K 0 -M 1.0 -V 0.001 -S 1"));
			} catch (Exception e) {

			}

			return classifier;
		}

		return null;

	}


}
