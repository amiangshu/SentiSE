package edu.sentise.preprocessing;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.process.DocumentPreprocessor;

public class Parser {

	
	public static  void sentenceTokenizer()
	{
		String paragraph = "My 1st sentence. “Does it work for questions?” My third sentence.";
		Reader reader = new StringReader(paragraph);
		DocumentPreprocessor dp = new DocumentPreprocessor(reader);
		List<String> sentenceList = new ArrayList<String>();

		for (List<HasWord> sentence : dp) {
		   // SentenceUtils not Sentence
		   String sentenceString = SentenceUtils.listToString(sentence);
		   sentenceList.add(sentenceString);
		}

		for (String sentence : sentenceList) {
		   System.out.println(sentence);
		}

	}
}
