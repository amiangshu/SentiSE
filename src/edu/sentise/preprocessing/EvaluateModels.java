package edu.sentise.preprocessing;

import java.io.IOException;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.RandomTree;
import weka.core.Instances;

public class EvaluateModels {

	public static void evaluateModels(Instances train) {

		//Classifier classifier = getClassifierByName("J48");
		//evaluateClassifier(classifier, train, "J48");
		 Classifier classifier = getClassifierByName("RF");
		 evaluateClassifier(classifier, train,"RF");
		/*
		 * RandomForest classifier=new RandomForest(); classifier.setNumIterations(40);
		 * evaluateClassifier(classifier, train, test); /*AdaBoostM1 adaBoostM1=new
		 * AdaBoostM1(); adaBoostM1.setClassifier(new J48());
		 * adaBoostM1.setNumIterations(7); evaluateClassifier(adaBoostM1, train, test);
		 * weka.classifiers.meta.LogitBoost lb = new weka.classifiers.meta.LogitBoost();
		 * 
		 * 
		 * weka.classifiers.trees.REPTree rep = new weka.classifiers.trees.REPTree();
		 * 
		 * rep.setMaxDepth(6);
		 * 
		 * lb.setClassifier(rep);
		 * 
		 * lb.setNumIterations(10); lb.setShrinkage(0.1); evaluateClassifier(lb, train,
		 * test);
		 */

	}

	private static void evaluateClassifier(Classifier classifier, Instances data, String clsName) {
		try {
			int folds = 10;
			
			Random rand = new Random(System.currentTimeMillis());
			Instances randData = new Instances(data);
			randData.randomize(rand);

			double pos_precision[] = new double[10];
			double neg_precision[] = new double[10];
			double neu_precision[] = new double[10];

			double pos_recall[] = new double[10];
			double neg_recall[] = new double[10];
			double neu_recall[] = new double[10];

			double accuracies[] = new double[10];

			// perform cross-validation
			Evaluation eval = new Evaluation(randData);
			for (int n = 0; n < folds; n++) {
				Instances train = randData.trainCV(folds, n);
				Instances test = randData.testCV(folds, n);
				// the above code is used by the StratifiedRemoveFolds filter, the
				// code below by the Explorer/Experimenter:
				// Instances train = randData.trainCV(folds, n, rand);

				// build and evaluate classifier
				Classifier clsCopy = getClassifierByName(clsName);
				clsCopy.buildClassifier(train);
				eval.evaluateModel(clsCopy, test);
				accuracies[folds] = eval.pctCorrect();

				neu_precision[folds] = eval.precision(0);
				neg_precision[folds] = eval.precision(1);
				pos_precision[folds] = eval.precision(2);

				neu_recall[folds] = eval.precision(0);
				neg_recall[folds] = eval.precision(1);
				pos_recall[folds] = eval.precision(2);

				System.out.println("Accuracy:" + eval.pctCorrect());

				System.out.println(" Precision(neutral):" + eval.precision(0));
				System.out.println("Recall(neutral):" + eval.recall(0));
				System.out.println("Fmeasure(neutral):" + eval.fMeasure(0));

				System.out.println(" Precision(negative):" + eval.precision(1));
				System.out.println("Recall(negative):" + eval.recall(1));
				System.out.println("Fmeasure(negative):" + eval.fMeasure(1));

				System.out.println(" Precision(positive):" + eval.precision(2));
				System.out.println("Recall(positive):" + eval.recall(2));
				System.out.println("Fmeasure(positive):" + eval.fMeasure(2));

			}

			
			System.out.println("\n\n.......Average......: \n\n");
			System.out.println("Accuracy:" + getAverage(accuracies));
			
			System.out.println("Precision (Neutral):" + getAverage(neu_precision));
			System.out.println("Recall (Neutral):" + getAverage(neu_recall));
			System.out.println("Precision (Negative):" + getAverage(neg_precision));
			System.out.println("Recall (Negative):" + getAverage(neg_recall));
			System.out.println("Precision (Positive):" + getAverage(pos_precision));
			System.out.println("Recall (Positive):" + getAverage(pos_recall));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static double getAverage(double[] elements) {
		double sum = 0.0;
		for (int i = 0; i < elements.length; i++)
			sum = sum + elements[i];

		// calculate average value
		double average = sum / elements.length;
		return average;
	}

	private static Classifier getClassifierByName(String cls) {
		if (cls.equals("NB")) {
			return new NaiveBayes();
		} else if (cls.equals("J48")) {
			return new J48();
		} else if (cls.equals("RF")) {
			// System.out.println("Instantiated Random Forest Classifier..");
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
