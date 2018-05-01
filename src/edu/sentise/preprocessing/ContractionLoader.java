package edu.sentise.preprocessing;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.sentise.model.SentimentData;
import edu.sentise.util.Util;

public class ContractionLoader implements TextPreprocessor {

	private HashMap<String, String> contractionMap = null;

	private void loadContractionData(String fileName) {
		contractionMap = new HashMap<String, String>();
		BufferedReader bufferedReader = Util.getBufferedreaderByFileName(fileName);

		try {
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {

				int index = line.indexOf('	');
				String key = line.substring(0, index).trim();
				String value = line.substring(index + 1, line.length()).trim();
				contractionMap.put(key, value);
			}

			// Always close files.
			bufferedReader.close();
		}

		catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	public ContractionLoader(String fileName) {
		loadContractionData(fileName);
	}

	public String preprocessContractions(String text) {
		return applyContraction(text.toLowerCase());
	}

	public ArrayList<SentimentData> apply(ArrayList<SentimentData> sentiList) {

		for (int i = 0; i < sentiList.size(); i++) {
			sentiList.get(i).setText(preprocessContractions(sentiList.get(i).getText()));
		}
		return sentiList;
	}

	private String applyContraction(String text) {

		HashSet<String> keySet = new HashSet<String>(contractionMap.keySet());
		for (String key : keySet) {
			if (text.contains(key)) {
				text = text.replaceAll(key, contractionMap.get(key));
			}
		}
		

		return text;
	}
}
