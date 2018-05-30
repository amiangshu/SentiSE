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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import edu.siu.sentise.factory.BasePOSUtility;
import edu.siu.sentise.model.SentimentData;
import edu.siu.sentise.util.DataLists;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class POSTagProcessor implements TextPreprocessor {

	private HashSet<String> negation_words = new HashSet<String>(Arrays.asList(DataLists.negation_words));
	private HashSet<String> emoticon_words = new HashSet<String>(Arrays.asList(DataLists.emoticon_words));

	private StanfordCoreNLP pipeline = null;
	private BasePOSUtility basePOSUtility;
	private boolean handleNegation = true;
	private double POSITIVE_THRESHOLD = 0.5;
	private double NEGATIVE_THRESHOLD = -0.75;
	private static boolean markSlangWords;

	public POSTagProcessor(BasePOSUtility bUtility, boolean shouldNegate, int addSentiScore, boolean markSlangWords) {

		handleNegation = shouldNegate;
		basePOSUtility = bUtility;
		addSentiScoreType = addSentiScore;
		this.markSlangWords = markSlangWords;
	}

	private int addSentiScoreType = 0;

	public void setHandleSentiScore(int hSentiScore) {
		addSentiScoreType = hSentiScore;
	}

	public ArrayList<SentimentData> apply(ArrayList<SentimentData> sentimentData) {

		int length = sentimentData.size();
		for (int i = 0; i < length; i++) {

			// System.out.println(sentimentData.get(i).getText());
			sentimentData.get(i).setText(preprocessPOStags(sentimentData.get(i).getText()));
			// System.out.println(sentimentData.get(i).getText());
			if ((i % 100) == 0) {
				System.out.println("POS tag processsed processed:" + i + " of " + length);
			}
		}

		return sentimentData;
	}

	public void initCoreNLP() {
		if (pipeline == null)
			pipeline = getCoreNLP();
	}

	public StanfordCoreNLP getCoreNLP() {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		// System.out.println("returning core nlp.");
		return pipeline;
	}

	public String preprocessPOStags(String text) {

		initCoreNLP();
		// if (isNegationAvailable(text)) {

		return getPosProccesedText(text);

		// }
		// return text;
	}

	public String getPosProccesedText(String text) {
		String newText = "";
		int countPositive = 0;
		int countNegative = 0;
		int countExtremeNegative = 0;
		int countExtremePositive = 0;
		int countSwearWord = 0;

		Annotation annotation = new Annotation(text);
		pipeline.annotate(annotation);
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			Tree tree = sentence.get(TreeAnnotation.class);

			List<Tree> leaves = new ArrayList<>();
			leaves = tree.getLeaves(leaves);
			Hashtable<String, String> hashTable = new Hashtable<>();
			for (Tree leaf : leaves) {
				String word = leaf.toString().toLowerCase();
				String pos_arr[] = leaf.parent(tree).toString().replace(")", "").replace("(", "").split(" ");
				String pos = "";
				if (pos_arr.length == 2) {
					pos = pos_arr[0];

				}
				Tree parentNode = null;

				if (!isAleadyChanged(leaf, hashTable)) {

					String context = leaf.parent(tree).parent(tree).value();
					double wordPosScore = AddSentiWord.getPositiveSentiScore(word, pos);

					if (wordPosScore != 0 && this.addSentiScoreType > 0) {
						if (this.addSentiScoreType == 4 && wordPosScore > POSITIVE_THRESHOLD)
							countExtremePositive++;
						else
							countPositive++;

					}

					double wordNegScore = AddSentiWord.getNegativeSentiScore(word, pos);

					if (wordNegScore != 0 && this.addSentiScoreType > 0) {
						if (this.addSentiScoreType == 4 && wordNegScore > NEGATIVE_THRESHOLD)
							countExtremeNegative++;
						else
							countNegative++;

					}
					if (this.markSlangWords && AddSentiWord.isSlangWord(word))
						countSwearWord++;

					if (!isPunctuation(word))
						basePOSUtility.shouldInclude(leaf.label().toString(), word, pos, context, hashTable);
					else
						hashTable.put(leaf.label().toString(), word);
				}

				if (handleNegation) {
					if (negation_words.contains(word)) {

						parentNode = leaf.parent(tree).parent(tree);
						getNegatedSentence(parentNode, hashTable, word, basePOSUtility);

					}
				}

			}
			int i = 0;
			for (Tree leaf : leaves) {

				String value = hashTable.get(leaf.label().toString());

				if (value != null) {
					// boolean isStopWord = stop_words.contains(value);
					if (i > 0) {
						newText += " ";
					}

					newText += value;
					// else
					// newText+=poshashTable.get(leave.label().toString())+"_"+value;

					i++;
				}

			}
			// System.out.println(positiveSentiScore+" "+negativeSentiScore);
			for (int j = 0; j < countExtremeNegative; j++)
				newText += " includes_extremenegative ";

			for (int j = 0; j < countExtremePositive; j++)
				newText += " includes_extremepositive ";

			for (int j = 0; j < countNegative; j++)
				newText += " includes_negative ";
			for (int j = 0; j < countPositive; j++)
				newText += " includes_positive ";

			for (int j = 0; j < countSwearWord; j++)
				newText += " includes_slangwords ";

		}

		return newText;
	}

	private void getNegatedSentence(Tree tree, Hashtable<String, String> hashTable, String negatedWord,
			BasePOSUtility basePOSUtility) {

		List<Tree> leaves = new ArrayList<>();
		boolean isNegWordfound = false;
		boolean isNounFundAfterNegation = false;
		leaves = tree.getLeaves(leaves);
		// boolean isNegationFound=false;
		for (Tree leave : leaves) {
			String word = leave.toString().toLowerCase();
			if (word.equals(negatedWord))
				isNegWordfound = true;
			String pos_arr[] = leave.parent(tree).toString().replace(")", "").replace("(", "").split(" ");
			String pos = "";
			if (pos_arr.length == 2) {
				pos = pos_arr[0];

			}
			if (isNegWordfound && (pos.startsWith("NN") || pos.startsWith("PR")))
				isNounFundAfterNegation = true;
			if (isNounFundAfterNegation)
				return;
			// System.out.println(word+" "+ pos);
			String neg = negatedWord(word, pos);
			double d = AddSentiWord.getPositiveSentiScore(word, pos);
			// handlePositiveSentimentStatusByScore(d);
			d = AddSentiWord.getNegativeSentiScore(word, pos);
			// handleNegativeSentimentStatusByScore(d);

			basePOSUtility.shouldInclude(leave.label().toString(), neg, pos, tree.value(), hashTable);

		}

	}

	private String negatedWord(String word, String pos) {
		if (negation_words.contains(word))
			return word;
		else if (emoticon_words.contains(word))
			return " " + word;
		else if (pos.startsWith("VB") || pos.startsWith("RB") || pos.startsWith("JJ") || pos.startsWith("MD"))
			return "not_" + word;
		else
			return word;
	}

	private boolean isAleadyChanged(Tree leaf, Hashtable<String, String> hashtable) {
		String val = hashtable.get(leaf.label().toString());
		String compare = leaf.toString().toLowerCase();
		// System.out.println(leaf.label().toString()+" "+val+" gg: "+compare);
		if (val == null || val.equals(compare))
			return false;
		else
			return true;
	}

	private boolean isPunctuation(String str) {
		if (Pattern.matches("\\p{Punct}", str)) {
			return true;
		}
		return false;
	}

}