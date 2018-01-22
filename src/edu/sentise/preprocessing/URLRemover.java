package edu.sentise.preprocessing;

import java.util.ArrayList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.sentise.model.SentimentData;

public class URLRemover {

	private static Pattern urlPattern = Pattern
			.compile("((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*/)");

	public static ArrayList<SentimentData> removeURL(ArrayList<SentimentData> sentiData) {

		for (int i = 0; i < sentiData.size(); i++) {

			sentiData.get(i).setText(removeURL(sentiData.get(i).getText()));
		}

		return sentiData;
	}

	public static String removeURL(String text) {

		Matcher m = urlPattern.matcher(text);
		while (m.find()) {
			String urlStr = m.group();

			text = text.replaceAll(urlStr, "");
		}
		return text;
	}
}
