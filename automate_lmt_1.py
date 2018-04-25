#!/usr/bin/python
import subprocess as sp
arg_list=[['java','-jar','sentise.jar','-algo','LMT','-ngram'],['java','-jar','sentise.jar','-algo','LMT','-negate'],
['java','-jar','sentise.jar','-algo','LMT','-tag','1'],['java','-jar','sentise.jar','-algo','LMT','-tag','2'],
['java','-jar','sentise.jar','-algo','LMT','-punctuation','1'],
['java','-jar','sentise.jar','-algo','LMT','-punctuation','2']]
for arg in arg_list:
	sp.call(arg)
	
