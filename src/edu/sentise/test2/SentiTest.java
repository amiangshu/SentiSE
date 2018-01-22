package edu.sentise.test2;

import java.util.ArrayList;
import java.util.HashMap;
import edu.sentise.preprocessing.ContractionLoader;
import edu.sentise.preprocessing.EmoticonLoader;
import edu.sentise.preprocessing.MyStopWordsHandler;
import edu.sentise.preprocessing.NegationHandler;
import edu.sentise.preprocessing.URLRemover;
import edu.sentise.test.ARFFTestGenerator;
import edu.sentise.util.Constants;
import weka.classifiers.Classifier;

import weka.core.Instances;
import weka.core.stemmers.SnowballStemmer;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class SentiTest {

	private static String[] sentences = { "I'm not sure I entirely understand what you are saying. "
			+ "However, looking at file_linux_test.go I'm pretty sure an interface type would be easier for people to use.",
			"I think it always returns it as 0.",
			"If the steal does not commit, there's no need to clean up _p_'s runq. If it doesn't commit,"
					+ " runqsteal just won't update runqtail, so it won't matter what's in _p_.runq.",
			"Please change the subject: s:internal/syscall/windows:internal/syscall/windows/registry:",
			"I don't think the name Sockaddr is a good choice here, since it means something very different in "
					+ "the C world.  What do you think of SocketConnAddr instead?",
			"could we use sed here? " + " https://go-review.googlesource.com/#/c/10112/1/src/syscall/mkall.sh "
					+ " it will make the location of the build tag consistent across files (always before the package statement).",
			"Is the implementation hiding here important? This would be simpler still as: "
					+ " typedef struct GoSeq {   uint8_t *buf;   size_t off;   size_t len;   size_t cap; } GoSeq;",
			"Make sure you test both ways, or a bug that made it always return false would cause the test to pass. "
					+ " assertTrue(Testpkg.Negate(false)); " + " assertFalse(Testpkg.Negate(true)); "
					+ " If you want to use the assertEquals form, be sure the message makes clear what actually happened and "
					+ "what was expected (e.g. Negate(true) != false). ",
			"I think the comments here are a bad sign... I sent us down an unfortunate slippery slope  we are a search engine library!  don't put shit in the index directory! or we will delete your shit. dead simple.",
			" Matt... I'm an idiot  I used the wrong patch. Sigh.", "Adrian   thanks for the patch: rev. ",
			"Ah  damn  I thought it was fixed :/  Guillaume ? ",
			"You cannot design an API for logging. We should take out the ,\"command\" argument and have generic errors. It's easy looking at vdsm.log to discover what was the command executed before this failure.",
			"There is no VDSM action here, only database - just make the command transactive, and save yourself all this hassle."

	};

	private static String preprocessText(String text) {
		text = ContractionLoader.preprocessContractions(text);
		text = URLRemover.removeURL(text);
		text = EmoticonLoader.preprocessEmoticons(text);
		text = NegationHandler.handleNegation(text);
		return text;
	}

	public static void main(String[] args) {

		ArrayList<String> sentiText = new ArrayList<String>();
		for (int i = 0; i < sentences.length; i++) {
			sentiText.add(preprocessText(sentences[i]));
		}
		
		HashMap<Integer,String> classes=new HashMap<Integer,String>();
		classes.put(0, "Neutral");
		classes.put(1, "Negative");
		classes.put(2, "Positive");
		
		
		
		try {
			Instances testInstances = generateInstanceFromList(sentiText);
			Classifier classifier = WekaClassifierBuilder.getSavedClassfier();
			for (int j = 0; j < testInstances.size(); j++) {
				System.out.println(sentiText.get(j));
				System.out.println("class:  " + classes.get((int)classifier.classifyInstance(testInstances.get(j))));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Instances generateInstanceFromList(ArrayList<String> sentiText) throws Exception {
		Instances testInstances = ARFFTestGenerator.generateTestDataFromString(sentiText);
		StringToWordVector filter = new StringToWordVector();
		filter.setInputFormat(testInstances);
		WordTokenizer customTokenizer = new WordTokenizer();
		customTokenizer.setDelimiters(Constants.DELIMITERS);
		filter.setTokenizer(customTokenizer);
		filter.setStopwordsHandler(new MyStopWordsHandler());
		SnowballStemmer stemmer = new SnowballStemmer();
		filter.setStemmer(stemmer);
		filter.setLowerCaseTokens(true);
		filter.setTFTransform(true);
		filter.setIDFTransform(true);		
		testInstances = Filter.useFilter(testInstances, filter);
		return testInstances;
	}
}
