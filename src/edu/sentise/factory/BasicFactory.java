package edu.sentise.factory;

public  class BasicFactory {

	public static BasePOSUtility getPOSUtility(boolean keepPosTag, boolean includeImportantPOS) {
		
		System.out.println("\nprinting parameters:\n\nkeep pos Tags: "+ keepPosTag+"\ninclude important pos: "+includeImportantPOS+"\n");
		if (keepPosTag && includeImportantPOS)
			return new KeepImportantWithPOSTags();
		else if (keepPosTag)
			return new KeepPOSTags();
		else if (includeImportantPOS)
			return new KeepImportantPOSWithoutTags();
		else
			return new KeepUnchanged();
	}
}
