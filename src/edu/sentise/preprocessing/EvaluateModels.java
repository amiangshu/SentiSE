package edu.sentise.preprocessing;

import java.io.IOException;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

public class EvaluateModels {

	public static void evaluateModels(Instances train) {

		Classifier classifier= getClassifierByName("J48");
		evaluateClassifier(classifier, train,"J48");
		//Classifier classifier = getClassifierByName("RF");
		//evaluateClassifier(classifier, train,"RF");
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

	private static void evaluateClassifier(Classifier classifier, Instances data, String clsName) {
		try {
			int folds=10;
			double accuracy=0;
			double precision=0;
			double recall=0;
			double fmeasure=0;
			 Random rand = new Random(System.currentTimeMillis());
			    Instances randData = new Instances(data);
			    randData.randomize(rand);
			   

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
			      accuracy+=eval.pctCorrect();
			      fmeasure+=eval.fMeasure(1);
			      recall+=eval.recall(1);
			      precision+=eval.precision(1);
			      System.out.println("fold: "+n);
			      System.out.println("Accuracy:"+eval.pctCorrect());
			      System.out.println("Precision:"+eval.precision(1));
			      System.out.println("Recall:"+eval.recall(1));
			      System.out.println("Fmeasure:"+eval.fMeasure(1));
			      
			    }

			    // output evaluation
			   /* System.out.println();
			    System.out.println("=== Setup ===");
			 //   System.out.println("Classifier: " + classifier.getClass().getName() + " " + Utils.joinOptions(cls.getOptions()));
			    System.out.println("Dataset: " + data.relationName());
			    System.out.println("Folds: " + folds);
			    System.out.println("Seed: " + classifier);
			    System.out.println();
			    System.out.println(eval.toSummaryString("=== " + folds + "-fold Cross-validation ===", false));*/
			    
			    System.out.println("\n\n.......Average......: \n\n");
			      System.out.println("Accuracy:"+accuracy/folds);
			      System.out.println("Precision:"+precision/folds);
			      System.out.println("Recall:"+recall/folds);
			      System.out.println("Fmeasure:"+fmeasure/folds);
			  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static Classifier getClassifierByName(String cls)
	{ 
		if(cls.equals("NB"))
		{
			return new NaiveBayes();
		}
		else if(cls.equals("J48"))
		{
			return new J48();
		}
		else if(cls.equals("RF"))
		{
			//System.out.println("Instantiated Random Forest Classifier..");
			return new RandomForest();
		}
		
		return null;
		
	}

}