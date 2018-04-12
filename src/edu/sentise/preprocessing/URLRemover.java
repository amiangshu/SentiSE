package edu.sentise.preprocessing;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import edu.sentise.model.SentimentData;

public class URLRemover implements TextPreprocessor {

	private static Pattern urlPattern = Pattern
			.compile("((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*/)");

	public ArrayList<SentimentData> apply(ArrayList<SentimentData> sentiData) {

		for (int i = 0; i < sentiData.size(); i++) {

			sentiData.get(i).setText(removeURL(sentiData.get(i).getText()));
		}

		return sentiData;
	}

	public static String removeURL(String text) {

		Matcher m = urlPattern.matcher(text);
		while (m.find()) {
			String urlStr = m.group();

			try {
				text = text.replaceAll(urlStr, "");
			} catch (PatternSyntaxException e) {

			}
		}
		return text;
	}
}
