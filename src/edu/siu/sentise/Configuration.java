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

public class Configuration {

	public static final String CONTRACTION_TEXT_FILE_NAME = "models/Contractions.txt";
	public static final String EMOTICONS_FILE_NAME = "models/EmoticonLookupTable.txt";
	public static final String EMOTICONS_CATEGORIZED = "models/EmoticonLookupCategorized.txt";
	public static final String STOPWORDS_FILE_NAME = "models/StopWords.txt";
	public static final String ORACLE_FILE_NAME = "models/combined-with-source-randomized.xlsx";
	public static final String ARFF_ORACLE_FILE_NAME = "models/myoracle.arff";
	public static final String ARFF_ORACLE_FILE_NAME_TEST = "models/myoracle_test.arff";
	public static final String DELIMITERS = " \\r\\t\\n.,;:\\'\\\"()?!-><#$\\\\%&*+/@^=[]{}`~0123456789\\'|";
	public static final String MODEL_FILE_NAME = "models/rf.combined.xlsx.model";
	public static final String NEG_DATA_FILE_NAME = "models/neg_data.txt";
	public static final String POSITIVE_SENTI_WORD_FILE = "models/positive.txt";
	public static final String NEGATIVE_SENTI_WORD_FILE = "models/negative.txt";
	public static final String SWEAR_WORD_FILE = "models/swear_words.txt";
	public static final String BIGRAM_FILE = "models/bigram.txt";
	public static final String TRIGRAM_FILE = "models/trigram.txt";
	public static final String ACRONYM_WORD_FILE = "models/short_word_list.txt";
	public static final String KEYWORD_LIST_FILE = "models/keyword_list.txt";
	public static final String OUTPUT_DIRECTORY = "output/";
	public static final String ARFF_DIRECTORY = "arff/";
	public static final String EMPTY_FILE = "models/EmptyStopWords.txt";

}
