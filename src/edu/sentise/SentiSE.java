package edu.sentise;

import java.io.BufferedReader;

import edu.sentise.util.Constants;
import edu.sentise.util.Util;


public class SentiSE {
	
	public static void main(String[] args) {
		
		BufferedReader bufferedReader=Util.getBufferedreaderByFileName(Constants.ORACLE_FILE_NAME);
		Util.printFile(bufferedReader);
	}

}
