package edu.sentise.factory;

import edu.sentise.preprocessing.MyStopWordsHandler;

public class BasicFactory {

	public static BasePOSUtility getPOSUtility(boolean keepPosTag, boolean includeImportantPOS, boolean keepContextTag,
			MyStopWordsHandler handler) {
		if (keepPosTag && keepContextTag)
			throw new RuntimeException("Both POS and Context Tag cannot be kept at the same time");

		// System.out.println("\nprinting parameters:\n\nkeep pos Tags: "+
		// keepPosTag+"\ninclude important pos: "+includeImportantPOS+"\n");
		if (keepPosTag && includeImportantPOS)
			return new KeepImportantWithPOSTags(handler);
		if (keepContextTag && includeImportantPOS)
			return new KeepImportantWithContextTags(handler);
		else if (keepPosTag)
			return new KeepPOSTags(handler);
		else if (keepContextTag)
			return new KeepContextTags(handler);
		else if (includeImportantPOS)
			return new KeepImportantPOSWithoutTags(handler);
		else
			return new KeepUnchanged(handler);
	}
}
