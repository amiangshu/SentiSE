package edu.sentise.model;

import java.io.Serializable;

import weka.classifiers.Classifier;
import weka.classifiers.misc.InputMappedClassifier;

public class ClassifierModel implements Serializable{
	private static final long serialVersionUID = 1L;
	private String[] params;
	private InputMappedClassifier inputMappedClassifier;
	public String[] getParams() {
		return params;
	}
	public void setParams(String[] params) {
		this.params = params;
	}
	public InputMappedClassifier getClassifier() {
		return inputMappedClassifier;
	}
	public void setClassifier(InputMappedClassifier classifier) {
		this.inputMappedClassifier = classifier;
	}
	public ClassifierModel()
	{
		
	}
	public ClassifierModel(String[] params, InputMappedClassifier classifier) {
		super();
		this.params = params;
		this.inputMappedClassifier = classifier;
	}
	
}
