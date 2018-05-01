package edu.sentise.preprocessing;

public class StopwordWithKeywords extends MyStopWordsHandler {

	public StopwordWithKeywords(String stopwordFile, String keywordFile) {
		super(stopwordFile);
		this.loadStopwordsFromFile(keywordFile);
	}

}
