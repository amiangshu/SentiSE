package edu.sentise;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.functions.SMO;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.RandomCommittee;
import weka.classifiers.misc.InputMappedClassifier;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.LMT;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.classifiers.functions.Elman;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.NeuralNetwork;

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
			System.out.println("Loaded classifier model from: " + fileName);
			ois.close();

			return savedClassifier;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static Classifier getClassifierForAlgorithm(String algo) {
		if (algo.equals("NB")) {
			System.out.println("Algorithm: Multinomial Naive Bayes.");
			NaiveBayesMultinomial classifier = new NaiveBayesMultinomial();
			return classifier;
		} else if (algo.equals("DT")) {
			System.out.println("Algorithm: Decision tree");
			J48 classifier = new J48();

			return classifier;

		} else if (algo.equals("CNN")) {
			System.out.println("Algorithm: Convulated Neural Network");
			NeuralNetwork classifier = new weka.classifiers.functions.NeuralNetwork();

			try {
				classifier.setOptions(
						weka.core.Utils.splitOptions("--lr 0.0 -wp 1.0E-8 -mi 200 -bs 0 -th 12 -hl 50 -di 0.2 -dh 0.5 -iw 0"));
			classifier.setDebug(false);
			
			} catch (Exception e) {

			}
			return classifier;
		}

		else if (algo.equals("SVM")) {
			System.out.println("Algorithm: Support Vector Machine");
			return new SMO();
		}

		else if (algo.equals("MLPC")) {
			System.out.println("Algorithm: Multilayer Percptron Classifier");
			return new weka.classifiers.functions.MLPClassifier();
		}

		else if (algo.equals("SL")) {
			System.out.println("Algorithm: Simple Logistic Regression");
			return new weka.classifiers.functions.SimpleLogistic();
		}

		else if (algo.equals("KNN")) {
			System.out.println("Algorithm: K-Nearest Neighbours");
			return new weka.classifiers.lazy.IBk();
		} else if (algo.equals("LMT")) {
			LMT classifier=new LMT();
			
			System.out.println("Algorithm: Logistic Model Trees");

			return classifier;
		}
		 else if (algo.equals("RF")) {
			System.out.println("Algorithm: Random Forest");
			RandomForest classifier = new RandomForest();
			classifier.setNumExecutionSlots(4);
			try {
				classifier.setOptions(
						weka.core.Utils.splitOptions("-P 100 -I 100 -num-slots 4 -K 0 -M 1.0 -V 0.001 -S 1"));
			} catch (Exception e) {

			}

			return classifier;
		}

		else if (algo.equals("RC")) {
			System.out.println("Algorithm: Random Committee");
			RandomCommittee classifier = new weka.classifiers.meta.RandomCommittee();
			classifier.setNumExecutionSlots(4);
			try {
				classifier.setOptions(weka.core.Utils.splitOptions(
						"-S 1 -num-slots 4 -I 10 -W weka.classifiers.trees.RandomTree -- -K 0 -M 1.0 -V 0.001 -S 1"));
			} catch (Exception e) {

			}

			return classifier;
		}

		return null;

	}

}
