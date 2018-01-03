package edu.sentise.preprocessing;

import java.io.IOException;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class EvaluateModels {

	public static void evaluateModels(Instances train, Instances test) {

		Classifier classifier= new J48();
		evaluateClassifier(classifier, train, test);
		classifier = new NaiveBayes();
		evaluateClassifier(classifier, train, test);
	}

	private static void evaluateClassifier(Classifier classifier, Instances train, Instances test) {
		try {
			classifier.buildClassifier(train);
			Evaluation eval = new Evaluation(train);
			eval.evaluateModel(classifier, test);
			System.out.println(eval.toSummaryString("\nResults\n======\n", false));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
