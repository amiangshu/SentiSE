package edu.sentise.preprocessing;

import java.io.BufferedReader;
import java.util.ArrayList;
import edu.sentise.util.Util;
import weka.core.stopwords.StopwordsHandler;

public class MyStopWordsHandler implements StopwordsHandler {
	public static ArrayList<String> stopWordsList;

	public MyStopWordsHandler(String stopwordFile) {

		stopWordsList = new ArrayList<String>(200);
		loadStopwordsFromFile(stopwordFile);
	}

	public void loadStopwordsFromFile(String file) {

		BufferedReader bufferedReader = Util.getBufferedreaderByFileName(file);
		String line = null;
		try {
			ArrayList<String> words = new ArrayList<String>();
			while ((line = bufferedReader.readLine()) != null) {
				stopWordsList.add(line.trim());
			}

			bufferedReader.close();
		} catch (Exception e) {

		}
	}

	public ArrayList<String> getStopWordList() {

		return this.stopWordsList;
	}

	@Override
	public boolean isStopword(String word) {
		return stopWordsList.contains(word);

	}

}
