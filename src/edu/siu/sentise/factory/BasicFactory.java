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

import edu.siu.sentise.preprocessing.MyStopWordsHandler;

public class BasicFactory {

	public static BasePOSUtility getPOSUtility(boolean keepPosTag, boolean includeImportantPOS, boolean keepContextTag,
			MyStopWordsHandler handler) {
		if (keepPosTag && keepContextTag)
			throw new RuntimeException("Both POS and Context Tag cannot be kept at the same time");

		// System.out.println("\nprinting parameters:\n\nkeep pos Tags: "+
		// keepPosTag+"\ninclude important pos: "+includeImportantPOS+"\n");
		if (keepPosTag && includeImportantPOS)
			return new KeepImportantWithPOSTags(handler);
		if (keepContextTag && includeImportantPOS)
			return new KeepImportantWithContextTags(handler);
		else if (keepPosTag)
			return new KeepPOSTags(handler);
		else if (keepContextTag)
			return new KeepContextTags(handler);
		else if (includeImportantPOS)
			return new KeepImportantPOSWithoutTags(handler);
		else
			return new KeepUnchanged(handler);
	}
}
