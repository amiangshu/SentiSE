package edu.sentise.preprocessing;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;

public class Parser {

	
	public static  ArrayList<String> sentenceTokenizer(String paragraph)
	{
		//String paragraph = "My 1st sentence. “Does it work for questions?” My third sentence.";
		Reader reader = new StringReader(paragraph);
		DocumentPreprocessor dp = new DocumentPreprocessor(reader);
		ArrayList<String> sentenceList = new ArrayList<String>();

		for (List<HasWord> sentence : dp) {
		   // SentenceUtils not Sentence
		   String sentenceString = SentenceUtils.listToString(sentence);
		   sentenceList.add(sentenceString);
		}

		/*for (String sentence : sentenceList) {
		   System.out.println(sentence);
		}*/
		return sentenceList;
	}
	public static void wordTokenizer()
	{
		/*String sent2 = "This is another sentence.";
	    TokenizerFactory<CoreLabel> tokenizerFactory =
	        PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
	    Tokenizer<CoreLabel> tok =
	        tokenizerFactory.getTokenizer(new StringReader(sent2));
	    List<CoreLabel> rawWords = tok.tokenize();
	    for (CoreLabel word : rawWords) {
			   System.out.println(word.word());
	}*/
	}
	
}
