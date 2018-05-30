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

package edu.siu.sentise.factory;

import java.util.HashMap;
import java.util.Hashtable;

import edu.siu.sentise.preprocessing.MyStopWordsHandler;
import edu.siu.sentise.util.Util;

public class KeepImportantPOSWithoutTags extends BasePOSUtility{

	public KeepImportantPOSWithoutTags(MyStopWordsHandler handler) {
		super(handler);		
	}

	@Override
	public void shouldInclude(String label,String word, String tag, String context, Hashtable<String, String> myMap) {
		if(Util.isEligiblePos(tag))
			if(!this.isStopWord(word))
			   myMap.put(label,word);
		
	}
}
