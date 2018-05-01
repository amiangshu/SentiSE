package edu.sentise.factory;

import java.util.HashMap;
import java.util.Hashtable;

import edu.sentise.preprocessing.MyStopWordsHandler;
import edu.sentise.util.Util;

public class KeepImportantWithContextTags extends BasePOSUtility {

	public KeepImportantWithContextTags(MyStopWordsHandler handler) {
		super(handler);

	}

	@Override
	public void shouldInclude(String label, String word, String tag, String context, Hashtable<String, String> myMap) {
		if (Util.isEligiblePos(tag))
			if (!this.isStopWord(word))
				myMap.put(label, context + "_" + word);

	}
}
