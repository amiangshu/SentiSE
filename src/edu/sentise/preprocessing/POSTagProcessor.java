package edu.sentise.preprocessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import edu.sentise.factory.BasePOSUtility;
import edu.sentise.model.SentimentData;
import edu.sentise.util.DataLists;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class POSTagProcessor implements TextPreprocessor {

	private static HashSet<String> negation_words = new HashSet<String>(Arrays.asList(DataLists.negation_words));
	private static HashSet<String> emoticon_words = new HashSet<String>(Arrays.asList(DataLists.emoticon_words));
	
	private static StanfordCoreNLP pipeline = null;
	private static BasePOSUtility basePOSUtility;
	private static boolean handleNegation = true;
	private static double POSITIVE_THRESHOLD=0.5;
	private static double NEGATIVE_THRESHOLD=-0.75;

	public POSTagProcessor(BasePOSUtility bUtility, boolean shouldNegate,int addSentiScore) {

		handleNegation = shouldNegate;
		basePOSUtility = bUtility;
		addSentiScoreType=addSentiScore;
	}

	private static int addSentiScoreType = 0;

	public static void setHandleSentiScore(int hSentiScore) {
		addSentiScoreType = hSentiScore;
	}

	public ArrayList<SentimentData> apply(ArrayList<SentimentData> sentimentData) {

		int length = sentimentData.size();
		for (int i = 0; i < length; i++) {

			//System.out.println(sentimentData.get(i).getText());
			sentimentData.get(i).setText(preprocessPOStags(sentimentData.get(i).getText()));
		//	System.out.println(sentimentData.get(i).getText());
			if ((i % 100) == 0) {
				System.out.println("POS tag processsed processed:" + i + " of " + length);
			}
		}

		return sentimentData;
	}

	public static void initCoreNLP() {
		if (pipeline == null)
			pipeline = getCoreNLP();
	}

	public static StanfordCoreNLP getCoreNLP() {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		// System.out.println("returning core nlp.");
		return pipeline;
	}

	public static String preprocessPOStags(String text) {

		initCoreNLP();
		// if (isNegationAvailable(text)) {

		return getPosProccesedText(text);

		// }
		// return text;
	}

	static boolean positive;
	static boolean negative ;
	static boolean extreme_positive;
	static boolean extreme_negative ;

	public static String getPosProccesedText(String text) {
		String newText = "";
		positive = false;
		negative = false;
		extreme_negative=false;
		extreme_positive=false;
		Annotation annotation = new Annotation(text);
		pipeline.annotate(annotation);
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			Tree tree = sentence.get(TreeAnnotation.class);

			List<Tree> leaves = new ArrayList<>();
			leaves = tree.getLeaves(leaves);
			Hashtable<String, String> hashTable = new Hashtable<>();
			// Hashtable<String, String> poshashTable = new Hashtable<>();
			// boolean isNegationFound = false;
			// String negetedSentence = sentence.toString();
			for (Tree leaf : leaves) {
				String word = leaf.toString().toLowerCase();
				String pos_arr[] = leaf.parent(tree).toString().replace(")", "").replace("(", "").split(" ");
				String pos = "";
				if (pos_arr.length == 2) {
					pos = pos_arr[0];
					

				}
				// poshashTable.put(leave.label().toString(), pos);
				// System.out.println("label: "+ leave.toString().toLowerCase()+" "+
				// leave.label());
				Tree parentNode = null;

				if (!isAleadyChanged(leaf, hashTable)) {

					String context = leaf.parent(tree).parent(tree).value();
					double d = AddSentiWord.getPositiveSentiScore(word, pos);
					handlePositiveSentimentStatusByScore(d);
					d= AddSentiWord.getNegativeSentiScore(word, pos);
					handleNegativeSentimentStatusByScore(d);
					basePOSUtility.shouldInclude(leaf.label().toString(), word, pos, context, hashTable);
				}

				if (handleNegation) {
					if (negation_words.contains(word)) {
						// isNegationFound = true;
						parentNode = leaf.parent(tree).parent(tree);
						getNegatedSentence(parentNode, hashTable, word, basePOSUtility);
						// newText+=" "+compare;
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
			if (addSentiScoreType == 4) {
				if(extreme_negative)
					newText +=  " includes_extremenegative ";

				if(extreme_positive)
					newText +=  " includes_extremepositive ";
			}
			if(addSentiScoreType >=2)
			{
				if(negative)
					newText +=  " includes_negative ";

				if(positive)
					newText +=  " includes_positive ";
			}

		}

		return newText;
	}
	private static void handlePositiveSentimentStatusByScore(double score)
	{
		if(score== 0)
			return;
		if(addSentiScoreType == 4 && score>=POSITIVE_THRESHOLD)
		{
			extreme_positive=true;
			return;
		}
		positive=true;
		
		
	}
	private static void handleNegativeSentimentStatusByScore(double score)
	{
		if(score== 0)
			return;
		if(addSentiScoreType == 4 && score<=NEGATIVE_THRESHOLD)
		{
			extreme_negative=true;
			return;
		}
		negative=true;
		
		
	}

	private static void getNegatedSentence(Tree tree, Hashtable<String, String> hashTable, String negatedWord,
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
			handlePositiveSentimentStatusByScore(d);
			d= AddSentiWord.getNegativeSentiScore(word, pos);
			handleNegativeSentimentStatusByScore(d);
			basePOSUtility.shouldInclude(leave.label().toString(), neg, pos, tree.value(), hashTable);

		}

	}

	private static String negatedWord(String word, String pos) {
		if (negation_words.contains(word))
			return word;
		else if (emoticon_words.contains(word))
			return " " + word;
		else if (pos.startsWith("VB") || pos.startsWith("RB") || pos.startsWith("JJ") || pos.startsWith("MD"))
			return "not_" + word;
		else
			return word;
	}

	private static boolean isAleadyChanged(Tree leaf, Hashtable<String, String> hashtable) {
		String val = hashtable.get(leaf.label().toString());
		String compare = leaf.toString().toLowerCase();
		// System.out.println(leaf.label().toString()+" "+val+" gg: "+compare);
		if (val == null || val.equals(compare))
			return false;
		else
			return true;
	}

}