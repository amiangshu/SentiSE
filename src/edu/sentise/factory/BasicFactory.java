package edu.sentise.factory;

public class BasicFactory {

	BasePOSUtility getPOSUtility(boolean includeAllPos, boolean includeImporstantPOS, boolean isIncludeNegaive)
	{
		return new IncludeAllPOS();
	}
}
