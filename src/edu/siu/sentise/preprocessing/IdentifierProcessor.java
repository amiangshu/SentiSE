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

import edu.siu.sentise.model.SentimentData;

public class IdentifierProcessor implements TextPreprocessor {

	final String camelCaseRegex = "[A-Za-z]([A-Z0-9]*[a-z][a-z0-9]*[A-Z]|[a-z0-9]*[A-Z][A-Z0-9]*[a-z])[A-Za-z0-9]*";
	final String allCAPSRegex="([A-Z][A-Z]+)";

	final String regexWordsWithNumbers = "\\w*\\d\\w*";
	final String regexWordsWithUnderscore = "\\w*_\\w*";
	final String replacement = " ";

	@Override
	public ArrayList<SentimentData> apply(ArrayList<SentimentData> sentiList) {
		for (int i = 0; i < sentiList.size(); i++)

		{
			String origText = sentiList.get(i).getText();
			String modifiedText = removeCameCaseWords(origText);
			modifiedText=removeWordsWithNumbers(origText);
			
			sentiList.get(i).setText(modifiedText);
		}
		return sentiList;
	}

	String removeCameCaseWords(String text) { 

		return text.replaceAll(camelCaseRegex, replacement);
	}
	
	
	String removeAllCAPS(String text) { //Not applied since some word can be written in all caps for emphasis

		return text.replaceAll(allCAPSRegex, replacement);
	}
	
	
	String removeWordsWithNumbers(String text) {

		return text.replaceAll(regexWordsWithNumbers, replacement);
		
	}
	
	String removeWordsWithUnderscores(String text) {

		return text.replaceAll(regexWordsWithUnderscore, replacement);
		
	}

	public static void main(String[] args) {

		String test = "camelCase this well amin_gerat23 nice_try History2Lession IFoo HTTPConnection Good1 good1 this.ElectionModel bad Touch leaF CORRECT GOT_IT";
		IdentifierProcessor p = new IdentifierProcessor();
		String modText=p.removeCameCaseWords(test);
		modText=p.removeWordsWithNumbers(modText);	
		modText=p.removeWordsWithUnderscores(modText);
		//modText=p.removeAllCAPS(modText);

		System.out.println(modText);

	}

}
