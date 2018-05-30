/*
 * Copyright (C) 2018 Southern Illinois University Carbondale, SoftSearch Lab
 *
 * Author: Amiangshu Bosu
 *
 * Licensed under GNU LESSER GENERAL PUBLIC LICENSE Version 3, 29 June 2007
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.siu.sentise;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import edu.siu.sentise.factory.BasicFactory;
import edu.siu.sentise.model.SentimentData;
import edu.siu.sentise.preprocessing.AncronymHandler;
import edu.siu.sentise.preprocessing.BiGramTriGramHandler;
import edu.siu.sentise.preprocessing.ContractionLoader;
import edu.siu.sentise.preprocessing.EmoticonProcessor;
import edu.siu.sentise.preprocessing.ExclamationHandler;
import edu.siu.sentise.preprocessing.IdentifierProcessor;
import edu.siu.sentise.preprocessing.MyStopWordsHandler;
import edu.siu.sentise.preprocessing.POSTagProcessor;
import edu.siu.sentise.preprocessing.QuestionMarkHandler;
import edu.siu.sentise.preprocessing.StanfordCoreNLPLemmatizer;
import edu.siu.sentise.preprocessing.StopwordWithKeywords;
import edu.siu.sentise.preprocessing.TextPreprocessor;
import edu.siu.sentise.preprocessing.URLRemover;
import edu.siu.sentise.util.Util;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.stemmers.NullStemmer;
import weka.core.stemmers.SnowballStemmer;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class SentiSE {

	private HashMap<Integer, Integer> classMapping;
	private Classifier classifier;
	private String emoticonDictionary = Configuration.EMOTICONS_FILE_NAME;
	private String stopWordDictionary = Configuration.EMPTY_FILE;
	private String contractionDictionary = Configuration.CONTRACTION_TEXT_FILE_NAME;
	private String oracleFileName = Configuration.ORACLE_FILE_NAME;
	private String acronymDictionary = Configuration.ACRONYM_WORD_FILE;

	private String arffFileName;

	private int minTermFrequeny = 3;
	private int maxWordsToKeep = 4000;

	private String algorithm = "RF";

	private boolean crossValidate = false;
	private boolean forceRcreateTrainingData = false;
	private boolean applyPosTag = false; // Apply POS tags with words
	private boolean keepOnlyImportantPos = false; // keepOnlyImportantPos means keeping only verbs,adjectives and
													// adverbs
	private boolean preprocessNegation = false; // preprocessNegation means handle the negation effects on other POS
	private boolean applyContextTag = false; // Apply context information of a word like
												// VP,ADVP or NP
	private int addSentiScoreType = 0; // if a sentence contains sentiment word. Add a correspponding string with it.
	private boolean processQuestionMark = false; // process question and exclamatory marks
	private boolean processExclamationMark = false;
	private boolean handleNGram = false;

	private boolean useStemmer = false;
	private boolean useLemmatizer = false;
	private boolean removeIdentifiers = false;
	private boolean removeKeywords = false;
	private boolean removeStopwords=false;
	private boolean markSlangWords=false;
	private Random rand;
	private static int REPEAT_COUNT = 10;
	private boolean categorizeEmoticon = false;
	private String outputFile;
	Instances trainingInstances = null;

	private MyStopWordsHandler stopWordHandler;

	public void setEmoticonDictionary(String emoticonDictionary) {
		this.emoticonDictionary = emoticonDictionary;
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

	public boolean isRemoveIdentifiers() {
		return removeIdentifiers;
	}

	public void setRemoveIdentifiers(boolean removeIdentifiers) {
		this.removeIdentifiers = removeIdentifiers;
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

	public void setKeepPosTag(boolean keep) {
		applyPosTag = keep;
	}

	public boolean isRemoveKeywords() {
		return removeKeywords;
	}

	public void setRemoveKeywords(boolean removeKeywords) {
		this.removeKeywords = removeKeywords;
	}

	public boolean isCategorizeEmoticon() {
		return categorizeEmoticon;
	}

	public void setCategorizeEmoticon(boolean categorizeEmoticon) {
		this.categorizeEmoticon = categorizeEmoticon;
	}

	public boolean isUseStopWords() {
		return removeStopwords;
	}

	public void setUseStopWords(boolean useStopWords) {
		this.removeStopwords = useStopWords;
	}

	private ArrayList<TextPreprocessor> preprocessPipeline = new ArrayList<TextPreprocessor>();

	public SentiSE() {
		this.stopWordHandler=new MyStopWordsHandler(this.stopWordDictionary);
		
		// common preprocessing steps, always applied
		preprocessPipeline.add(new ContractionLoader(this.contractionDictionary));
		preprocessPipeline.add(new URLRemover());
		preprocessPipeline.add(new AncronymHandler(this.acronymDictionary));

	}

	private void createresultsFiles() {
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

		this.outputFile = Configuration.OUTPUT_DIRECTORY + this.algorithm + "_" + timeStamp + ".txt";
		this.arffFileName = Configuration.ARFF_DIRECTORY + timeStamp + ".arff";
	}

	private void createCombinedResultFile() {
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

		this.outputFile = Configuration.OUTPUT_DIRECTORY + "combined_" + timeStamp + ".txt";
		this.arffFileName = Configuration.ARFF_DIRECTORY + timeStamp + ".arff";
	}

	public void generateTrainingInstance() throws Exception {

		System.out.println("Reading oracle file...");
		ArrayList<SentimentData> sentimentDataList = SentimentData.parseSentimentData(this.oracleFileName);

		if (this.categorizeEmoticon)
			this.emoticonDictionary = Configuration.EMOTICONS_CATEGORIZED;
		else
			this.emoticonDictionary = Configuration.EMOTICONS_FILE_NAME;
		

		preprocessPipeline.add(new EmoticonProcessor(this.emoticonDictionary));
		
		if (this.removeIdentifiers)
			preprocessPipeline.add(new IdentifierProcessor());
		
		if (this.processExclamationMark)
			preprocessPipeline.add(new ExclamationHandler());

		if (this.processQuestionMark)
			preprocessPipeline.add(new QuestionMarkHandler());
		
		if (this.handleNGram)
			preprocessPipeline.add(new BiGramTriGramHandler());
		
		if(this.removeStopwords)
		{
			this.stopWordDictionary=Configuration.STOPWORDS_FILE_NAME;
			this.stopWordHandler=new MyStopWordsHandler(this.stopWordDictionary);
		}
		
		if (this.removeKeywords)
			this.stopWordHandler = new StopwordWithKeywords(stopWordDictionary, Configuration.KEYWORD_LIST_FILE);

		System.out.println("Preprocessing text ..");
		preprocessPipeline.add(new POSTagProcessor(
				BasicFactory.getPOSUtility(applyPosTag, keepOnlyImportantPos, applyContextTag, stopWordHandler),
				this.preprocessNegation, addSentiScoreType,this.markSlangWords));

		for (TextPreprocessor process : preprocessPipeline) {
			sentimentDataList = process.apply(sentimentDataList);
		}

		/*
		 * for(int i= 0;i<sentimentDataList.size();i++)
		 * System.out.println(sentimentDataList.get(i).getText());
		 */
		System.out.println("Converting to WEKA format ..");
		Instances rawInstance = ARFFGenerator.generateTestData(sentimentDataList);

		System.out.println("Converting string to vector..");
		this.trainingInstances = generateFilteredInstance(rawInstance, true);

		this.trainingInstances.setClassIndex(0);

		// adding info gain
		// trainingInstances=getInstancesFilteredByInformationgain(trainingInstances);
		storeAsARFF(this.trainingInstances, this.arffFileName);
		this.setForceRcreateTrainingData(false);

	}

	private void storeAsARFF(Instances instance, String fileName) {

		ARFFGenerator.writeInFile(this.trainingInstances, fileName);
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

		this.generateTrainingInstance();
		// trainingInstances = applyOversampling(trainingInstances);
		System.out.println("Training classifier..");
		this.classifier = WekaClassifierBuilder.createClassifierFromInstance(this.algorithm, this.trainingInstances);
		WekaClassifierBuilder.storeClassfierModel("models/" + this.algorithm + "." + this.oracleFileName + ".model",
				this.classifier);

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
		// text = contractionHandler.preprocessContractions(text);
		// text = URLRemover.removeURL(text);
		// text = emoticonHandler.preprocessEmoticons(text);
		// text = ParserUtility.preprocessPOStags(text);
		return text;
	}

	private Instances generateInstanceFromList(ArrayList<String> sentiText) throws Exception {
		Instances instance = ARFFGenerator.generateTestDataFromString(sentiText);
		return generateFilteredInstance(instance, false);

	}

	private Instances generateFilteredInstance(Instances instance, boolean disardLowFreqTerms) throws Exception {
		StringToWordVector filter = new StringToWordVector();
		filter.setInputFormat(instance);
		WordTokenizer customTokenizer = new WordTokenizer();
		customTokenizer.setDelimiters(Configuration.DELIMITERS);
		filter.setTokenizer(customTokenizer);

		
		filter.setStopwordsHandler(this.stopWordHandler);

		if (this.useStemmer) {
			SnowballStemmer snowballStemmer = new SnowballStemmer();
			filter.setStemmer(snowballStemmer);
		} else if (this.useLemmatizer) {
			StanfordCoreNLPLemmatizer lemmatizer = new StanfordCoreNLPLemmatizer();
			filter.setStemmer(lemmatizer);
		} else
			filter.setStemmer(new NullStemmer());

		System.out.println(useLemmatizer + " " + useStemmer + "  " + filter.getStemmer());
		filter.setLowerCaseTokens(true);
		filter.setTFTransform(true);
		filter.setIDFTransform(true);
		if (disardLowFreqTerms) {
			filter.setMinTermFreq(this.minTermFrequeny);
			filter.setWordsToKeep(this.maxWordsToKeep);
		}

		return Filter.useFilter(instance, filter);

	}

	private Instances getInstancesFilteredByInformationgain(Instances instances) {
		try {
			AttributeSelection filter = new AttributeSelection();
			InfoGainAttributeEval evaluator = new InfoGainAttributeEval();
			filter.setEvaluator(evaluator);
			Ranker search = new Ranker();
			search.setThreshold(0);
			filter.setSearch(search);
			filter.SelectAttributes(instances);
			int[] selected = filter.selectedAttributes();

			Remove removeFilter = new Remove();

			removeFilter.setAttributeIndicesArray(selected);
			removeFilter.setInvertSelection(true);
			removeFilter.setInputFormat(instances);
			return Filter.useFilter(instances, removeFilter);
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return instances;

	}

	private void initRand(long value) {

		rand = new Random(value);
	}

	private CrossValidationResult tenFoldCV() {

		try {

			String arffFileName = this.arffFileName;
			File arffFile = new File(arffFileName);

			if (!arffFile.exists() || this.isForceRcreateTrainingData()) {
				this.generateTrainingInstance();
			} else {
				this.trainingInstances = loadInstanceFromARFF(arffFileName);

			}
			int folds = 10;

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

				Instances train = null, test = null;

				train = randData.trainCV(folds, n);
				test = randData.testCV(folds, n);

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
				//eval.
				kappa[n] = Util.computeWeightedKappa(eval);

				System.out.println("Accuracy:" + eval.pctCorrect());
				System.out.println(" Weighted Kappa:" + kappa[n]);

				System.out.println(" Precision(positive):" + eval.precision(2));
				System.out.println("Recall(positive):" + eval.recall(2));
				System.out.println("Fmeasure(positive):" + eval.fMeasure(2));

				System.out.println(" Precision(neutral):" + eval.precision(0));
				System.out.println("Recall(neutral):" + eval.recall(0));
				System.out.println("Fmeasure(neutral):" + eval.fMeasure(0));

				System.out.println(" Precision(negative):" + eval.precision(1));
				System.out.println("Recall(negative):" + eval.recall(1));
				System.out.println("Fmeasure(negative):" + eval.fMeasure(1));

			}
			CrossValidationResult result = new CrossValidationResult();
			result.setAccuracy(getAverage(accuracies));
			result.setPosPrecision(getAverage(pos_precision));
			result.setNegPrecision(getAverage(neg_precision));
			result.setNeuPrecision(getAverage(neu_precision));
			result.setPosRecall(getAverage(pos_recall));
			result.setNegRecall(getAverage(neg_recall));
			result.setNeuRecall(getAverage(neu_recall));
			result.setPosFmeasure(getAverage(pos_fscore));
			result.setNegFmeasure(getAverage(neg_fscore));
			result.setNeuFmeasure(getAverage(neu_fscore));

			result.setKappa(getAverage(kappa));

			System.out.println("Algorithm:" + this.algorithm + "\n Oracle:" + this.oracleFileName);
			System.out.println("\n\n.......Average......: ");
			System.out.println("Accuracy:" + result.getAccuracy());
			System.out.println(" Weighted Kappa: " + getAverage(kappa));

			System.out.println("Precision (Positive):" + result.getPosPrecision());
			System.out.println("Recall (Positive):" + result.getPosRecall());
			System.out.println("F-Measure (Positive):" + result.getPosFmeasure());

			System.out.println("Precision (Neutral):" + result.getNeuPrecision());
			System.out.println("Recall (Neutral):" + result.getNeuRecall());
			System.out.println("F-Measure (Neutral):" + result.getNeuFmeasure());

			System.out.println("Precision (Negative):" + result.getNegPrecision());
			System.out.println("Recall (Negative):" + result.getNegRecall());
			System.out.println("F-measure (Negative):" + result.getNegFmeasure());

			// printConfiguration();
			return result;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getConfiguration() {
		StringBuilder builder = new StringBuilder();
		builder.append(".......Configuration......: ");
		builder.append("\n");
		// builder.append("Algorithm: " + this.algorithm);
		// builder.append("\n");
		builder.append("Use ngram: " + this.handleNGram);
		builder.append("\n");
		builder.append("Categorize emoticons: " + this.categorizeEmoticon);
		builder.append("\n");
		builder.append("Negation preprocess: " + this.preprocessNegation);
		builder.append("\n");
		builder.append("Context tag: " + this.applyContextTag);
		builder.append("\n");
		builder.append("POS tag: " + this.applyPosTag);
		builder.append("\n");
		builder.append("Replace question mark: " + this.processQuestionMark);
		builder.append("\n");
		builder.append("Replace exclamation mark: " + this.processExclamationMark);
		builder.append("\n");
		builder.append("Remove identifiers: " + this.removeIdentifiers);
		builder.append("\n");
		builder.append("Remove programming keywords: " + this.removeKeywords);
		builder.append("\n");
		builder.append("Remove stopwords:"  + this.removeStopwords);
		builder.append("\n");
		builder.append("Mark swearwords:"  + this.markSlangWords);
		builder.append("\n");

		builder.append("Stemming:" + this.useStemmer);
		builder.append("\n");
		builder.append("Lemmatization:" + this.useLemmatizer);
		builder.append("\n");
		builder.append("Only V, Adv, Adj:" + this.keepOnlyImportantPos);
		builder.append("\n");
		builder.append("Mark sentiment words:" + this.addSentiScoreType);
		builder.append("\n");
		builder.append("Min term frequency:" + this.minTermFrequeny);
		builder.append("\n");
		builder.append("Max features:" + this.maxWordsToKeep);
		builder.append("\n");
		return builder.toString();
	}

	private float getAverage(double[] elements) {
		double sum = 0.0;
		for (int i = 0; i < elements.length; i++)
			sum = sum + elements[i];

		// calculate average value
		double average = sum / elements.length;
		return (float) average;
	}

	public void runRepeatedValidation() {
		createresultsFiles();
		ArrayList<CrossValidationResult> cvResults = new ArrayList<CrossValidationResult>();

		try {
			setForceRcreateTrainingData(true);

			initRand(5555);

			for (int i = 0; i < REPEAT_COUNT; i++) {
				CrossValidationResult result = tenFoldCV();
				cvResults.add(result);
			}

			StringBuilder outputBuffer = new StringBuilder();
			outputBuffer.append(getConfiguration());
			outputBuffer.append("\n\n------Results-------\n");

			outputBuffer.append(CrossValidationResult.getResultHeader() + "\n");

			for (CrossValidationResult result : cvResults) {
				outputBuffer.append(result.toString() + "\n");
			}
			outputBuffer.append(totalAverage(cvResults));

			this.writeResultsToFile(outputBuffer.toString() + "\n");

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void runCVWithSameConfig() {
		createCombinedResultFile();

		setForceRcreateTrainingData(true);

		ArrayList<CrossValidationResult> cvResults = new ArrayList<CrossValidationResult>();

		String[] algorithms = { "RF","SL", "CNN", "LMT"};

		
		try {
			this.writeResultsToFile(getConfiguration());
			this.writeResultsToFile("\n\n------Results-------\n");
		} catch (IOException e) {
			
		}
		

		for (String algo : algorithms) {
			this.algorithm = algo;
			cvResults.clear();

			try {

				initRand(5555);

				this.writeResultsToFile("\n\n------" + algo + "-------\n");
				this.writeResultsToFile(CrossValidationResult.getResultHeader() + "\n");
				
				for (int i = 0; i < REPEAT_COUNT; i++) {
					CrossValidationResult result = tenFoldCV();
					cvResults.add(result);
					this.writeResultsToFile(result.toString()+"\n");
				}
				

				this.writeResultsToFile(totalAverage(cvResults)+"\n");				

			} catch (Exception e1) {
				 
			}
		}
	}

	private void writeResultsToFile(String text) throws IOException {

		BufferedWriter writer = new BufferedWriter(new FileWriter(this.outputFile, true));
		writer.write(text);
		writer.close();
	}

	private String totalAverage(ArrayList<CrossValidationResult> cvResults) {
		double[] results = new double[11];
		for (CrossValidationResult result : cvResults) {
			String[] splits = result.toString().split(",");
			for (int i = 0; i < splits.length; i++)
				results[i] += Double.parseDouble(splits[i]);
		}
		for (int i = 0; i < results.length; i++)
			results[i] /= 10;
		String res = "";
		for (int i = 0; i < results.length; i++) {
			if (i > 0)
				res += ",";

			res += results[i];
		}
		return res;

	}

	public static void main(String[] args) {

		SentiSE instance = new SentiSE();
		if (!instance.isCommandLineParsed(args))
			return;

		//instance.runCVWithSameConfig();
		instance.runRepeatedValidation();

	}

	private boolean isCommandLineParsed(String[] args) {
		CommandLineParser commandLineParser = new DefaultParser();

		Options options = new Options();

		options.addOption(Option.builder("algo").hasArg(true).desc(
				"Algorithm for classifier. \nChoices are: RF| DT | NB| SVM | KNN | MLPC | LMT| SVM | SL (Default) | RS")
				.build());
		options.addOption(Option.builder("help").hasArg(false).desc("Prints help message").build());
		options.addOption(Option.builder("root").hasArg(true)
				.desc("Word normalization.\n 0=None (Default) | 1=Stemming | 2=Lemmatization ").build());
		options.addOption(Option.builder("negate").hasArg(false)
				.desc("Prefix words in negative context\n Default: False").build());
		options.addOption(Option.builder("tag").hasArg(true)
				.desc("Add tags to words.\n0=None (Default)| 1= POS | 2=Context ").build());
		options.addOption(Option.builder("ngram").hasArg(false).desc("Use ngrams. Default: False").build());
		options.addOption(Option.builder("features").hasArg(true)
				.desc("Features to use.\n 1 = All (default) | 2 = Only Verbs, Adverbs, and Adjectives").build());
		options.addOption(Option.builder("punctuation").hasArg(true)
				.desc("Mark punctuations.\n 0= None (default) | 1= Question | 2= Exclamation | 3=Both ").build());
		options.addOption(Option.builder("sentiword").hasArg(true)
				.desc("Count sentiment words.\n 0= None (default) | 2= Two groups |4= Four groups ").build());
		options.addOption(Option.builder("output").hasArg(true).desc("Output file").build());
		options.addOption(Option.builder("oracle").hasArg(true).desc("Training dataset (Excel)").build());

		options.addOption(Option.builder("identifier").hasArg(false).desc("Remove identifiers").build());
		options.addOption(Option.builder("keyword").hasArg(false).desc("Remove programming Keywords").build());

		options.addOption(Option.builder("emocat").hasArg(false).desc("Categorize emoticons").build());
		options.addOption(Option.builder("allwords").hasArg(false).desc("Remove stop words").build());
		options.addOption(Option.builder("slang").hasArg(false).desc("Count slang words").build());

		Option termFreq = Option.builder("minfreq").hasArg()
				.desc("Minimum frequecy required to be considered as a feature. Default: 5").build();
		termFreq.setType(Number.class);
		options.addOption(termFreq);

		Option maxterms = Option.builder("maxfeatures").hasArg().desc("Maximum number of features. Default: 2500")
				.build();
		termFreq.setType(Number.class);
		options.addOption(maxterms);

		try {
			CommandLine commandLine = commandLineParser.parse(options, args);
			HelpFormatter formatter = new HelpFormatter();
			if (commandLine.hasOption("help")) {

				printUsageAndExit(options, formatter);
			}

			if (commandLine.hasOption("algo")) {
				String algo = commandLine.getOptionValue("algo");
				if (algo.equals("RF") || algo.equals("DT") || algo.equals("NB") || 
						algo.equals("CNN") || algo.equals("SVM") || algo.equals("MLPC") || algo.equals("SL")
						|| algo.equals("KNN") || algo.equals("RS")|| algo.equals("LMT"))
					this.algorithm = algo;
				else
					printUsageAndExit(options, formatter);
			}

			if (commandLine.hasOption("root")) {
				if (commandLine.getOptionValue("root").equals("1")) {
					useStemmer = true;
					useLemmatizer = false;
				} else if (commandLine.getOptionValue("root").equals("2")) {
					useStemmer = false;
					useLemmatizer = true;
				} else {
					useStemmer = false;
					useLemmatizer = false;
				}

			}

			if (commandLine.hasOption("negate")) {
				setPreprocessNegation(true);
			}
			
			if (commandLine.hasOption("allwords")) {
				this.setUseStopWords(true);
			}

			if (commandLine.hasOption("identifier")) {
				this.setRemoveIdentifiers(true);
			}

			if (commandLine.hasOption("emocat")) {
				this.setCategorizeEmoticon(true);
			}
			if (commandLine.hasOption("keyword")) {
				this.setRemoveKeywords(true);
			}

			if (commandLine.hasOption("output")) {
				this.outputFile = commandLine.getOptionValue("output");
			}

			if (commandLine.hasOption("oracle")) {
				this.oracleFileName = commandLine.getOptionValue("oracle");
			}

			if (commandLine.hasOption("tag")) {
				if (commandLine.getOptionValue("tag").equals("1")) {
					applyPosTag = true;
					applyContextTag = false;
				}

				else if (commandLine.getOptionValue("tag").equals("2")) {
					applyPosTag = false;
					applyContextTag = true;
				} else {
					applyPosTag = false;
					applyContextTag = false;
				}

			}

			if (commandLine.hasOption("punctuation")) {
				if (commandLine.getOptionValue("punctuation").equals("1")) {
					processQuestionMark = true;
					processExclamationMark = false;
				} else if (commandLine.getOptionValue("punctuation").equals("2")) {
					processQuestionMark = false;
					processExclamationMark = true;
				} else if (commandLine.getOptionValue("punctuation").equals("3")) {
					processQuestionMark = true;
					processExclamationMark = true;
				} else {
					processQuestionMark = false;
					processExclamationMark = false;
				}

			}

			if (commandLine.hasOption("features")) {
				if (commandLine.getOptionValue("features").equals("2"))
					keepOnlyImportantPos = true;
				else
					keepOnlyImportantPos = false;

			}
			if (commandLine.hasOption("sentiword")) {
				if (commandLine.getOptionValue("sentiword").equals("0"))
					addSentiScoreType = 0;
				else if (commandLine.getOptionValue("sentiword").equals("2"))
					addSentiScoreType = 2;
				else if (commandLine.getOptionValue("sentiword").equals("4"))
					addSentiScoreType = 4;

			}

			if (commandLine.hasOption("ngram")) {

				handleNGram = true;
			}
			
			if (commandLine.hasOption("slang")) {

				this.markSlangWords=true;
			}

			if (commandLine.hasOption("minfreq")) {

				this.minTermFrequeny = Integer.parseInt(commandLine.getOptionValue("minfreq"));
			}

			if (commandLine.hasOption("maxfeatures")) {

				this.maxWordsToKeep = Integer.parseInt(commandLine.getOptionValue("maxfeatures"));
			}

		} catch (ParseException e) {
			e.printStackTrace();

		}
		return true;
	}

	private void printUsageAndExit(Options options, HelpFormatter formatter) {
		formatter.printHelp("sentise", options, true);
		System.exit(0);
	}

}
