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

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import edu.siu.sentise.model.SentimentData;

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
