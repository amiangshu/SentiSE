package edu.sentise.factory;

import java.util.HashMap;
import java.util.Hashtable;

import edu.sentise.preprocessing.MyStopWordsHandler;

public class KeepUnchanged extends BasePOSUtility {

	public KeepUnchanged(MyStopWordsHandler handler) {
		super(handler);
	}

	@Override
	public void shouldInclude(String label, String word, String tag, String context, Hashtable<String, String> myMap) {
		myMap.put(label, word);

	}
}
