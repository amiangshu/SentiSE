package edu.sentise.preprocessing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;



public class ContractionLoader {

	public static HashMap<String, String> getContractionData(BufferedReader bufferedReader) {
		HashMap<String, String> mHashMap = new HashMap<>();
		try {

			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				//String[] splits=line.split(" ",2);
				int index=line.indexOf('	');
				String key=line.substring(0,index).trim();
				String value=line.substring(index+1,line.length()).trim();
				//System.out.println(index+" "+ key+": "+ value+" ::"+ line);
				mHashMap.put(key,value);
			}

			// Always close files.
			//bufferedReader.close();
		}

		catch (IOException ex) {

		}
		return mHashMap;

	}
}
