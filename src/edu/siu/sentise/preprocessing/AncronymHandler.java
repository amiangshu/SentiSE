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
import java.util.HashMap;
import java.util.HashSet;

import edu.siu.sentise.Configuration;
import edu.siu.sentise.model.SentimentData;
import edu.siu.sentise.util.Util;

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
