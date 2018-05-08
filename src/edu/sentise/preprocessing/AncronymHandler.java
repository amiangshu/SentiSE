package edu.sentise.preprocessing;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.sentise.Configuration;
import edu.sentise.model.SentimentData;
import edu.sentise.util.Util;

public class AncronymHandler implements TextPreprocessor {

	private HashMap<String, String> shortWordMap = null;
	private String fileName;

	public static void main(String[] args) {
		new AncronymHandler(Configuration.ACRONYM_WORD_FILE);
	}
	public ArrayList<SentimentData> apply(ArrayList<SentimentData> sentiList) {
		for (int i = 0; i < sentiList.size(); i++)
			sentiList.get(i).setText(replaceShortWords((sentiList.get(i).getText())));
		return sentiList;
	}

	private String replaceShortWords(String text) {

		if (shortWordMap == null || shortWordMap.size() == 0)
			createShortWordMap(this.fileName);
		
		HashSet<String> keySet = new HashSet<String>(shortWordMap.keySet());
		for (String key : keySet) {
			// previously used if. and replace all. problem in regex compelled to use while
			while (text.contains(key)) {
				text = text.replace(key, " " + shortWordMap.get(key) + " ");
			}
		}
		return text;
	}

	public AncronymHandler(String fileName) {
		this.fileName = fileName;
		createShortWordMap(fileName);
	}

	private void createShortWordMap(String fileName) {
		shortWordMap = new HashMap<>();
		BufferedReader bufferedReader = Util.getBufferedreaderByFileName(fileName);
		String line = null;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				String[] parse = line.split(" ",2);
				shortWordMap.put(parse[0], parse[1]);
				//System.out.println(parse[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
}
