package edu.sentise.preprocessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.sun.org.apache.regexp.internal.recompile;

import edu.sentise.SimpleParseTree;
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
	private static HashSet<String> stop_words=new HashSet<String>(Arrays.asList(DataLists.stop_words));

	public static ArrayList<SentimentData> handleNegation(ArrayList<SentimentData> sentimentData) {
		StanfordCoreNLP pipeline = SimpleParseTree.getCoreNLP();
		int length = sentimentData.size();
		for (int i = 0; i < length; i++) {
			System.out.println(i);
			if(isNegationAvailable(sentimentData.get(i).getText()))
			{
				//System.out.println("bef: "+sentimentData.get(i).getText());
				String negst=getNegatedSentiment(sentimentData.get(i).getText(), pipeline);
				sentimentData.get(i).setText(negst);
				//System.out.println("af: "+negst);
			}
		}
		return sentimentData;
	}

	public static String getNegatedSentiment(String text, StanfordCoreNLP pipeline) {
		String newText = "";
		Annotation annotation = new Annotation(text);
		pipeline.annotate(annotation);
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			Tree tree = sentence.get(TreeAnnotation.class);
			List<Tree> leaves = new ArrayList<>();
			leaves = tree.getLeaves(leaves);
			boolean isNegationFound = false;
			String negetedSentence = sentence.toString();
			for (Tree leave : leaves) {
				String compare = leave.toString().toLowerCase();
				if (isNegationFound) {
					Tree parentNode = leave.parent(tree).parent(tree);
				  
					newText+= getNegatedSentence(parentNode, compare);
					/*System.out.println(tree);
					System.out.println("---");
					System.out.println(leave);
					System.out.println(leave.parent(tree));
					System.out.println((leave.parent(tree)).parent(tree));*/
					isNegationFound=false;
				}
				else
				{
					if(negation_words.contains(compare))
						isNegationFound = true;
					else
						newText+=" "+compare;
				}
				
			}
			//newText += negetedSentence;

		}

		return newText;
	}

	private static String getNegatedSentence(Tree tree, String negetaed_word) {
		String sentence = "";
		List<Tree> leaves = new ArrayList<>();
		leaves = tree.getLeaves(leaves);
		// boolean isNegationFound=false;
		for (Tree leave : leaves) {
			String compare = leave.toString().toLowerCase();
			String pos_arr[]=leave.parent(tree).toString().replace(")","").replace("(", "").split(" ");
			String pos="";
			if(pos_arr.length==2)
			{
				pos=pos_arr[0];
				//System.out.println(pos_arr[0]+" "+pos_arr[1]);
			}
			sentence += negatedWord(compare,pos);

		}
		return sentence;
	}

	private static boolean isNegationAvailable(String text)
	{
		String[] splits=text.split(" ");
		int len=splits.length;
		for(int i= 0;i<len;i++)
		{
			if(negation_words.contains(splits[i]))
				return true;
		}
		return false;
	}
	private static String negatedWord(String word,String pos) {
		if (negation_words.contains(word))
			return " " + word;
		else if (stop_words.contains(word))
			return " " + word;
		else if (emoticon_words.contains(word))
			return " " + word;
		else if(pos.startsWith("VB") || pos.startsWith("RB") || pos.startsWith("JJ"))
			return " NOT_" + word;
		else 
			return " " + word;
	}

}
