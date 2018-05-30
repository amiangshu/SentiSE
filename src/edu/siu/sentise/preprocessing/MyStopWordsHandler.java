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

package edu.siu.sentise.preprocessing;

import java.io.BufferedReader;
import java.util.ArrayList;

import edu.siu.sentise.util.Util;
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
