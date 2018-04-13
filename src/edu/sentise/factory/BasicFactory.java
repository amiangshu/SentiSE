package edu.sentise.factory;

public  class BasicFactory {

	public static BasePOSUtility getPOSUtility(boolean keepPosTag, boolean includeImportantPOS, boolean keepContextTag) {
		if(keepPosTag && keepContextTag)
			throw new RuntimeException("Both POS and Context Tag cannot be kept at the same time");
		
		//System.out.println("\nprinting parameters:\n\nkeep pos Tags: "+ keepPosTag+"\ninclude important pos: "+includeImportantPOS+"\n");
		if (keepPosTag && includeImportantPOS)
			return new KeepImportantWithPOSTags();
		if(keepContextTag && includeImportantPOS)
			return new KeepImportantWithContextTags();
		else if (keepPosTag)
			return new KeepPOSTags();
		else if(keepContextTag)
			return new KeepContextTags();
		else if (includeImportantPOS)
			return new KeepImportantPOSWithoutTags();
		else
			return new KeepUnchanged();
	}
}
