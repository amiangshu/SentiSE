package edu.sentise.sentiwordparser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.poi.hslf.examples.CreateHyperlink;

import edu.sentise.util.Configuration;
import edu.sentise.util.Util;

public class SentiWordNetDemoCode {

	private Map<String, Double> dictionary;

	public SentiWordNetDemoCode(String pathToSWN) throws IOException {
		// This is our main dictionary representation
		dictionary = new HashMap<String, Double>();

		// From String to list of doubles.
		HashMap<String, HashMap<Integer, Double>> tempDictionary = new HashMap<String, HashMap<Integer, Double>>();

		BufferedReader csv = null;
		try {
			csv = new BufferedReader(new FileReader(pathToSWN));
			int lineNumber = 0;

			String line;
			while ((line = csv.readLine()) != null) {
				lineNumber++;

				// If it's a comment, skip this line.
				if (!line.trim().startsWith("#")) {
					// We use tab separation
					String[] data = line.split("\t");
					String wordTypeMarker = data[0];

					// Example line:
					// POS ID PosS NegS SynsetTerm#sensenumber Desc
					// a 00009618 0.5 0.25 spartan#4 austere#3 ascetical#2
					// ascetic#2 practicing great self-denial;...etc

					// Is it a valid line? Otherwise, through exception.
					if (data.length != 6) {
						throw new IllegalArgumentException(
								"Incorrect tabulation format in file, line: "
										+ lineNumber);
					}

					// Calculate synset score as score = PosS - NegS
					Double synsetScore = Double.parseDouble(data[2])
							- Double.parseDouble(data[3]);

					// Get all Synset terms
					String[] synTermsSplit = data[4].split(" ");

					// Go through all terms of current synset.
					for (String synTermSplit : synTermsSplit) {
						// Get synterm and synterm rank
						String[] synTermAndRank = synTermSplit.split("#");
						String synTerm = synTermAndRank[0] + "#"
								+ wordTypeMarker;

						int synTermRank = Integer.parseInt(synTermAndRank[1]);
						// What we get here is a map of the type:
						// term -> {score of synset#1, score of synset#2...}

						// Add map to term if it doesn't have one
						if (!tempDictionary.containsKey(synTerm)) {
							tempDictionary.put(synTerm,
									new HashMap<Integer, Double>());
						}

						// Add synset link to synterm
						tempDictionary.get(synTerm).put(synTermRank,
								synsetScore);
					}
				}
			}

			// Go through all the terms.
			for (Map.Entry<String, HashMap<Integer, Double>> entry : tempDictionary
					.entrySet()) {
				String word = entry.getKey();
				Map<Integer, Double> synSetScoreMap = entry.getValue();

				// Calculate weighted average. Weigh the synsets according to
				// their rank.
				// Score= 1/2*first + 1/3*second + 1/4*third ..... etc.
				// Sum = 1/1 + 1/2 + 1/3 ...
				double score = 0.0;
				double sum = 0.0;
				for (Map.Entry<Integer, Double> setScore : synSetScoreMap
						.entrySet()) {
					score += setScore.getValue() / (double) setScore.getKey();
					sum += 1.0 / (double) setScore.getKey();
				}
				score /= sum;

				dictionary.put(word, score);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (csv != null) {
				csv.close();
			}
		}
	}

	public double extract(String word, String pos) {
		return dictionary.get(word + "#" + pos);
	}
	
	public static void main(String [] args) throws IOException {
		/*if(args.length<1) {
			System.err.println("Usage: java SentiWordNetDemoCode <pathToSentiWordNetFile>");
			return;
		}*/
		
		String pathToSWN = "models/SentiWordNet_3.0.0_20130122.txt";
		SentiWordNetDemoCode sentiwordnet = new SentiWordNetDemoCode(pathToSWN);
		
		System.out.println("good#a "+sentiwordnet.extract("good", "a"));
		System.out.println("bad#a "+sentiwordnet.extract("bad", "a"));
		System.out.println("blue#a "+sentiwordnet.extract("blue", "a"));
		System.out.println("blue#n "+sentiwordnet.extract("blue", "n"));
		sentiwordnet.getPositiveSentiWords(createHashSetByFileName("models/positive_words_short.txt"));
		sentiwordnet.getNegativeSentiWords(createHashSetByFileName("models/negative_words_short.txt"));
	}
	private  void getPositiveSentiWords(HashSet<String> words)
	{
	
		try
		{
		String fileName=Configuration.POSITIVE_SENTI_WORD_FILE;
		File file= new File(fileName);
		file.createNewFile();
		BufferedWriter bufferedWriter=edu.sentise.util.Util.getBufferedWriterByFileName(fileName);
		for(String key: dictionary.keySet())
		{
			Double d=dictionary.get(key);
			if(d>0)
			{
				String s=key+"#"+d+"\n";
				String[] splits=s.split("#");
				if(words.contains(splits[0]))	
				  bufferedWriter.write(s);
			}
		}
		bufferedWriter.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("positive sentiment file writing done.........");
	}
	private static HashSet<String> createHashSetByFileName(String filename)
	{
		HashSet<String> hashset=new HashSet<>();
		BufferedReader bufferedReader=Util.getBufferedreaderByFileName(filename);
		String line=null;
		try
		{
			while((line = bufferedReader.readLine())!= null)
			{
				//String[] parse=line.split("#");
				hashset.add(line);
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		return hashset;
		
	}
	private  void getNegativeSentiWords(HashSet<String> words)
	{
	
		try
		{
		String fileName=Configuration.NEGATIVE_SENTI_WORD_FILE;

		File file= new File(fileName);
		file.createNewFile();
		BufferedWriter bufferedWriter=edu.sentise.util.Util.getBufferedWriterByFileName(fileName);
		for(String key: dictionary.keySet())
		{
			Double d=dictionary.get(key);
			if(d<0)
			{
				String s=key+"#"+d+"\n";
				String[] splits=s.split("#");
				if(words.contains(splits[0]))	
				  bufferedWriter.write(s);
			}
		}
		bufferedWriter.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("negative sentiment file writing done.........");
	}
}