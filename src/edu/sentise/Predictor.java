package edu.sentise;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.management.RuntimeErrorException;

import com.sun.corba.se.impl.orbutil.closure.Constant;

import edu.sentise.model.ClassifierModel;
import edu.sentise.model.SentimentData;
import edu.sentise.test.elimination.ExcelWriter;
import edu.sentise.util.ClassificationResultListner;
import edu.sentise.util.SentiSeLogListner;
import edu.sentise.util.Util;
import weka.classifiers.Classifier;
import weka.classifiers.misc.InputMappedClassifier;
import weka.core.Instances;

public class Predictor {

	public static void main(String[] args) {
		
		
		
	   
	}
	public static void classifyInstances(String inputFilePath, String modelFilepath, String outputFolder, SentiSeLogListner sentiSeLogListner)
	{
		Predictor predictor= new Predictor();
		ClassifierModel classifierModel = predictor.readClassifier(modelFilepath);
		Util.sentiSeLogListner=sentiSeLogListner;
		//if(classifierModel != null)
			//System.out.println(classifierModel.getParams()[2]);
		Instances instances=predictor.getPreprocessedData(classifierModel.getParams());
		ArrayList<SentimentData> rawDataList= SentimentData.parseNewSentimentData(inputFilePath);
		if(instances!= null && classifierModel!=null)
		{
			try {
				System.out.println(""+instances.size());
			InputMappedClassifier inputMappedClassifier =  classifierModel.getClassifier();
			for(int i=0; i<instances.size();i++)		
			{
				double label=inputMappedClassifier.classifyInstance(instances.get(i));
				System.out.println(rawDataList.get(i)+"  "+label);
				String str=rawDataList.get(i).getText()+"\nclass: "+ label;
				rawDataList.get(i).setRating((int)label);
				Util.Logger(str);
			}
			ExcelWriter.writeResultsInExcel(rawDataList, outputFolder);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private Instances getPreprocessedData(String[] args)
	{
		SentiSE sentiSE = new SentiSE();
		return sentiSE.getPreprocessdInstances(Configuration.ORACLE_FILE_NAME,args);
			
	}
	
	private ClassifierModel readClassifier(String filepath) {
		File file = new File(filepath);
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			ClassifierModel classifierModel= (ClassifierModel)objectInputStream.readObject();
			objectInputStream.close();
			return classifierModel;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
