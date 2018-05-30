/*
 * Copyright (C) 2018 Southern Illinois University Carbondale, SoftSearch Lab
 *
 * Author: Amiangshu Bosu
 *
 * Licensed under GNU LESSER GENERAL PUBLIC LICENSE Version 3, 29 June 2007
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.siu.sentise.bigram;

import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

import edu.siu.sentise.Configuration;
import edu.siu.sentise.model.SentimentData;
import edu.siu.sentise.preprocessing.ContractionLoader;
import edu.siu.sentise.preprocessing.EmoticonProcessor;
import edu.siu.sentise.preprocessing.MyStopWordsHandler;
import edu.siu.sentise.preprocessing.URLRemover;
import edu.siu.sentise.util.DataLists;

public class TriGramGenerator {

	static class TriGram {
		String trigram;
		int count;
	}

	private HashMap<String, Integer> stringMap = new HashMap<>();
	private ArrayList<String> stop_words;

	public static void main(String[] args) {

	}

	public TriGramGenerator() {

		MyStopWordsHandler handler = new MyStopWordsHandler(Configuration.STOPWORDS_FILE_NAME);
		this.stop_words = handler.getStopWordList();
		ArrayList<SentimentData> sentimentDataList = SentimentData.parseSentimentData(Configuration.ORACLE_FILE_NAME);

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
				System.out.println("trigram processed:" + i + " of " + length);
			}
		}
		createListFromMap();
		ArrayList<TriGram> mList = createListFromMap();
		sortTrirams(mList);

	}

	private ArrayList<TriGram> createListFromMap() {
		ArrayList<TriGram> mList = new ArrayList<>();
		for (String key : stringMap.keySet()) {
			TriGram biGram = new TriGram();
			biGram.trigram = key;
			biGram.count = stringMap.get(key);
			mList.add(biGram);
		}
		return mList;

	}

	private void sortTrirams(ArrayList<TriGram> mList) {
		int SELECT = 500;
		Collections.sort(mList, new Comparator<TriGram>() {
			public int compare(TriGram o1, TriGram o2) {
				return o2.count - o1.count;
			};
		});
		try {
			String fileName = Configuration.TRIGRAM_FILE;

			File file = new File(fileName);
			file.createNewFile();
			BufferedWriter bufferedWriter = edu.siu.sentise.util.Util.getBufferedWriterByFileName(fileName);
			for (int i = 0; i < SELECT && i < mList.size(); i++) {
				System.out.println(mList.get(i).trigram + " " + mList.get(i).count);
				bufferedWriter.write(mList.get(i).trigram + "\n");
			}

			bufferedWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("trigram file writing done.........");

	}

	private void tokenize(String text) {
		StringTokenizer st = new StringTokenizer(text, ",.!-+1234567890:[] ())| {}[]");
		String prevToken = null;
		String prev_prevToken = null;
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (prevToken != null && prev_prevToken != null) {
				String key = prev_prevToken + " " + prevToken + " " + token;
				if (stop_words.contains(prev_prevToken) && stop_words.contains(prevToken)
						&& stop_words.contains(token)) {
					// both are stop words. choose to ignore it
				} else {
					if (stringMap.containsKey(key))

						stringMap.put(key, stringMap.get(key) + 1);
					else
						stringMap.put(key, 1);
				}
			}

			prev_prevToken = prevToken;
			prevToken = token;
		}
	}
}
