package edu.sentise;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;

import edu.sentise.factory.BasePOSUtility;
import edu.sentise.factory.BasicFactory;
import edu.sentise.model.SentimentData;
import edu.sentise.preprocessing.ContractionLoader;
import edu.sentise.preprocessing.EmoticonLoader;
import edu.sentise.preprocessing.MyStopWordsHandler;
import edu.sentise.preprocessing.ParserUtility;
import edu.sentise.preprocessing.URLRemover;
import edu.sentise.test.ARFFTestGenerator;
import edu.sentise.util.Constants;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.stemmers.SnowballStemmer;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.supervised.instance.SMOTE;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class SentiSE {

	private HashMap<Integer, Integer> classMapping;
	private EmoticonLoader emoticonHandler;
	private ContractionLoader contractionHandler;
	private Classifier classifier;
	private String emoticonDictionary = Constants.EMOTICONS_FILE_NAME;
	private String stopWordDictionary = Constants.STOPWORDS_FILE_NAME;
	private String contractionDictionary = Constants.CONTRACTION_TEXT_FILE_NAME;
	private String oracleFileName = Constants.ORACLE_FILE_NAME;
	private int minTermFrequeny = 3;
	private int maxWordsToKeep = 2500;
	private String algorithm = "RF";

	private boolean crossValidate = false;
	private boolean forceRcreateTrainingData = false;
	private boolean keepPosTag=false;            //keepPosTag means add POS tags with words
	private boolean keepOnlyImportantPos=true;      //keepOnlyImportantPos means keeping only verbs,adjectives and adverbs
	private boolean preprocessNegation = true;       // preprocessNegation means handle the negation effects on other POS


	Instances trainingInstances = null;

	public void setEmoticonDictionary(String emoticonDictionary) {
		this.emoticonDictionary = emoticonDictionary;
	}

	public void setStopWordDictionary(String stopWordDictionary) {
		this.stopWordDictionary = stopWordDictionary;
	}

	public void setContractionDictionary(String contractionDictionary) {
		this.contractionDictionary = contractionDictionary;
	}

	public void setOracleFileName(String oracleFileName) {
		this.oracleFileName = oracleFileName;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public int getMinTermFrequeny() {
		return minTermFrequeny;
	}

	public void setMinTermFrequeny(int minTermFrequeny) {
		this.minTermFrequeny = minTermFrequeny;
	}

	public int getMaxWordsToKeep() {
		return maxWordsToKeep;
	}

	public void setMaxWordsToKeep(int maxWordsToKeep) {
		this.maxWordsToKeep = maxWordsToKeep;
	}

	public void setPreprocessNegation(boolean preprocessNegation) {
		this.preprocessNegation = preprocessNegation;
	}

	public boolean isCrossValidate() {
		return crossValidate;
	}

	public void setCrossValidate(boolean crossValidate) {
		this.crossValidate = crossValidate;
	}

	public boolean isForceRcreateTrainingData() {
		return forceRcreateTrainingData;
	}

	public void setForceRcreateTrainingData(boolean forceRcreateTrainingData) {
		this.forceRcreateTrainingData = forceRcreateTrainingData;
	}
	public void setKeepPosTag(boolean keep)
	{
		keepPosTag=keep;
	}

	public SentiSE() {
		ParserUtility.initCoreNLP();
		emoticonHandler = new EmoticonLoader(this.emoticonDictionary);
		contractionHandler = new ContractionLoader(this.contractionDictionary);
		classifier = WekaClassifierBuilder.getSavedClassfier(Constants.MODEL_FILE_NAME);
		classMapping = new HashMap<Integer, Integer>();
		classMapping.put(0, 0);
		classMapping.put(1, -1);
		classMapping.put(2, 1);
	}

	public void generateTrainingInstance(boolean oversample) throws Exception {

		System.out.println("Reading oracle file...");
		ArrayList<SentimentData> sentimentDataList = SentimentData.parseSentimentData(Constants.ORACLE_FILE_NAME);

		System.out.println("Preprocessing text ..");
		sentimentDataList = contractionHandler.preprocessContractions(sentimentDataList);
		sentimentDataList = URLRemover.removeURL(sentimentDataList);
		sentimentDataList = emoticonHandler.preprocessEmoticons(sentimentDataList);
		//ParserUtility.setShouldIncludePos(keepPosTag);
		ParserUtility.setBasePOSUtility(BasicFactory.getPOSUtility(keepPosTag, keepOnlyImportantPos));
		ParserUtility.setHandleNegation(preprocessNegation);
		//ParserUtility.setonlyKeepImportantPos(keepOnlyImportantPos);
	    sentimentDataList = ParserUtility.preprocessPOStags(sentimentDataList);

		System.out.println("Converting to WEKA format ..");
		Instances rawInstance = ARFFTestGenerator.generateTestData(sentimentDataList);

		System.out.println("Converting string to vector..");
		this.trainingInstances = generateFilteredInstance(rawInstance, true);

		this.trainingInstances.setClassIndex(0);

		storeAsARFF(this.trainingInstances, this.oracleFileName + ".arff");

	}

	private void storeAsARFF(Instances instance, String fileName) {

		ARFFTestGenerator.writeInFile(this.trainingInstances, fileName);
		System.out.println("Instance saved as:" + fileName);
	}

	private Instances loadInstanceFromARFF(String arffFileName) throws Exception {
		DataSource dataSource = new DataSource(arffFileName);
		Instances loadedInstance = dataSource.getDataSet();
		loadedInstance.setClassIndex(0);
		System.out.println("Instance loaded from:" + arffFileName);
		return loadedInstance;
	}

	public void reloadClassifier() throws Exception {

		this.generateTrainingInstance(true);
		trainingInstances = applyOversampling(trainingInstances);
		System.out.println("Training classifier..");
		this.classifier = WekaClassifierBuilder.createClassifierFromInstance(this.algorithm, this.trainingInstances);
		WekaClassifierBuilder.storeClassfierModel("models/" + this.algorithm + "." + this.oracleFileName + ".model",
				this.classifier);

	}

	public Instances applyOversampling(Instances filteredInstance) throws Exception {
		int count[] = filteredInstance.attributeStats(0).nominalCounts;
		System.out.println("Instances 0->" + count[0] + ", -1->" + count[1] + ", 1->" + count[2]);

		System.out.println("Creating synthetic negative samples");
		SMOTE oversampler = new SMOTE();
		oversampler.setNearestNeighbors(15);
		oversampler.setClassValue("2");

		oversampler.setInputFormat(filteredInstance);
		filteredInstance = Filter.useFilter(filteredInstance, oversampler);
		System.out.println("Creating synthetic positive samples");
		SMOTE oversampler2 = new SMOTE();
		oversampler2.setClassValue("3");
		oversampler2.setNearestNeighbors(15);
		oversampler2.setPercentage(40);
		oversampler2.setInputFormat(filteredInstance);

		filteredInstance = Filter.useFilter(filteredInstance, oversampler2);
		System.out.println("Finished oversampling..");
		return filteredInstance;

	}

	public int[] getSentimentScore(ArrayList<String> sentences) throws Exception {

		ArrayList<String> sentiText = new ArrayList<String>();
		for (int i = 0; i < sentences.size(); i++) {
			sentiText.add(preprocessText(sentences.get(i)));
		}

		int[] computedScores = new int[sentences.size()];

		Instances testInstances = generateInstanceFromList(sentiText);

		for (int j = 0; j < testInstances.size(); j++) {

			computedScores[j] = classMapping.get((int) classifier.classifyInstance(testInstances.get(j)));

		}
		return computedScores;
	}

	private String preprocessText(String text) {
		text = contractionHandler.preprocessContractions(text);
		text = URLRemover.removeURL(text);
		text = emoticonHandler.preprocessEmoticons(text);
		text = ParserUtility.preprocessPOStags(text);
		return text;
	}

	private Instances generateInstanceFromList(ArrayList<String> sentiText) throws Exception {
		Instances instance = ARFFTestGenerator.generateTestDataFromString(sentiText);
		return generateFilteredInstance(instance, false);

	}

	private Instances generateFilteredInstance(Instances instance, boolean disardLowFreqTerms) throws Exception {
		StringToWordVector filter = new StringToWordVector();
		filter.setInputFormat(instance);
		WordTokenizer customTokenizer = new WordTokenizer();
		customTokenizer.setDelimiters(Constants.DELIMITERS);
		filter.setTokenizer(customTokenizer);
		filter.setStopwordsHandler(new MyStopWordsHandler());
		SnowballStemmer stemmer = new SnowballStemmer();
		filter.setStemmer(stemmer);
		filter.setLowerCaseTokens(true);
		filter.setTFTransform(true);
		filter.setIDFTransform(true);
		if (disardLowFreqTerms) {
			filter.setMinTermFreq(this.minTermFrequeny);
			filter.setWordsToKeep(this.maxWordsToKeep);
		}

		return Filter.useFilter(instance, filter);

	}

	private void tenFoldCV() {

		try {

			String arffFileName = this.oracleFileName + ".arff";
			File arffFile = new File(arffFileName);

			if (!arffFile.exists() || this.isForceRcreateTrainingData()) {
				this.generateTrainingInstance(false);
			} else {
				this.trainingInstances = loadInstanceFromARFF(arffFileName);

			}
			int folds = 2;

			Random rand = new Random(System.currentTimeMillis());
			Instances randData = new Instances(this.trainingInstances);
			randData.randomize(rand);

			double pos_precision[] = new double[folds];
			double neg_precision[] = new double[folds];
			double neu_precision[] = new double[folds];

			double pos_recall[] = new double[folds];
			double neg_recall[] = new double[folds];
			double neu_recall[] = new double[folds];

			double pos_fscore[] = new double[folds];
			double neg_fscore[] = new double[folds];
			double neu_fscore[] = new double[folds];

			double accuracies[] = new double[folds];
			double kappa[] = new double[folds];

			// perform cross-validation
			Evaluation eval = new Evaluation(randData);
			for (int n = 0; n < folds; n++) {
				System.out.println(".............................");
				System.out.println(".......Testing on Fold:" + n);
				System.out.println("..........................");
				File oracleFile = new File(this.oracleFileName);

				File trainingFile = new File("models/cv/" + oracleFile.getName() + ".training." + n + ".arff");
				File testFile = new File("models/cv/" + oracleFile.getName() + ".test." + n + ".arff");
				Instances train = null, test = null;
				boolean loadedFromCache = false;

				if (!this.forceRcreateTrainingData && trainingFile.exists() && testFile.exists()) {
					try {
						train = loadInstanceFromARFF(trainingFile.getPath());
						test = loadInstanceFromARFF(testFile.getPath());
						loadedFromCache = true;
					} catch (Exception e) {
						e.printStackTrace();

					}

				}

				if (!loadedFromCache) {

					train = this.applyOversampling(randData.trainCV(folds, n));
					test = randData.testCV(folds, n);
					//storeAsARFF(train, trainingFile.getPath());
					//storeAsARFF(test, testFile.getPath());

				}

				Classifier clsCopy = WekaClassifierBuilder.getClassifierForAlgorithm(this.algorithm);
				System.out.println("Training classifier model..");
				clsCopy.buildClassifier(train);
				eval.evaluateModel(clsCopy, test);

				accuracies[n] = eval.pctCorrect();

				neu_precision[n] = eval.precision(0);
				neg_precision[n] = eval.precision(1);
				pos_precision[n] = eval.precision(2);

				neu_fscore[n] = eval.fMeasure(0);
				neg_fscore[n] = eval.fMeasure(1);
				pos_fscore[n] = eval.fMeasure(2);

				neu_recall[n] = eval.recall(0);
				neg_recall[n] = eval.recall(1);
				pos_recall[n] = eval.recall(2);
				kappa[n] = eval.kappa();

				System.out.println("Accuracy:" + eval.pctCorrect());
				System.out.println("Kappa:" + eval.kappa());

				System.out.println(" Precision(neutral):" + eval.precision(0));
				System.out.println("Recall(neutral):" + eval.recall(0));
				System.out.println("Fmeasure(neutral):" + eval.fMeasure(0));

				System.out.println(" Precision(negative):" + eval.precision(1));
				System.out.println("Recall(negative):" + eval.recall(1));
				System.out.println("Fmeasure(negative):" + eval.fMeasure(1));

				System.out.println(" Precision(positive):" + eval.precision(2));
				System.out.println("Recall(positive):" + eval.recall(2));
				System.out.println("Fmeasure(positive):" + eval.fMeasure(2));

			}

			System.out.println("\n\n.......Average......: \n\n");
			System.out.println("Accuracy:" + getAverage(accuracies));

			System.out.println("Algorithm:" + this.algorithm + "\n Oracle:" + this.oracleFileName);
			System.out.println("Precision (Neutral):" + getAverage(neu_precision));
			System.out.println("Recall (Neutral):" + getAverage(neu_recall));
			System.out.println("F-Measure (Neutral):" + getAverage(neu_fscore));

			System.out.println("Precision (Negative):" + getAverage(neg_precision));
			System.out.println("Recall (Negative):" + getAverage(neg_recall));
			System.out.println("F-measure (Negative):" + getAverage(neg_fscore));

			System.out.println("Precision (Positive):" + getAverage(pos_precision));
			System.out.println("Recall (Positive):" + getAverage(pos_recall));
			System.out.println("F-Measure (Positive):" + getAverage(pos_fscore));

			System.out.println("Kappa" + getAverage(kappa));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private double getAverage(double[] elements) {
		double sum = 0.0;
		for (int i = 0; i < elements.length; i++)
			sum = sum + elements[i];

		// calculate average value
		double average = sum / elements.length;
		return average;
	}

	public static void main(String[] args) {

		String[] testSentences = { "I'm not sure I entirely understand what you are saying. "
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
		SentiSE instance = new SentiSE();
		if (args.length > 0)
			instance.setAlgorithm(args[0].trim());

		try {
			instance.setForceRcreateTrainingData(true);
			instance.tenFoldCV();

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		/*try {
			int[] scores = instance.getSentimentScore(new ArrayList<String>(Arrays.asList(testSentences)));

			for (int i = 0; i < testSentences.length; i++) {
				System.out.println(testSentences[i]);
				System.out.println("Prediction:" + scores[i]);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
*/
	}

}
