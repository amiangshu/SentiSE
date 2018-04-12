package edu.sentise.preprocessing;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.sentise.model.SentimentData;
import edu.sentise.util.Util;

public class EmoticonProcessor implements TextPreprocessor {

	private HashMap<String, String> emoticonMap = null;

	private void loadEmoticons(String fileName) {
		emoticonMap = new HashMap<String, String>();
		try {
			BufferedReader bufferedReader = Util.getBufferedreaderByFileName(fileName);
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {

				int index = line.indexOf('	');
				String key = line.substring(0, index).trim();
				String value = line.substring(index + 1, line.length()).trim();
				emoticonMap.put(key, " " + value + " ");
			}

			// Always close files.
			bufferedReader.close();
		}

		catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	public EmoticonProcessor(String fileName) {
		loadEmoticons(fileName);
	}

	public String preprocessEmoticons(String text) {
		return replaceEmoticon(text.toLowerCase());
	}

	public ArrayList<SentimentData> apply(ArrayList<SentimentData> sentiList) {
		for (int i = 0; i < sentiList.size(); i++)
			sentiList.get(i).setText(preprocessEmoticons(sentiList.get(i).getText()));
		return sentiList;
	}

	private String replaceEmoticon(String text) {

		HashSet<String> keySet = new HashSet<String>(emoticonMap.keySet());
		for (String key : keySet) {
			// previously used if. and replace all. problem in regex compelled to use while
			while (text.contains(key)) {
				text = text.replace(key, " " + emoticonMap.get(key) + " ");
			}
		}

		return text;
	}

}
