package edu.sentise.preprocessing;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.sentise.model.SentimentData;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class Lemmatizer {

	private static StanfordCoreNLP pipeline = null;
	public static void main(String[] args) {
		 initCoreNLP();
	     System.out.println(lemmatize("that crying boy?"));
	      
	      
	}
	private static void initCoreNLP()
	{
		  Properties props = new Properties();
		  props.setProperty("annotators","tokenize, ssplit, pos, lemma, ner, parse");
	      pipeline = new StanfordCoreNLP(props);
	}
   public static ArrayList<SentimentData> lematizeSentimentData(ArrayList<SentimentData> sentimentData) {
      
		initCoreNLP();
		int length = sentimentData.size();
		for (int i = 0; i < length; i++) {

			System.out.println(sentimentData.get(i).getText());
			sentimentData.get(i).setText(lemmatize(sentimentData.get(i).getText()));
			System.out.println(sentimentData.get(i).getText());
			if((i%100) ==0)
			{
				System.out.println("lematization processed:"+i +" of "+length);
			}
		}

		return sentimentData;
	}
	
	
	public static String lemmatize(String documentText) 
    { 
       StringBuilder lema=new StringBuilder();
        // Create an empty Annotation just with the given text 
        Annotation document = new Annotation(documentText); 
        // run all Annotators on this text 
        pipeline.annotate(document); 
        // Iterate over all of the sentences found 
        List<CoreMap> sentences = document.get(SentencesAnnotation.class); 
        for(CoreMap sentence: sentences) { 
            // Iterate over all tokens in a sentence 
            for (CoreLabel token: sentence.get(TokensAnnotation.class)) { 
                // Retrieve and add the lemma for each word into the 
                // list of lemmas 
                lema.append((token.get(LemmaAnnotation.class))); 
                lema.append(' ');
            } 
        } 
        return lema.toString(); 
    }
}
