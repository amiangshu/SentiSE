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

import weka.classifiers.misc.InputMappedClassifier;
import weka.core.Instance;

public class CustomImputMappedClassifer extends InputMappedClassifier {

	public String getConvertedInstance(Instance instance)
	{
		try{
			String str="";
		Instance ins=constructMappedInstance(instance);
		int len= ins.numAttributes();
		for(int i=0;i<len;i++)
		{
			str+=ins.stringValue(i)+ ins.toString(i);
		}
		return str;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
