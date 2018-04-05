package edu.sentise.util;

public class DataLists {

	public static final String[] stop_words = { "i", "me", "my", "myself", "we", "our", "ourselves", "you", "your",
			"yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "herself", "it", "its", "itself",
			"they", "them", "their", "themselves", "this", "that", "these", "those", "am", "is", "are", "was", "were",
			"be", "been", "being", "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the",
			"and", "if", "or", "as", "until", "of", "at", "by", "between", "into", "through", "during", "to", "from",
			"in", "out", "on", "off", "then", "once", "here", "there", "all", "any", "both", "each", "few", "more",
			"other", "some", "such", "than", "too", "very", "s", "t", "can", "will", "don", "should", "now",

			// keywords

			"while", "case", "switch", "def", "abstract", "byte", "continue", "native", "private", "synchronized", "if",
			"do", "include", "each", "than", "finally", "class", "double", "float", "int", "else", "instanceof", "long",
			"super", "import", "short", "default", "catch", "try", "new", "final", "extends", "implements", "public",
			"protected", "static", "this", "return", "char", "const", "break", "boolean", "bool", "package", "byte",
			"assert", "raise", "global", "with", "or", "yield", "in", "out", "except", "and", "enum", "signed", "void",
			"virtual", "union", "goto", "var", "function", "require", "print", "echo", "foreach", "elseif", "namespace",
			"delegate", "event", "override", "struct", "readonly", "explicit", "interface", "get", "set", "elif", "for",
			"throw", "throws", "lambda", "endfor", "endforeach", "endif", "endwhile", "clone" ,"ifdef","mk",
			
			//from obersvation
			"ad","ah","ani","ag","ar","arg","b","c","cc","cbf","cl","d","e","g","h","ha","hf","hi","id","ie","j","k","l",
			"lrb","lsb","m","md","n","not_i","o","oh","onc","ond","p","py","r","rc","re","rcb","ro","rrb","rsb","u","v",
			"so","w","x","y","ye",
			//test symbols
			"''","'\\'","_","__"

	       };
	public static final String [] negation_words ={"not", "never", "none", "nobody", "nowhere", "neither", "barely", "hardly",
	                                                "nothing", "rarely", "seldom", "despite" };
	public static final String[] emoticon_words={"PositiveSentiment","NegativeSentiment"};
	public static final String[] intense_words={
			//positive
			"cool","thanks","thank","good","elegant","pleasure","cheers","PositiveSentiment",
			
			//negative
			"shit","damn","suck","sigh","idiot", "bug","weird","evil10=,.himsw","NegativeSentiment","crap","hell"};

}
