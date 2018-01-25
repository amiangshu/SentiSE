package edu.sentise.test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import edu.sentise.model.SentimentData;
import edu.sentise.util.Util;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

public class ARFFTestGenerator {

	public static void writeInFile(Instances instances, String fileName) {
		try {

			BufferedWriter bufferedWriter = Util.getBufferedWriterByFileName(fileName);
			bufferedWriter.write(instances.toString());
			Util.closeBufferedWriter(bufferedWriter);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static Instances generateTestData(ArrayList<SentimentData> sentiList) {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		Instances data;
		ArrayList<String> classVectors = new ArrayList<String>();
		classVectors.add("0");
		classVectors.add("-1");
		classVectors.add("1");
		attributes.add(new Attribute("lab", classVectors));
		attributes.add(new Attribute("text", (ArrayList) null));

		data = new Instances("SentiSe", attributes, 0);
		int length = sentiList.size();
		//System.out.println("sentilist size: " + length);

		for (int i = 0; i < length; i++) {
			double[] vals = new double[data.numAttributes()];
			vals[0] = classVectors.indexOf("" + sentiList.get(i).getRating());
			vals[1] = data.attribute(1).addStringValue(sentiList.get(i).getText());
			DenseInstance denseInstance = new DenseInstance(1.0, vals);
			data.add(denseInstance);

		}

		return data;
	}

	public static Instances generateTestDataFromString(ArrayList<String> sentiList) {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		Instances data;

		attributes.add(new Attribute("text", (ArrayList) null));

		data = new Instances("SentiSe", attributes, 0);
		int length = sentiList.size();
		
		for (int i = 0; i < length; i++) {
			double[] vals = new double[data.numAttributes()];

			vals[0] = data.attribute(0).addStringValue(sentiList.get(i));
			DenseInstance denseInstance = new DenseInstance(1.0, vals);
			data.add(denseInstance);

		}
		// System.out.println(data);
		return data;
	}

	public static void writeInProcessedTestData(Instances instances) {
		try {

			BufferedWriter bufferedWriter = Util.getBufferedWriterByFileName(TestUtils.TEST_DATA_PROCESSED_ARFF_FILE);
			bufferedWriter.write(instances.toString());
			Util.closeBufferedWriter(bufferedWriter);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
