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
		String[] testTexts= {"the histograms themselves do not use multipoint storage. the input for them often would be, but in this case it is not.",
				"whoops, this is not new code, but docs are always good!",
						"could not  you just remember the first sequence number your rild connection received?",
						"do not remove the entire statement, seekgroup() has side effects.", 
						"ctr is not something that we would want to call out in ui as users may not understand what it is about.",
						"this does not  make too much sense to me, as this file lives in the same directory (hence double quotes)."

						};
       // String text = "the histograms themselves do not use multipoint storage. the input for them often would be, but in this case it is not.";
        StanfordCoreNLP pipeline= getCoreNLP();
        for( String text: testTexts)
        	System.out.println(NegationHandler.getNegatedSentiment(text));
	}
	public static StanfordCoreNLP getCoreNLP()
	{
		 Properties props = new Properties();
		 props.setProperty("annotators","tokenize, ssplit, pos, lemma, ner, parse");
	     StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	     //System.out.println("returning core nlp.");
	     return pipeline;
	}
}
