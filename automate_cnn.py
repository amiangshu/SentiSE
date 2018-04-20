#!/usr/bin/python
import subprocess as sp
arg_list=[['java','-jar','sentise.jar','-algo','CNN','-ngram'],['java','-jar','sentise.jar','-algo','CNN','-negate'],
['java','-jar','sentise.jar','-algo','CNN','-tag','1'],['java','-jar','sentise.jar','-algo','CNN','-tag','2'],
['java','-jar','sentise.jar','-algo','CNN','-punctuation','1'],
['java','-jar','sentise.jar','-algo','CNN','-punctuation','2'],['java','-jar','sentise.jar','-algo','CNN','-root','1'],
['java','-jar','sentise.jar','-algo','CNN','-root','2'],['java','-jar','sentise.jar','-algo','CNN','-features','2'],
['java','-jar','sentise.jar','-algo','CNN','-sentiword','2'],['java','-jar','sentise.jar','-algo','CNN','-sentiword','4']]
for arg in arg_list:
	sp.call(arg)
	
