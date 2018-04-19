#!/usr/bin/python
import subprocess as sp
arg_list=[['java','-jar','sentise.jar','-algo','RF','-negate'],['java','-jar','sentise.jar','-algo','RF','-ngram'],
['java','-jar','sentise.jar','-algo','RF','-tag','1'],['java','-jar','sentise.jar','-algo','RF','-tag','2'],['java','-jar','sentise.jar','-algo','RF','-punctuation','1'],
['java','-jar','sentise.jar','-algo','RF','-punctuation','2'],['java','-jar','sentise.jar','-algo','RF','-root','1'],
['java','-jar','sentise.jar','-algo','RF','-root','2'],['java','-jar','sentise.jar','-algo','RF','-features','2'],
['java','-jar','sentise.jar','-algo','RF','-sentiword','2'],['java','-jar','sentise.jar','-algo','RF','-sentiword','4']]
for arg in arg_list:
	sp.call(arg)
	
