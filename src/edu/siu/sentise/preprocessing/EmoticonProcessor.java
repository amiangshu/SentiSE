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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.siu.sentise.model.SentimentData;
import edu.siu.sentise.util.Util;

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

		//System.out.println(text);
		HashSet<String> keySet = new HashSet<String>(emoticonMap.keySet());
		for (String key : keySet) {
			// previously used if. and replace all. problem in regex compelled to use while
			while (text.contains(key)) {
				text = text.replace(key, " " + emoticonMap.get(key) + " ");
			}
		}

		//System.out.println(text);
		return text;
	}

}
