package edu.sentise.preprocessing;

import java.io.IOException;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

public class EvaluateModels {

	public static void evaluateModels(Instances train, Instances test) {

		Classifier classifier= new J48();
		evaluateClassifier(classifier, train, test);
		classifier = new NaiveBayes();
		evaluateClassifier(classifier, train, test);
		/*RandomForest classifier=new RandomForest();
		classifier.setNumIterations(40);
		evaluateClassifier(classifier, train, test);
		/*AdaBoostM1 adaBoostM1=new AdaBoostM1();
		adaBoostM1.setClassifier(new J48());
		adaBoostM1.setNumIterations(7);
		evaluateClassifier(adaBoostM1, train, test);
		weka.classifiers.meta.LogitBoost lb = new weka.classifiers.meta.LogitBoost();


		weka.classifiers.trees.REPTree rep = new weka.classifiers.trees.REPTree();

		rep.setMaxDepth(6);

		lb.setClassifier(rep);

		lb.setNumIterations(10);
		lb.setShrinkage(0.1);
		evaluateClassifier(lb, train, test);*/

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
