package edu.sentise.factory;

import java.util.HashMap;
import java.util.Hashtable;

public interface WordChooser {

	public void shouldInclude(String label,String word, String tag, String context, Hashtable<String,String> myMap);
}
