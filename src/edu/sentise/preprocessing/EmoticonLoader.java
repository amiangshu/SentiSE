package edu.sentise.preprocessing;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import edu.sentise.model.SentimentData;
import edu.sentise.util.Constants;
import edu.sentise.util.Util;

public class EmoticonLoader {

	private static HashMap<String, String> emoticonMap = null;

	private static void loadEmoticons() {
		emoticonMap = new HashMap<String, String>();
		try {
			BufferedReader bufferedReader = Util.getBufferedreaderByFileName(Constants.EMOTICONS_FILE_NAME);
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

	public static String preprocessEmoticons(String text) {
		if (emoticonMap == null)
			loadEmoticons();

		return replaceEmoticon(text.toLowerCase());
	}

	public static ArrayList<SentimentData> preprocessEmoticons(ArrayList<SentimentData> sentiList) {
		for (int i = 0; i < sentiList.size(); i++)
			sentiList.get(i).setText(preprocessEmoticons(sentiList.get(i).getText()));
		return sentiList;
	}

	private static String replaceEmoticon(String text) {

		HashSet<String> keySet = new HashSet<String>(emoticonMap.keySet());
		for (String key : keySet) {
			// previously used if. and replace all. problem in regex compelled to use while
			while (text.contains(key)) {
				// System.out.println(sentiList.get(i).getText()+" "+ key);
				text = text.replace(key, " " + emoticonMap.get(key) + " ");
				// System.out.println(sentiList.get(i).getText());
			}
		}

		return text;
	}
}
