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

public class StanfordCoreNLPLemmatizer  implements weka.core.stemmers.Stemmer  {

	private StanfordCoreNLP pipeline = null;
		
	public StanfordCoreNLPLemmatizer() {
	
		 Properties props = new Properties();
		  props.setProperty("annotators","tokenize, ssplit, pos, lemma");
	      pipeline = new StanfordCoreNLP(props);
	}
	
  	
	
	public  String stem(String word) 
    { 
      
		if(word.contains("_")) {
			
    	  String[] wordparts=word.split("_");
    	  String lastpart;
		try {
			lastpart = wordparts[wordparts.length-1];
			wordparts[wordparts.length-1]=stem(lastpart);
			return String.join("_", wordparts);
		} catch (Exception e) {
			
		}
    	  
    	  
    	  
      }
		StringBuilder lema=new StringBuilder();
        // Create an empty Annotation just with the given text 
        Annotation document = new Annotation(word); 
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
                
            } 
        } 
        
      //  System.out.println(lema);
        return lema.toString(); 
    }

	public static void main(String args[]) {
		
		StanfordCoreNLPLemmatizer lm=new StanfordCoreNLPLemmatizer();
		String str[]="I am VB_working very hard since yesterday. not_VB_Gone nn_not_fishing.".split(" ");
		for(String s:str)
			
			System.out.println(lm.stem(s));
	}
	


	@Override
	public String getRevision() {
		// TODO Auto-generated method stub
		return "$Revision: 8034 $";
	}
}
