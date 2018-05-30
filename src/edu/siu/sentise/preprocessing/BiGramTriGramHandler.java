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
import java.util.StringTokenizer;

import com.mysql.jdbc.ReplicationConnectionProxy;

import edu.siu.sentise.Configuration;
import edu.siu.sentise.model.SentimentData;
import edu.siu.sentise.util.Util;

public class BiGramTriGramHandler implements TextPreprocessor {

	private static HashMap<String,String> bigram_map= null;
	private static HashMap<String,String> trigram_map= null;
	public static void main(String[] args) {
		System.out.println(new BiGramTriGramHandler().replacenGrams(" i would like to tell you that big fan not sure"));
		
	}
	@Override
	public ArrayList<SentimentData> apply(ArrayList<SentimentData> sentimentData) {
		for (int i = 0; i < sentimentData.size(); i++)
			sentimentData.get(i).setText(replacenGrams((sentimentData.get(i).getText())));
		return sentimentData;
	}
	

	private   String replacenGrams(String text) {

		
		
		if(bigram_map == null || bigram_map.size() == 0)
			createBiGram();
		if(trigram_map == null || trigram_map.size() == 0)
			createTriGram();
		
		for(String key : trigram_map.keySet())
			if(text.contains(key))
				text=text.replaceAll(key, trigram_map.get(key));
		
		for(String key : bigram_map.keySet())
			if(text.contains(key))
			{
				
				text=text.replaceAll(key, bigram_map.get(key));
			}
				
		
		
		
		//System.out.println(text);
		return text;
		
	}
	private static void createBiGram()
	{
		bigram_map= new HashMap<>();
		BufferedReader bufferedReader=Util.getBufferedreaderByFileName(Configuration.BIGRAM_FILE);
		String line=null;
		try
		{
			while((line = bufferedReader.readLine())!= null)
			{
				String val=" "+line.replaceAll(" ", "_")+" ";
				bigram_map.put(line,val);				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
	}
	private static void createTriGram()
	{
		trigram_map= new HashMap<>();
		BufferedReader bufferedReader=Util.getBufferedreaderByFileName(Configuration.TRIGRAM_FILE);
		String line=null;
		try
		{
			while((line = bufferedReader.readLine())!= null)
			{
			
				String val=" "+line.replaceAll(" ", "_")+" ";
				trigram_map.put(line,val);
				//System.out.println(key+ " "+val);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
	}
}
