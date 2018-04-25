#!/usr/bin/python
import subprocess as sp
arg_list=[['java','-jar','sentise.jar','-algo','RF','-ngram'],
['java','-jar','sentise.jar','-algo','SL','-ngram'],['java','-jar','sentise.jar','-algo','CNN','-ngram']]
for arg in arg_list:
	sp.call(arg)
	
