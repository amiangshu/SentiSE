package edu.sentise.test;

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

public class ARFFTestGenerator {

	public static void generateARFForWeka(ArrayList<SentimentData> sentiList) {
		//Collections.shuffle(sentiList);
		Instances instances = generateTestData(sentiList);
		writeInFile(instances);
		//instances=generateTestData(sentiList);
		//writeInFileTest(instances);

	}

	public static void writeInFile(Instances instances) {
		try {
			
			BufferedWriter bufferedWriter = Util.getBufferedWriterByFileName(TestUtils.TEST_DATA_ARFF_FILE);
			bufferedWriter.write(instances.toString());
			Util.closeBufferedWriter(bufferedWriter);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	public static Instances generateTestData(ArrayList<SentimentData> sentiList) {
		FastVector attributes;
		Instances data;
		attributes = new FastVector<>();
		FastVector classVectors=new FastVector<>();
		classVectors.addElement("0");
		classVectors.addElement("-1");
		classVectors.addElement("1");
		attributes.addElement(new Attribute("lab",classVectors));
		attributes.addElement(new Attribute("text", (FastVector) null));

		data = new Instances("SentiSe", attributes, 0);
		int length = sentiList.size();
		System.out.println("sentilist size: "+ length);
		//int train=(int)(length*.8);
		
		for (int i = 0; i <length; i++) {
			double[] vals = new double[data.numAttributes()];
			vals[0] = classVectors.indexOf(""+sentiList.get(i).getRating());
			vals[1] = data.attribute(1).addStringValue(sentiList.get(i).getText());
			DenseInstance denseInstance= new DenseInstance(1.0, vals);
			data.add(denseInstance);
		  // System.out.println("i "+ i +" "+sentiList.get(i).getRating()+ " "+sentiList.get(i).getText());
			
		}
		//System.out.println(data);
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
