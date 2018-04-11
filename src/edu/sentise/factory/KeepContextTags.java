package edu.sentise.factory;

import java.util.HashMap;
import java.util.Hashtable;

public class KeepContextTags extends BasePOSUtility{

	@Override
	public void shouldInclude(String label,String word, String tag, String context, Hashtable<String, String> myMap) {
		myMap.put(label,context+"|"+word);
		
	}
}
