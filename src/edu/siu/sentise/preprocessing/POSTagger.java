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

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class POSTagger {

	public static void main(String[] args) {
		addPOSToText();
	}
	private static String tagPartsOfSpeech(MaxentTagger tagger, String text)
	{
		String tagged = tagger.tagString(text);
		// Output the result
		return tagged;
	}
	public static void addPOSToText()
	{
		//try
		//{
		MaxentTagger tagger = new MaxentTagger("src/taggers/bidirectional-distsim-wsj-0-18.tagger");
		String sample = "This is a sample text";
		String tagged = tagPartsOfSpeech(tagger,sample);
		System.out.println(tagged);
		
		 
		
		/*}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}*/
	}
	private static String getPOSToWord(MaxentTagger tagger, String word)
	{
		String tagged = tagger.tagString(word);
		// Output the result
		return tagged;
	}
	public static String addPOSToWord(String word)
	{
		MaxentTagger tagger = new MaxentTagger("src/taggers/bidirectional-distsim-wsj-0-18.tagger");
		//String sample = "This is a sample text";
		String tagged = getPOSToWord(tagger,word);
		return tagged;
	}
	
}
