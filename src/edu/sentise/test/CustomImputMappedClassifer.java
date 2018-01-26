package edu.sentise.test;

import weka.classifiers.misc.InputMappedClassifier;
import weka.core.Instance;

public class CustomImputMappedClassifer extends InputMappedClassifier {

	public String getConvertedInstance(Instance instance)
	{
		try{
			String str="";
		Instance ins=constructMappedInstance(instance);
		int len= ins.numAttributes();
		for(int i=0;i<len;i++)
		{
			str+=ins.stringValue(i)+ ins.toString(i);
		}
		return str;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
