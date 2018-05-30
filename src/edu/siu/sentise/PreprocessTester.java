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

package edu.siu.sentise;

import java.util.ArrayList;

import edu.siu.sentise.factory.BasicFactory;
import edu.siu.sentise.model.SentimentData;
import edu.siu.sentise.preprocessing.*;

public class PreprocessTester {

	public static void main(String[] args) {
		
		String text="First of all I do not think you hate hate hate horrible suck fuck genius love awesome need all these.";
		SentimentData d=new SentimentData(text,0);
		ArrayList<SentimentData> dt=new ArrayList<SentimentData>();
		dt.add(d);
		
		
		TextPreprocessor pipeline =new POSTagProcessor(BasicFactory.getPOSUtility(false, false, false, 
				new MyStopWordsHandler(Configuration.STOPWORDS_FILE_NAME)), true, 2, true);
		
		dt=pipeline.apply(dt);
		System.out.println(dt.get(0).getText());
		

	}

}
