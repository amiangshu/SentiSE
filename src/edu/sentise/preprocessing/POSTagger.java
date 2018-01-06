package edu.sentise.preprocessing;

import java.io.IOException;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class POSTagger {

	private static String tagPartsOfSpeech(MaxentTagger tagger, String text)
	{
		String tagged = tagger.tagString(text);
		// Output the result
		return tagged;
	}
	public static void addPOSToText()
	{
		//try
		//{
		MaxentTagger tagger = new MaxentTagger("src/taggers/bidirectional-distsim-wsj-0-18.tagger");
		String sample = "This is a sample text";
		String tagged = tagPartsOfSpeech(tagger,sample);
		System.out.println(tagged);
		
		 
		
		/*}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}*/
	}
	private static String getPOSToWord(MaxentTagger tagger, String word)
	{
		String tagged = tagger.tagString(word);
		// Output the result
		return tagged;
	}
	public static String addPOSToWord(String word)
	{
		MaxentTagger tagger = new MaxentTagger("src/taggers/bidirectional-distsim-wsj-0-18.tagger");
		//String sample = "This is a sample text";
		String tagged = getPOSToWord(tagger,word);
		return tagged;
	}
	
}
