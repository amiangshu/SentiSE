package edu.sentise.test2;

import java.util.ArrayList;

import edu.sentise.model.SentimentData;
import edu.sentise.preprocessing.ContractionLoader;
import edu.sentise.preprocessing.EmoticonLoader;
import edu.sentise.preprocessing.MyStopWordsHandler;
import edu.sentise.preprocessing.NegationHandler;
import edu.sentise.preprocessing.URLRemover;
import edu.sentise.test.ARFFTestGenerator;
import edu.sentise.test.CustomImputMappedClassifer;
import edu.sentise.test.SentiSEModelEvaluator;
import edu.sentise.test.TestUtils;
import edu.stanford.nlp.pipeline.CustomAnnotationSerializer;
import weka.classifiers.Classifier;
import weka.classifiers.misc.InputMappedClassifier;
import weka.core.Instances;
import weka.core.stemmers.SnowballStemmer;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class SentiTest {

	private static String[] sentences={"I'm not sure I entirely understand what you are saying. "+
	                                   "However, looking at file_linux_test.go I'm pretty sure an interface type would be easier for people to use.",
	                                   "I think it always returns it as 0.",
	                                   "If the steal does not commit, there's no need to clean up _p_'s runq. If it doesn't commit,"+
	                                     " runqsteal just won't update runqtail, so it won't matter what's in _p_.runq.",
	                                   "Please change the subject: s:internal/syscall/windows:internal/syscall/windows/registry:",
	                                   "I don't think the name Sockaddr is a good choice here, since it means something very different in "+
	                                   "the C world.  What do you think of SocketConnAddr instead?",
	                                   "could we use sed here? "+
	                                    " https://go-review.googlesource.com/#/c/10112/1/src/syscall/mkall.sh "+
	                                    " it will make the location of the build tag consistent across files (always before the package statement).",
	                                   "Is the implementation hiding here important? This would be simpler still as: "+
	                                  " typedef struct GoSeq {   uint8_t *buf;   size_t off;   size_t len;   size_t cap; } GoSeq;",
	                                   "Make sure you test both ways, or a bug that made it always return false would cause the test to pass. "+
	                                " assertTrue(Testpkg.Negate(false)); "+
	                                " assertFalse(Testpkg.Negate(true)); "+
	                                " If you want to use the assertEquals form, be sure the message makes clear what actually happened and " +
	                                "what was expected (e.g. Negate(true) != false). ", 
	                                "I think the comments here are a bad sign... I sent us down an unfortunate slippery slope  we are a search engine library!  don't put shit in the index directory! or we will delete your shit. dead simple.",
	                                " Matt... I'm an idiot  I used the wrong patch. Sigh.",
	                                "Adrian   thanks for the patch: rev. ",
	                                "Ah  damn  I thought it was fixed :/  Guillaume ? ",
	                                "You cannot design an API for logging. We should take out the ,\"command\" argument and have generic errors. It's easy looking at vdsm.log to discover what was the command executed before this failure.",
	                                "There is no VDSM action here, only database - just make the command transactive, and save yourself all this hassle."
	                                
	                                    };
	public static void main(String[] args) {
		
		ArrayList<SentimentData> sentimentDataList=new ArrayList<>();
		for(int i=0;i<sentences.length;i++)
		{
			SentimentData sentimentData=new SentimentData(sentences[i], 1);
			sentimentDataList.add(sentimentData);
		}
		System.out.println(sentimentDataList.size());
		sentimentDataList=ContractionLoader.preprocessContractions(sentimentDataList);
		sentimentDataList=URLRemover.removeURL(sentimentDataList);
		sentimentDataList=EmoticonLoader.preprocessEmoticons(sentimentDataList);
		// ARFF is the default file format for weka. I converted our clean data to arff format
		// so that its easier to be compitable with weka. Shuffled the sentilist and divided 80% and 20% of the
		//data for train and test respectively
		sentimentDataList=NegationHandler.handleNegation(sentimentDataList);
		//ARFFTestGenerator.generateARFForWeka(sentimentDataList);
		try
		{
			Instances testInstances=ARFFTestGenerator.generateTestData(sentimentDataList);
			
			StringToWordVector filter = new StringToWordVector();
			filter.setInputFormat(testInstances);
	
			WordTokenizer customTokenizer = new WordTokenizer();
			String delimiters = " \r\t\n.,;:\'\"()?!-><#$\\%&*+/@^=[]{}|`~0123456789\'ï¿½â´¾ï¿½ï¿½â‚„ã¬¸ï¿½ï¿½ï¿½¬âã";
			customTokenizer.setDelimiters(delimiters);
			filter.setTokenizer(customTokenizer);
			filter.setStopwordsHandler(new MyStopWordsHandler());
			SnowballStemmer stemmer = new SnowballStemmer();
			filter.setStemmer(stemmer);
			filter.setLowerCaseTokens(true);
			filter.setTFTransform(true);
			filter.setIDFTransform(true);
			filter.setMinTermFreq(3);
			filter.setWordsToKeep(2500);
	
			// filter.setOutputWordCounts(true);
	
			
			System.out.println("Creating TF-IDF..");
			testInstances = Filter.useFilter(testInstances, filter);
			testInstances.setClassIndex(0);
			ARFFTestGenerator.writeInFile(testInstances);
			
			int len=testInstances.size();
			Classifier classifier=WekaClassifierBuilder.getSavedClassfier();
			for(int j=0;j<len;j++){
				// double[] prediction=classifier.distributionForInstance(testInstances.get(j));

			        //output predictions
				 System.out.println(sentimentDataList.get(j).getText());
			       /* for(int i=0; i<prediction.length; i=i+1)
			        {
			            System.out.println("Probability of class "+
			            		testInstances.classAttribute().value(i)+
			                               " : "+Double.toString(prediction[i]));
			        }*/
				System.out.println("class:  "+classifier.classifyInstance(testInstances.get(j)));
			}
		/*	CustomImputMappedClassifer classifier=WekaClassifierBuilder.getTestClassfier();
			for(int j=0;j<len;j++){
				 double[] prediction=classifier.distributionForInstance(testInstances.get(j));
                  System.out.println(classifier.getConvertedInstance(testInstances.get(j)));
			        //output predictions
				 System.out.println("instance: "+j);
			        for(int i=0; i<prediction.length; i=i+1)
			        {
			            System.out.println("Probability of class "+
			            		testInstances.classAttribute().value(i)+
			                               " : "+Double.toString(prediction[i]));
			        }
				//System.out.println(i+"  "+classifier.classifyInstance(testInstances.get(i)));
			}*/
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
