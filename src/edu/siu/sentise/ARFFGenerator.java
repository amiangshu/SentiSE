/*
 * Copyright (C) 2018 Southern Illinois University Carbondale, SoftSearch Lab
 *
 * Author: Amiangshu Bosu
 *
 * Licensed under GNU LESSER GENERAL PUBLIC LICENSE Version 3, 29 June 2007
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.siu.sentise;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import edu.siu.sentise.model.SentimentData;
import edu.siu.sentise.util.Util;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

public class ARFFGenerator {

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

	
}
