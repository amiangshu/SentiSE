package edu.sentise.preprocessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import edu.sentise.model.SentimentData;
import edu.sentise.util.DataLists;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class NegationHandler {

	private static HashSet<String> negation_words = new HashSet<String>(Arrays.asList(DataLists.negation_words));
	private static HashSet<String> emoticon_words = new HashSet<String>(Arrays.asList(DataLists.emoticon_words));
	private static HashSet<String> stop_words = new HashSet<String>(Arrays.asList(DataLists.stop_words));

	private static StanfordCoreNLP pipeline = null;

	public static ArrayList<SentimentData> handleNegation(ArrayList<SentimentData> sentimentData) {

		int length = sentimentData.size();
		for (int i = 0; i < length; i++) {

			sentimentData.get(i).setText(handleNegation(sentimentData.get(i).getText()));
			
			if((i%100) ==0)
			{
				System.out.println("Negation processed:"+i +" of "+length);
			}
		}

		return sentimentData;
	}
	
	public  static void initCoreNLP() {
		if (pipeline == null)
			pipeline = getCoreNLP();	
	}

	
		public static StanfordCoreNLP getCoreNLP()
		{
			 Properties props = new Properties();
			 props.setProperty("annotators","tokenize, ssplit, pos, lemma, ner, parse");
		     StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		     //System.out.println("returning core nlp.");
		     return pipeline;
	}
	
	public static String handleNegation(String text) {
		
		initCoreNLP();
	//	if (isNegationAvailable(text)) {

			return getNegatedSentiment(text);

		//}
		//return text;
	}

	public static String getNegatedSentiment(String text) {
		String newText = "";
		Annotation annotation = new Annotation(text);
		pipeline.annotate(annotation);
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			Tree tree = sentence.get(TreeAnnotation.class);
			List<Tree> leaves = new ArrayList<>();
			leaves = tree.getLeaves(leaves);
			Hashtable<String, String> hashTable = new Hashtable<>();
			//Hashtable<String, String> poshashTable = new Hashtable<>();
			// boolean isNegationFound = false;
			// String negetedSentence = sentence.toString();
			for (Tree leave : leaves) {
				String compare = leave.toString().toLowerCase();
				String pos_arr[] = leave.parent(tree).toString().replace(")", "").replace("(", "").split(" ");
				String pos = "";
				if (pos_arr.length == 2) {
					pos = pos_arr[0];

				}
				//poshashTable.put(leave.label().toString(), pos);
				// System.out.println("label: "+ leave.toString().toLowerCase()+" "+
				// leave.label());
				Tree parentNode = null;

				if (!isAleadyChanged(leave, hashTable)) {
					if(isEligiblePos(pos))
					 hashTable.put(leave.label().toString(), compare);

					if (negation_words.contains(compare)) {
						// isNegationFound = true;
						parentNode = leave.parent(tree).parent(tree);
						// System.out.println(tree);
						// System.out.println("---");
						// System.out.println(leave);
						// System.out.println(leave.parent(tree));
						// System.out.println(parentNode);
						getNegatedSentence(parentNode, hashTable, compare);
						// newText+=" "+compare;
					}
				}
			}
			int i = 0;
			for (Tree leave : leaves) {
				String value = hashTable.get(leave.label().toString());
				//boolean isStopWord = stop_words.contains(value);
				if (i > 0) {
					newText += " ";
				}

				
				newText += value;
				// else
				// newText+=poshashTable.get(leave.label().toString())+"_"+value;

				i++;

			}

		}

		return newText;
	}

	private static void getNegatedSentence(Tree tree, Hashtable<String, String> hashTable, String negatedWord) {

		List<Tree> leaves = new ArrayList<>();
		boolean isNegWordfound = false;
		boolean isNounFundAfterNegation = false;
		leaves = tree.getLeaves(leaves);
		// boolean isNegationFound=false;
		for (Tree leave : leaves) {
			String compare = leave.toString().toLowerCase();
			if (compare.equals(negatedWord))
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
			
			String neg = negatedWord(compare, pos);
			// System.out.println(compare+" "+ neg);
			if(isEligiblePos(pos))
				hashTable.put(leave.label().toString(), neg);

		}

	}
	/**
	 * only verb,adverb, adjective and modals contribute to negation of a sentence.So checking eligibility of that
	 * pos 
	 * @param pos is the parts of speech
	 * @return is it eligible or not
	 */
	private static  boolean isEligiblePos(String pos) {
		if(pos.startsWith("RB") || pos.startsWith("MD") || pos.startsWith("VB") || pos.startsWith("JJ"))
			return true;
		else
			return false;
	}

	private static boolean isNegationAvailable(String text) {
		String[] splits = text.split(" ");
		int len = splits.length;
		for (int i = 0; i < len; i++) {
			if (negation_words.contains(splits[i]))
				return true;
		}
		return false;
	}

	private static String negatedWord(String word, String pos) {
		if (negation_words.contains(word))
			return word;
		else if (emoticon_words.contains(word))
			return " " + word;
		else if (pos.startsWith("VB") || pos.startsWith("RB") || pos.startsWith("JJ") || pos.startsWith("MD"))
			return "NOT_" + word;
		else
			return word;
	}

	private static boolean isAleadyChanged(Tree leaf, Hashtable<String, String> hashtable) {
		String val = hashtable.get(leaf.label().toString());
		String compare = leaf.toString().toLowerCase();
		if (val == null || val.equals(compare))
			return false;
		else
			return true;
	}

}