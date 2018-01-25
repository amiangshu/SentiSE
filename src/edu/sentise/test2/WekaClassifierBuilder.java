package edu.sentise.test2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


import edu.sentise.util.Util;

import weka.classifiers.Classifier;
import weka.classifiers.misc.InputMappedClassifier;
import weka.core.Instances;

public class WekaClassifierBuilder {

	public static InputMappedClassifier createClassifierFromInstance(String algo, Instances instance) {
		try {
			InputMappedClassifier classifier = new InputMappedClassifier();
			classifier.setClassifier(Util.getClassifierByName(algo));
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
			ois.close();

			return savedClassifier;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
