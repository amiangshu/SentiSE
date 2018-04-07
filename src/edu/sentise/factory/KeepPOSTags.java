package edu.sentise.factory;

import java.util.HashMap;
import java.util.Hashtable;

public class KeepPOSTags extends BasePOSUtility{

	@Override
	public void shouldInclude(String label,String word, String pos, Hashtable<String, String> myMap) {
		myMap.put(label,pos+"_"+word);
		
	}
}
