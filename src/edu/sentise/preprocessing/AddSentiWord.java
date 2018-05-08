package edu.sentise.preprocessing;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.chainsaw.Main;

import edu.sentise.Configuration;
import edu.sentise.util.Util;

public class AddSentiWord {

	public static HashMap<String, Double> positiveSentiMap = new HashMap<>();
	public static HashMap<String, Double> negtiveSentiMap = new HashMap<>();
	public static ArrayList<String> swearWordMap;

	/**
	 * converts sentiword.net tags to stanfordparser tags
	 * 
	 * @param pos
	 * @return
	 */
	public static String getConvertedPOS(String pos) {
		if (pos.equals("n"))
			return "NN";
		else if (pos.equals("v"))
			return "VB";
		else if (pos.equals("a"))
			return "JJ";
		else
			return "";

	}

	public static void createPositiveSentiMap() {

		BufferedReader bufferedReader = Util.getBufferedreaderByFileName(Configuration.POSITIVE_SENTI_WORD_FILE);
		String line = null;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				String[] parse = line.split("#");
				positiveSentiMap.put(parse[0] + "#" + getConvertedPOS(parse[1]), Double.parseDouble(parse[2]));
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static void createNegativeSentiMap() {

		BufferedReader bufferedReader = Util.getBufferedreaderByFileName(Configuration.NEGATIVE_SENTI_WORD_FILE);
		String line = null;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				String[] parse = line.split("#");
				negtiveSentiMap.put(parse[0] + "#" + getConvertedPOS(parse[1]), Double.parseDouble(parse[2]));
				// System.out.println(line);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public static void createSwearWordMap() {
		swearWordMap = new ArrayList<String>();
		BufferedReader bufferedReader = Util.getBufferedreaderByFileName(Configuration.SWEAR_WORD_FILE);
		String line = null;
		try {
			while ((line = bufferedReader.readLine()) != null) {

				swearWordMap.add(line.trim());

			}
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public static boolean isSlangWord(String word) {

		if (swearWordMap == null)
			createSwearWordMap();

		// System.out.println(word);
		if (swearWordMap.contains(word)) {
			return true;
		}

		return false;
	}

	public static double getPositiveSentiScore(String word, String pos) {
		if (positiveSentiMap.size() == 0)
			createPositiveSentiMap();
		Double d = positiveSentiMap.get(word + "#" + getModifiedPOS(pos));
		// System.out.println(word+ " "+d +" "+pos + positiveSentiMap.size());

		if (d == null)
			return 0;

		return d;
	}

	public static double getNegativeSentiScore(String word, String pos) {

		if (negtiveSentiMap.size() == 0)
			createNegativeSentiMap();
		Double d = negtiveSentiMap.get(word + "#" + getModifiedPOS(pos));
		// System.out.println(word+ " "+d +" "+pos + negtiveSentiMap.size());
		if (d == null)
			return 0;

		return d;
	}

	public static String getModifiedPOS(String pos) {
		if (pos.startsWith("VB"))
			return "VB";
		if (pos.startsWith("JJ"))
			return "JJ";
		if (pos.startsWith("NN"))
			return "NN";
		else
			return "";

	}

	public static void main(String[] args) {
		createPositiveSentiMap();
		createNegativeSentiMap();

	}

}
