package edu.sentise.bigram;

import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

import edu.sentise.Configuration;
import edu.sentise.model.SentimentData;
import edu.sentise.preprocessing.ContractionLoader;
import edu.sentise.preprocessing.EmoticonProcessor;
import edu.sentise.preprocessing.MyStopWordsHandler;
import edu.sentise.preprocessing.URLRemover;
import edu.sentise.util.DataLists;

public class BigramGenerator {

	class BiGram {
		String bigram;
		int count;
	}

	private HashMap<String, Integer> stringMap = new HashMap<>();
	private ArrayList<String> stop_words;

	public static void main(String[] args) {
		BigramGenerator instance = new BigramGenerator();
	}

	public BigramGenerator() {

		ArrayList<SentimentData> sentimentDataList = SentimentData.parseSentimentData(Configuration.ORACLE_FILE_NAME);

		MyStopWordsHandler handler = new MyStopWordsHandler(Configuration.STOPWORDS_FILE_NAME);

		this.stop_words = handler.getStopWordList();
		System.out.println("Preprocessing text ..");
		ContractionLoader contractionLoader = new ContractionLoader(Configuration.CONTRACTION_TEXT_FILE_NAME);
		sentimentDataList = contractionLoader.apply(sentimentDataList);
		URLRemover remover = new URLRemover();
		sentimentDataList = remover.apply(sentimentDataList);
		EmoticonProcessor emoticonHandler = new EmoticonProcessor(Configuration.EMOTICONS_FILE_NAME);
		sentimentDataList = emoticonHandler.apply(sentimentDataList);
		lematizeSentimentData(sentimentDataList);
	}

	public void lematizeSentimentData(ArrayList<SentimentData> sentimentData) {

		int length = sentimentData.size();
		for (int i = 0; i < length; i++) {

			// System.out.println(sentimentData.get(i).getText());
			tokenize(sentimentData.get(i).getText());
			if ((i % 100) == 0) {
				System.out.println("bigram processed:" + i + " of " + length);
			}
		}
		createListFromMap();
		ArrayList<BiGram> mList = createListFromMap();
		sortBigrams(mList);

	}

	private ArrayList<BiGram> createListFromMap() {
		ArrayList<BiGram> mList = new ArrayList<>();
		for (String key : stringMap.keySet()) {
			BiGram biGram = new BiGram();
			biGram.bigram = key;

			biGram.count = stringMap.get(key);
			mList.add(biGram);
		}
		return mList;

	}

	private void sortBigrams(ArrayList<BiGram> mList) {
		int SELECT = 500;
		Collections.sort(mList, new Comparator<BiGram>() {
			public int compare(BiGram o1, BiGram o2) {
				return o2.count - o1.count;
			};
		});
		try {
			String fileName = Configuration.BIGRAM_FILE;

			File file = new File(fileName);
			file.createNewFile();
			BufferedWriter bufferedWriter = edu.sentise.util.Util.getBufferedWriterByFileName(fileName);
			for (int i = 0; i < SELECT && i < mList.size(); i++) {
				String bigramText = mList.get(i).bigram;

				System.out.println(bigramText + ", " + mList.get(i).count);
				bufferedWriter.write(mList.get(i).bigram + "\n");

			}

			bufferedWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(" file writing done.........");

	}

	private void tokenize(String text) {
		StringTokenizer st = new StringTokenizer(text, ",.!-+1234567890:[] ()) {}[]");
		String prevToken = null;
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (prevToken != null) {
				String key = prevToken + " " + token;
				if (stop_words.contains(prevToken) || stop_words.contains(token)) {
					// both are stop words. choose to ignore it
				} else {
					if (stringMap.containsKey(key))

						stringMap.put(key, stringMap.get(key) + 1);
					else
						stringMap.put(key, 1);
				}
			}
			prevToken = token;
		}
	}
}
