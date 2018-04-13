package edu.sentise.preprocessing;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.mysql.jdbc.ReplicationConnectionProxy;

import edu.sentise.model.SentimentData;
import edu.sentise.util.Constants;
import edu.sentise.util.Util;

public class AncronymHandler implements TextPreprocessor {

	private HashMap<String, String> shortWordMap = null;
	private String fileName;

	public static void main(String[] args) {
		new AncronymHandler(Constants.ACRONYM_WORD_FILE);
	}
	public ArrayList<SentimentData> apply(ArrayList<SentimentData> sentiList) {
		for (int i = 0; i < sentiList.size(); i++)
			sentiList.get(i).setText(replaceShortWords((sentiList.get(i).getText())));
		return sentiList;
	}

	private String replaceShortWords(String text) {

		if (shortWordMap == null || shortWordMap.size() == 0)
			createShortWordMap(this.fileName);
		StringTokenizer st = new StringTokenizer(text, " ");

		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (shortWordMap.containsKey(token)) {
				text = text.replaceAll(token, shortWordMap.get(token));
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
