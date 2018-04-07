package edu.sentise.factory;

import java.util.HashMap;
import java.util.Hashtable;

import edu.sentise.util.Util;

public class KeepImportantWithPOSTags extends BasePOSUtility{

	@Override
	public void shouldInclude(String label,String word, String tag, String context, Hashtable<String, String> myMap) {
		if(Util.isEligiblePos(tag))
			myMap.put(label,tag+"_"+word);
		
	}
}
