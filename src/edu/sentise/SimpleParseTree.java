package edu.sentise;
import java.io.*;
import java.util.*;

import edu.sentise.preprocessing.NegationHandler;
import edu.sentise.preprocessing.POSTagger;
import edu.stanford.nlp.io.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.trees.TreeCoreAnnotations.*;
import edu.stanford.nlp.semgraph.*;
import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.util.*;
public class SimpleParseTree {

	public static void main(String[] args) {
		/* Properties props = new Properties();
	        props.setProperty("annotators","tokenize, ssplit, pos, lemma, ner, parse");
	        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	        String text = "We were offered water for the table but were not told the Voss bottles of water were $8 a piece.";
	        Annotation annotation = new Annotation(text);
	        pipeline.annotate(annotation);
	        List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
	        for (CoreMap sentence : sentences) {
	            Tree tree = sentence.get(TreeAnnotation.class);
	            List<Tree> leaves = new ArrayList<>();
	            leaves = tree.getLeaves(leaves);
	            for (Tree leave : leaves) {
	                String compare = leave.toString().toLowerCase();
	                if(compare.equals("bottles") == true) {
	                    System.out.println(tree);
	                    System.out.println("---");
	                    System.out.println(leave);
	                    System.out.println(leave.parent(tree));
	                    System.out.println((leave.parent(tree)).parent(tree));
	                }
	            }

	        }*/
		//System.out.println(POSTagger.addPOSToWord("observed"));
        String text = "The bug that I reported was actually on the change screen; I had not observed the same issue on the project page.Yep, the original fix was to fix the project page. It'd be great if we could fix both in one go ";
		 System.out.println(NegationHandler.getNegatedSentiment(text, getCoreNLP()));
	}
	public static StanfordCoreNLP getCoreNLP()
	{
		 Properties props = new Properties();
		 props.setProperty("annotators","tokenize, ssplit, pos, lemma, ner, parse");
	     StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	     System.out.println("returning core nlp.");
	     return pipeline;
	}
}
