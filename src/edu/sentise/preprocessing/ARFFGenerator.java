package edu.sentise.preprocessing;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import edu.sentise.model.SentimentData;
import edu.sentise.util.Constants;
import edu.sentise.util.Util;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instances;

public class ARFFGenerator {

	public static void generateARIFForWeka(ArrayList<SentimentData> sentiList) {
		Collections.shuffle(sentiList);
		Instances instances = generateTrainData(sentiList);
		writeInFile(instances);
		instances=generateTestData(sentiList);
		writeInFileTest(instances);

	}

	private static void writeInFile(Instances instances) {
		try {
			
			BufferedWriter bufferedWriter = Util.getBufferedWriterByFileName(Constants.ARFF_ORACLE_FILE_NAME);
			bufferedWriter.write(instances.toString());
			Util.closeBufferedWriter(bufferedWriter);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	private static void writeInFileTest(Instances instances) {
		try {
			
			BufferedWriter bufferedWriter = Util.getBufferedWriterByFileName(Constants.ARFF_ORACLE_FILE_NAME_TEST);
			bufferedWriter.write(instances.toString());
			Util.closeBufferedWriter(bufferedWriter);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static Instances generateTrainData(ArrayList<SentimentData> sentiList) {
		FastVector attributes;
		Instances data;
		attributes = new FastVector<>();
		FastVector classVectors=new FastVector<>();
		classVectors.addElement("0");
		classVectors.addElement("-1");
		attributes.addElement(new Attribute("label",classVectors));
		attributes.addElement(new Attribute("text", (FastVector) null));

		data = new Instances("SentiSe", attributes, 0);
		int length = sentiList.size();
		int train=(int)(length*.8);
		
		for (int i = 0; i <train; i++) {
			double[] vals = new double[data.numAttributes()];
			vals[0] = classVectors.indexOf(""+sentiList.get(i).getRating());
			vals[1] = data.attribute(1).addStringValue(sentiList.get(i).getText());
			data.add(new DenseInstance(1.0, vals));
		}
		System.out.println(data);
		return data;
	}
	private static Instances generateTestData(ArrayList<SentimentData> sentiList) {
		FastVector attributes;
		Instances data;
		attributes = new FastVector<>();
		FastVector classVectors=new FastVector<>();
		classVectors.addElement("0");
		classVectors.addElement("-1");
		attributes.addElement(new Attribute("label",classVectors));
		attributes.addElement(new Attribute("text", (FastVector) null));

		data = new Instances("SentiSe", attributes, 0);
		int length = sentiList.size();
		int train=(int)(length*.8);
		Collections.shuffle(sentiList);
		for (int i = train; i < length; i++) {
			double[] vals = new double[data.numAttributes()];
			vals[0] = classVectors.indexOf(""+sentiList.get(i).getRating());
			vals[1] = data.attribute(1).addStringValue(sentiList.get(i).getText());
			data.add(new DenseInstance(1.0, vals));
		}
		System.out.println(data);
		return data;
	}
}
